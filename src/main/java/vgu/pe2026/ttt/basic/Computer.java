package vgu.pe2026.ttt.basic;

import java.util.Random;

public class Computer extends Player {

	Random random = new Random();

	public Computer(int symbol) {
		super(symbol);
	}

	@Override
	public int makeMove(Board board) {
		// TODO Auto-generated method stub
		int move;
		for (move = 1; move <= 9; move++) {
			if (isValidMove(move, board)) {
				break;
			}
		}
		System.out.println("Computer choose: " + move);
		return move;
	}

	@Override
	public String namePlayerType() {
		return "Computer";
	}
}
