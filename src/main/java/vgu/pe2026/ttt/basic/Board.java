package vgu.pe2026.ttt.basic;

public class Board {
	private int[] cells = new int[10]; // Initialize

	// At the beginning, all the cells display 0
	public Board() {
		for (int i = 0; i < 9; i++) {
			cells[i] = 0;
		}
	}

	// Displayed table
	public void display() {
		for (int i = 0; i < 9; i += 3) {
			System.out.println("| " + cells[i] + " | " + cells[i + 1] + " | " + cells[i + 2] + " |");
		}
	}

	public boolean isValidMove(int move) {
		return move >= 1 && move <= 9 && cells[move - 1] == 0;
	}

	public void placeMove(int move, int symbol) {
		cells[move - 1] = symbol;
	}

	// Check if the board is full
	public boolean isFull() {
		for (int i = 0; i < 9; i++) {
			if (cells[i] == 0)
				return false;
		}
		return true;
	}

	public boolean isDraw() {
		if (hasWinner())
			return false;
		if (!isFull())
			return false;
		return true;
	}

	// Check if the winning game and it there are no Winner -> return 0
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
			if (cells[winPattern[0] - 1] == cells[winPattern[1] - 1]
					& cells[winPattern[1] - 1] == cells[winPattern[2] - 1]) {
				return cells[winPattern[0] - 1];
			}
		}
		return 0;
	}

	public boolean hasWinner() {
		return checkWinner() != 0;
	}
}
