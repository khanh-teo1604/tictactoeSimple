package vgu.pe2026.ttt.basic.HTTP;

public class GameRequest {

    private int clientMove;
    private int[] board;

    private String messageSignature;
    private String nonce;
    private String createdTime;

    public GameRequest() {
    }

    public GameRequest(
            int clientMove,
            int[] board,
            String messageSignature,
            String nonce,
            String createdTime) {

        this.clientMove = clientMove;
        this.board = board;
        this.messageSignature = messageSignature;
        this.nonce = nonce;
        this.createdTime = createdTime;
    }

    public int getClientMove() {
        return clientMove;
    }

    public void setClientMove(int clientMove) {
        this.clientMove = clientMove;
    }

    public int[] getBoard() {
        return board;
    }

    public void setBoard(int[] board) {
        this.board = board;
    }

    public String getMessageSignature() {
        return messageSignature;
    }

    public void setMessageSignature(String messageSignature) {
        this.messageSignature = messageSignature;
    }

    public String getNonce() {
        return nonce;
    }

    public void setNonce(String nonce) {
        this.nonce = nonce;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }
}