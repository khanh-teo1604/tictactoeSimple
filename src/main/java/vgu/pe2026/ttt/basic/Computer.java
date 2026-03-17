package vgu.pe2026.ttt.basic;

import java.util.Random;

public class Computer extends Player {

	Random random = new Random();

	public Computer(int symbol) {
		super(symbol);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void makeMove(Board board) {
		// TODO Auto-generated method stub
		// int move = random.nextInt(9) + 1;

		// // If the move is not valid we just simply add it to 1
		// // If the move is exceed we use %
		// while (!board.isValidMove(move) && !board.isFull()) {
		// move += 1;
		// move %= 10;
		// }

		// The computer just choose the first available cell from 1 to 9
		int move;
		for (move = 1; move <= 9; move++) {
			if (board.isValidMove(move)) {
				break;
			}
		}
		System.out.println("Computer choose: " + move);
		board.placeMove(move, getSymbol());
	}
}
