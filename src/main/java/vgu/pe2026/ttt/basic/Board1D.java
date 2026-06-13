package vgu.pe2026.ttt.basic;

import static vgu.pe2026.ttt.basic.Constant.Setting.EMPTY_CELL;
import static vgu.pe2026.ttt.basic.Constant.Setting.NUMBER_COLUMN;
import static vgu.pe2026.ttt.basic.Constant.Setting.NUMBER_ROWS;

import vgu.pe2026.ttt.basic.Constant.PlayerType;

public class Board1D implements Board {
	private int[] cells = new int[NUMBER_ROWS * NUMBER_COLUMN]; // Initialize

	// At the beginning, all the cells display empty
	public Board1D() {
		for (int i = 0; i < NUMBER_ROWS * NUMBER_COLUMN; i++) {
			cells[i] = EMPTY_CELL;
		}
	}

	@Override
	public void setCells(int[][] allCells) {
		for (int i = 0; i < NUMBER_ROWS; i++) {
			System.arraycopy(allCells[i], 0, cells, i * NUMBER_COLUMN, allCells[i].length);
		}
	}

	// Displayed table
	@Override
	public void display() {
		System.out.print(displayBoard());
	}

	@Override
	public String displayBoard() {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < NUMBER_ROWS; i++) {
			for (int j = 0; j < NUMBER_COLUMN; j++) {
				output.append(" | ").append(cells[i * NUMBER_COLUMN + j]);
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
			for (int j = 0; j < NUMBER_COLUMN; j++) {
				snapshot[i][j] = cells[i * NUMBER_COLUMN + j];
			}
		}
		return snapshot;
	}

	@Override
	public boolean isMoveWithinTheRange(int move) {
		return move >= 1 && move <= NUMBER_COLUMN * NUMBER_ROWS;
	}

	@Override
	public boolean isOccupied(int move) {
		return cells[move - 1] != EMPTY_CELL;
	}

	@Override
	public void placeMove(int move, int symbol) {
		cells[move - 1] = symbol;
	}

	// Check if the board is full
	@Override
	public boolean isFull() {
		for (int i = 0; i < NUMBER_COLUMN * NUMBER_ROWS; i++) {
			if (cells[i] == EMPTY_CELL)
				return false;
		}
		return true;
	}

	@Override
	public boolean isEmpty() {
		for (int cell : cells) {
			if (cell != EMPTY_CELL) {
				return false;
			}
		}
		return true;
	}

	// Check if the winning game and it there are no Winner -> return 0
	@Override
	public int checkWinner() {
		int[][] winPatterns = {
				{ 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 }, // rows
				{ 1, 4, 7 }, { 2, 5, 8 }, { 3, 6, 9 }, // cols
				{ 1, 5, 9 }, { 3, 5, 7 } // diagonals
		};

		// In the cells cells[0] is in the position 1, cells[1] is in the position 2 and
		// so on
		// That's why the wining pattern need to -1 to have the correct position in
		// cells
		for (int[] winPattern : winPatterns) {
			if (cells[winPattern[0] - 1] != EMPTY_CELL
					&& cells[winPattern[0] - 1] == cells[winPattern[1] - 1]
					&& cells[winPattern[1] - 1] == cells[winPattern[2] - 1]) {
				return cells[winPattern[0] - 1];
			}
		}
		return 0;
	}
}
