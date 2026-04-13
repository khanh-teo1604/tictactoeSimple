package vgu.pe2026.ttt.basic;

import static vgu.pe2026.ttt.basic.Constant.NOT_INTEGER_MOVE;

import java.util.Scanner;

public class Human extends Player {

	private Scanner scanner;

	public Human(int symbol, Scanner scanner) {
		super(symbol);
		// TODO Auto-generated constructor stub
		this.scanner = scanner;
	}

	@Override
	public int makeMove(Board board) {
		int move;
		System.out.print("Enter your move: ");
		try {
			move = Integer.parseInt(scanner.nextLine());
		} catch (NumberFormatException e) {
			// TODO: handle exception
			return NOT_INTEGER_MOVE;
		}
		return move;
	}

	@Override
	public String namePlayerType() {
		return "Human";
	}
}
