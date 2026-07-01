package vgu.pe2026.ttt.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

import org.junit.jupiter.api.Test;


public class HumanPlayerTest {
    Board board = new Board1D();

    @Test
    void testMakeMove() {
        Scanner scanner = new Scanner("5\n");
        Player human = new HumanPlayerFactory(scanner).createPlayer();
        int move = human.makeMove(board);
        assertEquals(5, move);
    }

}
