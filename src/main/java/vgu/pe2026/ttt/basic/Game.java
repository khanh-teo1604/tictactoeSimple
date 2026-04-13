package vgu.pe2026.ttt.basic;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static vgu.pe2026.ttt.basic.Constant.COMPUTER;
import static vgu.pe2026.ttt.basic.Constant.HUMAN;
import static vgu.pe2026.ttt.basic.Constant.NOT_INTEGER_MOVE;

public class Game {

	public static void main(String[] args) {

		int currentPlayerIndex = Integer.parseInt(args[0]);
		Scanner scanner = new Scanner(System.in);
		Board1D board = new Board1D();

		Map<Integer, Player> allPlayers = new HashMap<>();
		allPlayers.put(HUMAN, new Human(HUMAN, scanner));
		allPlayers.put(COMPUTER, new Computer(COMPUTER));

		board.display();
		Player currentPlayer = allPlayers.get(currentPlayerIndex);

		while (true) {
			int playerMove = currentPlayer.makeMove(board);

			if (playerMove == NOT_INTEGER_MOVE) {
				System.out.println("Please input integer");
				continue;
			}
			if (!board.isMoveWithinTheRange(playerMove)) {
				System.out.println("Please input only from 1 to 9");
				continue;
			}
			if (board.isOccupied(playerMove)) {
				System.out.println("The move is Occupied, try another move");
				continue;
			}
			board.placeMove(playerMove, currentPlayer.getplayerTypeSymbol());
			board.display();

			if (board.checkWinner() != 0) {
				System.out.println(currentPlayer.namePlayerType() + " win");
				break;
			}

			if (board.isFull()) {
				System.out.println("It's draw");
				break;
			}

			currentPlayerIndex = currentPlayerIndex % allPlayers.size() + 1;
			currentPlayer = allPlayers.get(currentPlayerIndex);
		}
		scanner.close();
	}

}
