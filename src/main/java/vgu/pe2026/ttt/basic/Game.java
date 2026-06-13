package vgu.pe2026.ttt.basic;

import vgu.pe2026.ttt.basic.Constant.GameStatus;

public class Game {
    private final Board board;
    private final MoveValidator moveValidator;

    public Game(Board board) {
        this.board = board;
        this.moveValidator = new MoveValidator(board);
    }

    public GameMoveResult playMove(int move, int playerSymbol) {
        String invalidMoveMessage = getInvalidMoveMessage(move);
        if (!invalidMoveMessage.isEmpty()) {
            return new GameMoveResult(GameStatus.USER_WRONG_INPUT, invalidMoveMessage, false);
        }

        board.placeMove(move, playerSymbol);
        return getCurrentBoardStatus(playerSymbol, true);
    }

    public GameMoveResult getCurrentBoardStatus(int lastPlayerSymbol) {
        return getCurrentBoardStatus(lastPlayerSymbol, false);
    }

    private GameMoveResult getCurrentBoardStatus(int lastPlayerSymbol, boolean boardChanged) {
        if (board.checkWinner() != 0) {
            return new GameMoveResult(GameStatus.GAME_OVER, "Player#" + lastPlayerSymbol + " won!", boardChanged);
        }

        if (board.isFull()) {
            return new GameMoveResult(GameStatus.GAME_OVER, "It is a draw!", boardChanged);
        }

        return new GameMoveResult(GameStatus.GAME_RUNNING, "", boardChanged);
    }

    public boolean isValidMove(int move) {
        return moveValidator.isValidMove(move);
    }

    public String getInvalidMoveMessage(int move) {
        return moveValidator.getInvalidMoveMessage(move);
    }
}
