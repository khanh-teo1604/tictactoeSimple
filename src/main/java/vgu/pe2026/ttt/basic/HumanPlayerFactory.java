package vgu.pe2026.ttt.basic;

import java.util.Scanner;

public class HumanPlayerFactory extends PlayerFactory {

	private Scanner scanner;

	public HumanPlayerFactory(Scanner scanner) {
		this.scanner = scanner;
	}

	@Override
	public Player createPlayer() {
		return new HumanPlayer(scanner);
	}
}
