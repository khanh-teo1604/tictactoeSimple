package vgu.pe2026.ttt.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static vgu.pe2026.ttt.basic.Constant.Setting.EMPTY_CELL;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.*;

import vgu.pe2026.ttt.basic.Constant.PlayerType;

/**
 * Unit test for simple App.
 */
public class BoardTest {
    private final PrintStream originalOutput = System.out;
    private PipedOutputStream outputStream;
    private BufferedReader scanner;
    private final Board1D board = new Board1D();
    private int HUMAN = PlayerType.HUMAN.getplayerTypeSymbol();
    private int COMPUTER = PlayerType.COMPUTER.getplayerTypeSymbol();

    @BeforeEach
    void setup() {
        outputStream = new PipedOutputStream();
        PipedInputStream inputStream;

        try {
            inputStream = new PipedInputStream(outputStream);
            scanner = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.setOut(new PrintStream(outputStream, true));

    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOutput);
    }

    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    public void testDisplayBoard() throws IOException {
        board.display();
        String expectedOutput = " | 0 | 0 | 0 | ";
        assertEquals(expectedOutput, scanner.readLine());
        assertEquals(expectedOutput, scanner.readLine());
        assertEquals(expectedOutput, scanner.readLine());
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
