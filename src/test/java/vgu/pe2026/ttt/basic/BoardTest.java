package vgu.pe2026.ttt.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static vgu.pe2026.ttt.basic.Constant.COMPUTER;
import static vgu.pe2026.ttt.basic.Constant.HUMAN;
import static vgu.pe2026.ttt.basic.Constant.EMPTY_CELL;

import org.junit.jupiter.api.*;

/**
 * Unit test for simple App.
 */
public class BoardTest {

    private final Board1D board = new Board1D();

    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void checkBoardIsFull() {
        int cellsTest[][] = { { HUMAN, HUMAN, COMPUTER }, { COMPUTER, HUMAN, COMPUTER },
                { COMPUTER, HUMAN, COMPUTER } };
        board.setCells(cellsTest);
        assertTrue(board.isFull());
    }

    @Test
    public void checkBoardIsNotFull() {
        int cellsTest[][] = { { HUMAN, HUMAN, COMPUTER }, { COMPUTER, HUMAN, COMPUTER },
                { COMPUTER, HUMAN, EMPTY_CELL } };
        board.setCells(cellsTest);
        assertFalse(board.isFull());
    }

    @Test
    public void checkWinnerIsHuman() {
        int cellsTest[][] = { { HUMAN, HUMAN, HUMAN }, { COMPUTER, HUMAN, COMPUTER },
                { EMPTY_CELL, EMPTY_CELL, EMPTY_CELL } };
        board.setCells(cellsTest);
        assertEquals(HUMAN, board.checkWinner());
    }

    @Test
    public void checkWinnerIsComputer() {
        int cellsTest[][] = { { COMPUTER, HUMAN, HUMAN }, { COMPUTER, HUMAN, COMPUTER },
                { COMPUTER, EMPTY_CELL, EMPTY_CELL } };
        board.setCells(cellsTest);
        assertEquals(COMPUTER, board.checkWinner());
    }

    @Test
    public void checkThereIsNoWinner() {
        int cellsTest[][] = { { COMPUTER, HUMAN, HUMAN }, { COMPUTER, HUMAN, COMPUTER },
                { EMPTY_CELL, EMPTY_CELL, EMPTY_CELL } };
        board.setCells(cellsTest);
        assertEquals(EMPTY_CELL, board.checkWinner());
    }

    @Test
    public void testMoveWithinTheRange() {
        assertTrue(board.isMoveWithinTheRange(1));
    }

    @Test
    public void testMoveNotWithinTheRange() {
        assertFalse(board.isMoveWithinTheRange(-1));
    }

    @Test
    public void testIsOccupied() {
        int cellsTest[][] = { { COMPUTER, EMPTY_CELL, EMPTY_CELL }, { EMPTY_CELL, COMPUTER, COMPUTER },
                { HUMAN, HUMAN, HUMAN } };
        board.setCells(cellsTest);
        assertTrue(board.isOccupied(1));
    }

    @Test
    public void testIsNotOccupied() {
        int cellsTest[][] = { { COMPUTER, EMPTY_CELL, EMPTY_CELL }, { EMPTY_CELL, COMPUTER, COMPUTER },
                { HUMAN, HUMAN, HUMAN } };
        board.setCells(cellsTest);
        assertFalse(board.isOccupied(2));
    }

}
