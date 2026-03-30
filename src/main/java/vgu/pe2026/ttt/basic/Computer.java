package vgu.pe2026.ttt.basic;

import java.util.Random;

public class Computer extends Player {

	Random random = new Random();

	public Computer(int symbol) {
		super(symbol);
	}

	@Override
	public void makeMove(Board board) {
		int move;
		for (move = 1; move <= 9; move++) {
			if (board.isValidMove(move)) {
				break;
			}
		}
		System.out.println("Computer choose: " + move);
		board.placeMove(move, getplayerTypeSymbol());
	}

	@Override
	public String namePlayerType() {
		return "Computer";
	}
}
