package vgu.pe2026.ttt.basic;

public interface Board {
    public void setCells(int[][] allCells);

    public void display();

    public boolean isMoveWithinTheRange(int move);

    public boolean isOccupied(int move);

    public void placeMove(int move, int symbol);

    public boolean isFull();

    public int checkWinner();

}
