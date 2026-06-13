package vgu.pe2026.ttt.basic;

import vgu.pe2026.ttt.basic.Constant.GameStatus;

public class GameMoveResult {
    private final GameStatus status;
    private final String message;
    private final boolean boardChanged;

    public GameMoveResult(GameStatus status, String message, boolean boardChanged) {
        this.status = status;
        this.message = message;
        this.boardChanged = boardChanged;
    }

    public GameStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public boolean isBoardChanged() {
        return boardChanged;
    }
}
