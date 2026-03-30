package vgu.pe2026.ttt.basic;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static vgu.pe2026.ttt.basic.Constant.COMPUTER;
import static vgu.pe2026.ttt.basic.Constant.HUMAN;

public class Game {

	public static void main(String[] args) {

		int currentPlayerIndex = Integer.parseInt(args[0]);
		Scanner scanner = new Scanner(System.in);
		Board board = new Board();

		Map<Integer, Player> allPlayers = new HashMap<>();
		allPlayers.put(HUMAN, new Human(HUMAN, scanner));
		allPlayers.put(COMPUTER, new Computer(COMPUTER));

		board.display();
		Player currentPlayer = allPlayers.get(currentPlayerIndex);

		while (true) {
			currentPlayer.makeMove(board);
			board.display();

			if (board.hasWinner()) {
				System.out.println(currentPlayer.namePlayerType() + " win");
				break;
			}

			// isFull does not mean
			if (board.isDraw()) {
				System.out.println("It's draw");
				break;
			}

			currentPlayerIndex = currentPlayerIndex % allPlayers.size() + 1;
			currentPlayer = allPlayers.get(currentPlayerIndex);
		}
		scanner.close();
	}

}
