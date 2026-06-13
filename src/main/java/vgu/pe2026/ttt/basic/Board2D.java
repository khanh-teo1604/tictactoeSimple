package vgu.pe2026.ttt.basic;

import static vgu.pe2026.ttt.basic.Constant.Setting.EMPTY_CELL;
import static vgu.pe2026.ttt.basic.Constant.Setting.NUMBER_COLUMN;
import static vgu.pe2026.ttt.basic.Constant.Setting.NUMBER_ROWS;

import vgu.pe2026.ttt.basic.Constant.PlayerType;

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
        System.out.print(displayBoard());
    }

    @Override
    public String displayBoard() {
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < NUMBER_ROWS; i++) {
            for (int j = 0; j < NUMBER_COLUMN; j++) {
                output.append(" | ").append(cells[i][j]);
            }
            output.append(" | ").append(System.lineSeparator());
        }
        return output.toString();
    }

    private String displayCell(int cell) {
        if (cell == PlayerType.HUMAN.getplayerTypeSymbol()) {
            return "X";
        }
        if (cell == PlayerType.COMPUTER.getplayerTypeSymbol()) {
            return "O";
        }
        return "_";
    }

    @Override
    public int[][] getCells() {
        int[][] snapshot = new int[NUMBER_ROWS][NUMBER_COLUMN];
        for (int i = 0; i < NUMBER_ROWS; i++) {
            System.arraycopy(cells[i], 0, snapshot[i], 0, NUMBER_COLUMN);
        }
        return snapshot;
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
    public boolean isEmpty() {
        for (int i = 0; i < NUMBER_ROWS; i++) {
            for (int j = 0; j < NUMBER_COLUMN; j++) {
                if (cells[i][j] != EMPTY_CELL) {
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
            if (cells[i][0] != EMPTY_CELL &&
                    cells[i][0] == cells[i][1] &&
                    cells[i][1] == cells[i][2]) {
                return cells[i][0];
            }
        }

        // Columns
        for (int j = 0; j < 3; j++) {
            if (cells[0][j] != EMPTY_CELL &&
                    cells[0][j] == cells[1][j] &&
                    cells[1][j] == cells[2][j]) {
                return cells[0][j];
            }
        }

        // Diagonals
        if (cells[0][0] != EMPTY_CELL &&
                cells[0][0] == cells[1][1] &&
                cells[1][1] == cells[2][2]) {
            return cells[0][0];
        }

        if (cells[0][2] != EMPTY_CELL &&
                cells[0][2] == cells[1][1] &&
                cells[1][1] == cells[2][0]) {
            return cells[0][2];
        }
        return 0;
    }

}
