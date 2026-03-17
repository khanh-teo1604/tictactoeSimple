package vgu.pe2026.ttt.basic;

abstract class Player {
	private int symbol; // 1 for human 2 for computer

	public Player(int symbol) {
		this.symbol = symbol;
	}

	public abstract void makeMove(Board board);

	public int getSymbol() {
		return symbol;
	}
}
