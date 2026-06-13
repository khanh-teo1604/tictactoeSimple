package vgu.pe2026.ttt.basic.server;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import vgu.pe2026.ttt.basic.Board;

public final class MessageSignature {
    private static final int NO_MOVE = -1;
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final byte[] MASTER_SECRET = loadMasterSecret();

    private MessageSignature() {
    }

    public static boolean isAuthentic(Board board, String signature, String nonce, String createdTime, int clientMove) {
        if (clientMove == NO_MOVE && board.isEmpty()) {
            return true;
        }

        if (signature == null || signature.isBlank() || nonce == null || createdTime == null) {
            return false;
        }

        return MessageDigest.isEqual(
                sign(board, nonce, createdTime).getBytes(StandardCharsets.UTF_8),
                signature.getBytes(StandardCharsets.UTF_8));
    }

    public static String sign(Board board, String nonce, String createdTime) {
        return signValue(String.join("|", board.toPayload(), nonce, createdTime));
    }

    public static boolean matchesHash(String value, String hash) {
        if (value == null || hash == null || hash.isBlank()) {
            return false;
        }

        return MessageDigest.isEqual(
                signValue(value).getBytes(StandardCharsets.UTF_8),
                hash.getBytes(StandardCharsets.UTF_8));
    }

    private static String signValue(String value) {
        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            mac.init(new SecretKeySpec(MASTER_SECRET, HMAC_ALGORITHM));
            mac.update(value.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(mac.doFinal());
        } catch (Exception e) {
            throw new IllegalStateException("Could not sign value", e);
        }
    }

    private static byte[] loadMasterSecret() {
        String configuredSecret = System.getenv("TTT_SERVER_SECRET");
        if (configuredSecret != null && !configuredSecret.isBlank()) {
            return configuredSecret.getBytes(StandardCharsets.UTF_8);
        }

        byte[] generatedSecret = new byte[32];
        new SecureRandom().nextBytes(generatedSecret);
        return generatedSecret;
    }
}
