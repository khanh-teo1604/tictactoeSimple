package vgu.pe2026.ttt.basic;

import static vgu.pe2026.ttt.basic.Constant.EMPTY_CELL;
import static vgu.pe2026.ttt.basic.Constant.NUMBER_COLUMN;
import static vgu.pe2026.ttt.basic.Constant.NUMBER_ROWS;

public class Board2D implements Board {

    private int[][] cells = new int[NUMBER_COLUMN][NUMBER_ROWS];

    public Board2D() {
        for (int i = 0; i < NUMBER_ROWS; i++) {
            for (int j = 0; j < NUMBER_COLUMN; j++) {
                cells[i][j] = EMPTY_CELL;
            }
        }
    }

    @Override
    public void setCells(int[][] allCells) {
        for (int i = 0; i < NUMBER_ROWS; i++) {
            System.arraycopy(allCells[i], 0, cells[i], 0, allCells[i].length);
        }
    }

    @Override
    public void display() {
        // TODO: Implement display logic
        for (int i = 0; i < NUMBER_ROWS; i++) {
            for (int j = 0; j < NUMBER_COLUMN; j++) {
                System.out.print(" | " + cells[i][j]);
            }
            System.out.print(" | \n");
        }
    }

    @Override
    public boolean isOccupied(int move) {
        // TODO: Implement isOccupied logic
        int rowPosition = (move - 1) / NUMBER_ROWS;
        int columnPosition = (move - 1) % NUMBER_ROWS;
        return cells[rowPosition][columnPosition] != EMPTY_CELL;
    }

    @Override
    public boolean isMoveWithinTheRange(int move) {
        // TODO: Implement isMoveWithinTheRange logic
        return move >= 1 && move <= 9;
    }

    @Override
    public boolean isFull() {
        // TODO: Implement isFull logic
        for (int i = 0; i < NUMBER_ROWS; i++) {
            for (int j = 0; j < NUMBER_COLUMN; j++) {
                if (cells[i][j] == EMPTY_CELL) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void placeMove(int move, int symbol) {
        // TODO: Implement placeMove logic
        int rowPosition = (move - 1) / NUMBER_ROWS;
        int columnPosition = (move - 1) % NUMBER_ROWS;
        cells[rowPosition][columnPosition] = symbol;

    }

    @Override
    public int checkWinner() {
        // TODO: Implement checkWinner logic
        for (int i = 0; i < 3; i++) {
            if (cells[i][0] == cells[i][1] &&
                    cells[i][1] == cells[i][2]) {
                return cells[i][0];
            }
        }

        // Columns
        for (int j = 0; j < 3; j++) {
            if (cells[0][j] == cells[1][j] &&
                    cells[1][j] == cells[2][j]) {
                return cells[0][j];
            }
        }

        // Diagonals
        if (cells[0][0] == cells[1][1] &&
                cells[1][1] == cells[2][2]) {
            return cells[0][0];
        }

        if (cells[0][2] == cells[1][1] &&
                cells[1][1] == cells[2][0]) {
            return cells[0][2];
        }
        return 0;
    }

}
