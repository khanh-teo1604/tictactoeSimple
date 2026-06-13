package vgu.pe2026.ttt.basic.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import vgu.pe2026.ttt.basic.Board1D;
import vgu.pe2026.ttt.basic.Constant.GameStatus;
import vgu.pe2026.ttt.basic.Constant.Setting;

class TicTacToeServerNonceTest {
    private static final long CREATED_TIME = 1_000L;
    private static final long FRESH_TIME = CREATED_TIME + 1_000L;
    private static final long EXPIRED_TIME = CREATED_TIME + Setting.DECLINE_TIME * 1_000L + 1L;

    @BeforeEach
    void resetNonceDatabase() {
        NonceChallenge.resetDatabaseForTest();
    }

    @Test
    void nonceIsAcceptedOnlyOnce() {
        NonceChallenge challenge = NonceChallenge.create(CREATED_TIME);

        assertTrue(NonceChallenge.accept(
                challenge.nonce,
                challenge.createdTime,
                FRESH_TIME));

        assertFalse(NonceChallenge.accept(
                challenge.nonce,
                challenge.createdTime,
                FRESH_TIME));
    }

    @Test
    void expiredNonceIsRejected() {
        NonceChallenge challenge = NonceChallenge.create(CREATED_TIME);

        assertFalse(NonceChallenge.accept(
                challenge.nonce,
                challenge.createdTime,
                EXPIRED_TIME));
    }

    @Test
    void blankNonceIsRejected() {
        NonceChallenge challenge = NonceChallenge.create(CREATED_TIME);

        assertFalse(NonceChallenge.accept(
                "",
                challenge.createdTime,
                FRESH_TIME));
    }

    @Test
    void tamperedTimestampIsRejected() {
        NonceChallenge challenge = NonceChallenge.create(CREATED_TIME);

        assertFalse(NonceChallenge.accept(
                challenge.nonce,
                "different timestamp",
                FRESH_TIME));
    }

    @Test
    void responseContainsNonceChallengeFields() throws Exception {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream output = new DataOutputStream(bytes);

        TicTacToeServer.writeResponse(output, GameStatus.GAME_RUNNING, new Board1D(), "Player#1's turn");

        DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes.toByteArray()));
        assertEquals(GameStatus.GAME_RUNNING.name(), input.readUTF());
        for (int i = 0; i < Setting.NUMBER_ROWS * Setting.NUMBER_COLUMN; i++) {
            input.readInt();
        }
        assertEquals("Player#1's turn", input.readUTF());

        String nonce = input.readUTF();
        String createdTime = input.readUTF();
        String messageSignature = input.readUTF();

        assertEquals(6, nonce.length());
        assertTrue(MessageSignature.isAuthentic(new Board1D(), messageSignature, nonce, createdTime, 1));
    }
}
