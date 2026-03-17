package vgu.pe2026.ttt.basic;

import java.util.Scanner;

public class Game {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int starter = Integer.parseInt(args[0]);
		Scanner scanner = new Scanner(System.in);
		Board board = new Board();
		Player human = new Human(1, scanner);
		Computer computer = new Computer(2);

		board.display();
		Player currentPlayer = (starter == 1) ? human : computer;

		while (true) {
			currentPlayer.makeMove(board);
			board.display();

			if (board.isWinner(currentPlayer.getSymbol())) {
				if (currentPlayer.getSymbol() == 1) {
					System.out.println("Human wins");
				} else {
					System.out.println("Computer wins");
				}
				break;
			}
			if (board.isFull()) {
				System.out.println("It's draw");
				break;
			}

			currentPlayer = (currentPlayer == human) ? computer : human;
		}
		scanner.close();
	}

}
