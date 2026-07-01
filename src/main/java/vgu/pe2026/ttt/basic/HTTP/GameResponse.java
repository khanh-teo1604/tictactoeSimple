package vgu.pe2026.ttt.basic.HTTP;

import vgu.pe2026.ttt.basic.Constant.GameStatus;

public class GameResponse {
    private GameStatus status;
    private int[] board;
    private String message;
    private String nonce;
    private String createdTime;
    private String messageSignature;

    public GameResponse() {
    }

    public GameResponse(
            GameStatus status,
            int[] board,
            String message,
            String nonce,
            String createdTime,
            String messageSignature) {

        this.status = status;
        this.board = board;
        this.message = message;
        this.nonce = nonce;
        this.createdTime = createdTime;
        this.messageSignature = messageSignature;
    }

    public GameStatus getStatus() {
        return status;
    }

    public int[] getBoard() {
        return board;
    }

    public String getMessage() {
        return message;
    }

    public String getNonce() {
        return nonce;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public String getMessageSignature() {
        return messageSignature;
    }
}
