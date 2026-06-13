package vgu.pe2026.ttt.basic;

public class MoveValidator {
    public static final String INVALID_NUMBER_MESSAGE = "Please, input a valid number [1-9]";
    public static final String OCCUPIED_CELL_MESSAGE = "The cell is occupied!";

    private final Board board;

    public MoveValidator(Board board) {
        this.board = board;
    }

    public boolean isValidMove(int move) {
        return getInvalidMoveMessage(move).isEmpty();
    }

    public String getInvalidMoveMessage(int move) {
        if (!board.isMoveWithinTheRange(move)) {
            return INVALID_NUMBER_MESSAGE;
        }

        if (board.isOccupied(move)) {
            return OCCUPIED_CELL_MESSAGE;
        }

        return "";
    }
}
