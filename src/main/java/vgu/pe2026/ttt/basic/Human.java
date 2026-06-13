package vgu.pe2026.ttt.basic;

import java.util.Scanner;

import vgu.pe2026.ttt.basic.Constant.PlayerType;

public class Human extends Player {

	private Scanner scanner;

	public Human(PlayerType type, Scanner scanner) {
		super(type);
		// TODO Auto-generated constructor stub
		this.scanner = scanner;
	}

	@Override
	public int makeMove(Board board) {
		int move;
		MoveValidator moveValidator = new MoveValidator(board);
		while (true) {
			String input = scanner.nextLine();
			if (input.equals("q")) {
				return -1;
			}
			if (input.equals("n")) {
				return -2;
			}
			try {
				move = Integer.parseInt(input);
				String invalidMoveMessage = moveValidator.getInvalidMoveMessage(move);
				if (invalidMoveMessage.isEmpty()) {
					return move;
				}
				System.out.println(invalidMoveMessage);
				continue;
			} catch (NumberFormatException e) {
				// TODO: handle exception
				System.out.println(MoveValidator.INVALID_NUMBER_MESSAGE);

			}
		}

	}

}
