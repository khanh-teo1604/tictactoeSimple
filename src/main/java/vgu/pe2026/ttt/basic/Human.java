package vgu.pe2026.ttt.basic;

import java.util.Scanner;

public class Human extends Player {

	private Scanner scanner;

	public Human(int symbol, Scanner scanner) {
		super(symbol);
		// TODO Auto-generated constructor stub
		this.scanner = scanner;
	}

	@Override
	public void makeMove(Board board) {
		// TODO Auto-generated method stub
		int move;
		while (true) {
			System.out.println("Enter your move: ");
			move = scanner.nextInt();
			if (board.isValidMove(move)) {
				System.out.println("Human choose move: " + move);
				board.placeMove(move, getSymbol());
				break;
			} else {
				System.out.println("Invalid move");
			}
		}
	}
}
