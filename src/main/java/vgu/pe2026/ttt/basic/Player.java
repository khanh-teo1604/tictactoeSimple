package vgu.pe2026.ttt.basic;

import java.util.Scanner;

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

	public static Player create(PlayerType type, Scanner scanner) {
		return switch (type) {
			case HUMAN -> new Human(type, scanner);
			case COMPUTER -> new Computer(type);
			default -> throw new IllegalArgumentException("Unsupported player type: " + type);
		};
	}
}
