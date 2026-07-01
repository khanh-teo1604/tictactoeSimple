package vgu.pe2026.ttt.basic;

import vgu.pe2026.ttt.basic.Constant.PlayerType;

public abstract class Player {
	private int playerTypeSymbol;

	public Player(PlayerType type) {
		this.playerTypeSymbol = type.getplayerTypeSymbol();
	}

	public abstract int makeMove(Board board);

	public int getplayerTypeSymbol() {
		return playerTypeSymbol;
	}

	public boolean isValidMove(int move, Board board) {
		return new MoveValidator(board).isValidMove(move);
	}

}
