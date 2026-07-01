package vgu.pe2026.ttt.basic.server;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import vgu.pe2026.ttt.basic.Constant.Setting;

public class NonceChallenge {
    private static final int NONCE_BOUND = 1_000_000;
    private static final long DECLINE_TIME_MILLIS = Setting.DECLINE_TIME * 1000L;
    private static final SecureRandom RANDOM = new SecureRandom();
    private static final Map<String, Long> NONCES_DATABASE = new HashMap<>();
    private static long lastCleanupTime = System.currentTimeMillis();

    public final String nonce;
    public final String createdTime;

    NonceChallenge(String nonce, String createdTime) {
        this.nonce = nonce;
        this.createdTime = createdTime;
    }

    public static NonceChallenge create(long createdTime) {
        cleanupExpiredNoncesIfDue(createdTime);

        String nonce;
        do {
            nonce = String.format("%06d", RANDOM.nextInt(NONCE_BOUND));
        } while (NONCES_DATABASE.containsKey(nonce));

        String createdTimeText = String.valueOf(createdTime);
        return new NonceChallenge(nonce, createdTimeText);
    }

    public static boolean accept(String nonce, String createdTimeText, long currentTime) {
        cleanupExpiredNoncesIfDue(currentTime);

        Long createdTime = parseCreatedTime(createdTimeText);
        if (createdTime == null || createdTime + DECLINE_TIME_MILLIS < currentTime) {
            return false;
        }

        if (nonce == null || nonce.isBlank()) {
            return false;
        }

        if (NONCES_DATABASE.containsKey(nonce)) {
            return false;
        }

        NONCES_DATABASE.put(nonce, createdTime);
        return true;
    }

    static void resetDatabaseForTest() {
        NONCES_DATABASE.clear();
        lastCleanupTime = 0L;
    }

    private static Long parseCreatedTime(String createdTimeText) {
        if (createdTimeText == null || createdTimeText.isBlank()) {
            return null;
        }

        try {
            return Long.parseLong(createdTimeText);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static void cleanupExpiredNoncesIfDue(long currentTime) {
        if (lastCleanupTime + DECLINE_TIME_MILLIS > currentTime) {
            return;
        }

        cleanupExpiredNonces(NONCES_DATABASE, currentTime);
        lastCleanupTime = currentTime;
    }

    private static void cleanupExpiredNonces(Map<String, Long> nonces, long currentTime) {
        Iterator<Map.Entry<String, Long>> iterator = nonces.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();
            if (entry.getValue() + DECLINE_TIME_MILLIS < currentTime) {
                iterator.remove();
            }
        }
    }
}
