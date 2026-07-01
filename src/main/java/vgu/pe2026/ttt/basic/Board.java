package vgu.pe2026.ttt.basic;

import vgu.pe2026.ttt.basic.server.MessageSignature;

public interface Board {
    public void setCells(int[][] allCells);

    public void setCells(int[] allCells);

    public void display();

    public String displayBoard();

    public int[][] get2DCells();

    public int[] get1DCells();

    public boolean isMoveWithinTheRange(int move);

    public boolean isOccupied(int move);

    public void placeMove(int move, int symbol);

    public boolean isFull();

    public boolean isEmpty();

    public default String toPayload() {
        StringBuilder payload = new StringBuilder();
        for (int[] row : get2DCells()) {
            for (int cell : row) {
                payload.append(cell).append(',');
            }
        }
        return payload.toString();
    }

    public default boolean isValidBoard(String signature, String nonce, String createdTime, int clientMove) {
        return MessageSignature.isAuthentic(this, signature, nonce, createdTime, clientMove);
    }

    public int checkWinner();

}
