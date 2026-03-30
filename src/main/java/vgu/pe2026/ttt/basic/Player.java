package vgu.pe2026.ttt.basic;

abstract class Player {
	private int playerTypeSymbol; // 1 for human 2 for computer

	public Player(int playerTypeSymbol) {
		this.playerTypeSymbol = playerTypeSymbol;
	}

	public abstract void makeMove(Board board);

	public abstract String namePlayerType();

	public int getplayerTypeSymbol() {
		return playerTypeSymbol;
	}
}
