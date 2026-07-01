package vgu.pe2026.ttt.basic;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import vgu.pe2026.ttt.basic.Constant.GameStatus;
import vgu.pe2026.ttt.basic.Constant.PlayerType;

public class Main {

	public static void main(String[] args) {
		startLocalGame(args);
	}

	private static void startLocalGame(String[] args) {
		if (args.length != 1) {
			System.out.println("Please, input a valid option [1-2]");
			return;
		}

		if (!PlayerType.getValidArguments().contains(args[0])) {
			System.out.println("Please, input a valid option [1-2]");
			return;
		}

		int currentPlayerIndex;
		try {
			currentPlayerIndex = Integer.parseInt(args[0]);
		} catch (NumberFormatException e) {
			System.out.println("Please, input a valid option [1-2]");
			return;
		}

		Scanner scanner = new Scanner(System.in);
		Board1D board = new Board1D();

		List<Player> allPlayers = new ArrayList<>();
		allPlayers.add(new HumanPlayerFactory(scanner).createPlayer());
		allPlayers.add(new ComputerPlayerFactory().createPlayer());
		
		Game gameEngine = new Game(board);
		GameStatus status = GameStatus.GAME_RUNNING;
		System.out.println("Hello!");
		board.display();

		Player currentPlayer = allPlayers.get(currentPlayerIndex - 1);
		while (status != GameStatus.GAME_OVER) {
			System.out.println("Player#" + currentPlayer.getplayerTypeSymbol() + "'s turn");
			int playerMove = currentPlayer.makeMove(board);

			if (playerMove == -1) {
				System.out.println("End of the game");
				return;
			}

			GameMoveResult result = gameEngine.playMove(playerMove, currentPlayer.getplayerTypeSymbol());
			if (result.getStatus() == GameStatus.USER_WRONG_INPUT) {
				System.out.println(result.getMessage());
				continue;
			}

			if (result.isBoardChanged()) {
				board.display();
			}

			if (result.getStatus() == GameStatus.GAME_OVER) {
				System.out.println(result.getMessage());
			}

			status = result.getStatus();
			currentPlayerIndex = currentPlayerIndex % allPlayers.size() + 1;
			currentPlayer = allPlayers.get(currentPlayerIndex - 1);
		}
	}

}
