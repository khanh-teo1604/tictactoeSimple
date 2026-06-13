package vgu.pe2026.ttt.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Scanner;

import org.junit.jupiter.api.Test;

import vgu.pe2026.ttt.basic.Constant.PlayerType;

public class HumanTest {
    Board board = new Board1D();

    @Test
    void testMakeMove() {
        Scanner scanner = new Scanner("5\n");
        Human human = new Human(PlayerType.HUMAN, scanner);
        int move = human.makeMove(board);
        assertEquals(5, move);
    }

}
