package vgu.pe2026.ttt.basic;

import java.util.Random;

import vgu.pe2026.ttt.basic.Constant.PlayerType;

public class Computer extends Player {

	Random random = new Random();

	public Computer(PlayerType type) {
		super(type);
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
		return move;
	}

}
