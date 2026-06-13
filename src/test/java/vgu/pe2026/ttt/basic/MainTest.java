package vgu.pe2026.ttt.basic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MainTest {

    private final PrintStream originalOutput = System.out;
    private final InputStream originalInput = System.in;

    @BeforeEach
    void setUp() {
        // outputStream = new PipedOutputStream();

        // PipedInputStream inputStream;
        // try {
        // inputStream = new PipedInputStream(outputStream);
        // scanner = new BufferedReader(new InputStreamReader(inputStream,
        // StandardCharsets.UTF_8));
        // } catch (IOException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        // System.setOut(new PrintStream(outputStream));
    }

    @AfterEach
    void tearDown() {
        System.setIn(originalInput);
        System.setOut(originalOutput);
    }

    private void skipLine(BufferedReader reader, int numberOfLines) throws IOException {
        for (int i = 0; i < numberOfLines; i++) {
            reader.readLine();
        }
    }

    private BufferedReader runApp(String[] args, String input) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output, true));

        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);

        Main.main(args);

        return new BufferedReader(
                new InputStreamReader(
                        new ByteArrayInputStream(output.toByteArray()),
                        StandardCharsets.UTF_8));
    }

    @Test
    void testNoArgument() throws IOException {
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        Main.main(new String[] { "" });

        byte[] printout = outputByteArray.toByteArray();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8));

        String expectedMessage = "Please, input a valid option [1-2]";
        assertEquals(expectedMessage, reader.readLine());
    }

    @Test
    void testWrongArgument() throws IOException {
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        Main.main(new String[] { "a" });
        byte[] printout = outputByteArray.toByteArray();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8));

        String expectedMessage = "Please, input a valid option [1-2]";
        assertEquals(expectedMessage, reader.readLine());
    }

    @Test
    void extraOptionAfterInvalidOptionTest() throws IOException {
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        Main.main(new String[] { "a", "extra" });

        byte[] printout = outputByteArray.toByteArray();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8));

        String expectedMessage = "Please, input a valid option [1-2]";
        assertEquals(expectedMessage, reader.readLine());
    }

    @Test
    void testInvalidArgumentValue() throws IOException {
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        Main.main(new String[] { "3" });

        byte[] printout = outputByteArray.toByteArray();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8));

        String expectedMessage = "Please, input a valid option [1-2]";
        assertEquals(expectedMessage, reader.readLine());
    }

    @Test
    void testFormatArgumentValue() throws IOException {
        ByteArrayOutputStream outputByteArray = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputByteArray, true));

        Main.main(new String[] { "01" });

        byte[] printout = outputByteArray.toByteArray();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(printout), StandardCharsets.UTF_8));

        String expectedMessage = "Please, input a valid option [1-2]";
        assertEquals(expectedMessage, reader.readLine());
    }

    @Test
    void startWithInitialBoard() throws IOException {
        BufferedReader reader = runApp(new String[] { "1" }, "q\n");

        String expectedMessage = "Hello!";
        assertEquals(expectedMessage, reader.readLine());

        String expectedOutput = " | 0 | 0 | 0 | ";
        assertEquals(expectedOutput, reader.readLine());
        assertEquals(expectedOutput, reader.readLine());
        assertEquals(expectedOutput, reader.readLine());
    }

    @Test
    void startGameWithHumanFirst() throws IOException {
        BufferedReader reader = runApp(new String[] { "1" }, "q\n");

        skipLine(reader, 4);

        assertEquals("Player#1's turn", reader.readLine());
    }

    @Test
    void startGameWithComputerFirst() throws IOException {
        BufferedReader reader = runApp(new String[] { "2" }, "q\n");

        skipLine(reader, 4);

        assertEquals("Player#2's turn", reader.readLine());
    }

    @Test
    void invalidNonIntegerInputInGame() throws IOException {
        BufferedReader reader = runApp(new String[] { "1" }, "abc\nq\n");

        String line;
        boolean found = false;

        while ((line = reader.readLine()) != null) {
            if (line.equals("Please, input a valid number [1-9]")) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    @Test
    void quitGame() throws IOException {
        BufferedReader reader = runApp(new String[] { "1" }, "q\n");

        String line;
        boolean found = false;

        while ((line = reader.readLine()) != null) {
            if (line.equals("End of the game")) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }

    @Test
    void qCaseSensitivity() throws IOException {
        BufferedReader reader = runApp(new String[] { "1" }, "Q\nq\n");

        String line;
        boolean foundInvalid = false;

        while ((line = reader.readLine()) != null) {
            if (line.equals("Please, input a valid number [1-9]")) {
                foundInvalid = true;
                break;
            }
        }

        assertTrue(foundInvalid);
    }

    @Test
    void invalidRangeNumbers() throws IOException {
        BufferedReader reader = runApp(new String[] { "1" }, "0\n10\n-3\nq\n");

        int count = 0;
        String line;

        while ((line = reader.readLine()) != null) {
            if (line.equals("Please, input a valid number [1-9]")) {
                count++;
            }
        }

        assertTrue(count >= 3);
    }

    @Test
    void occupiedCell() throws IOException {
        BufferedReader reader = runApp(new String[] { "1" }, "1\n1\nq\n");

        String line;

        boolean found = false;

        while ((line = reader.readLine()) != null) {
            if (line.equals("The cell is occupied!")) {
                found = true;
                break;
            }
        }

        assertTrue(found);

    }

    @Test
    void humanValidMoveUpdateBoard() throws IOException {
        BufferedReader reader = runApp(new String[] { "1" }, "1\nq\n");
        skipLine(reader, 5); // Hello and initial board and human move

        assertEquals(" | 1 | 0 | 0 | ", reader.readLine());
        assertEquals(" | 0 | 0 | 0 | ", reader.readLine());
        assertEquals(" | 0 | 0 | 0 | ", reader.readLine());

        assertEquals("Player#2's turn", reader.readLine());

    }

    // Not black box
    @Test
    void computerValidMoveUpdateBoard() throws IOException {
        BufferedReader reader = runApp(new String[] { "2" }, "1\nq\n");
        skipLine(reader, 5); // Hello and initial board and human move

        assertEquals(" | 2 | 0 | 0 | ", reader.readLine());
        assertEquals(" | 0 | 0 | 0 | ", reader.readLine());
        assertEquals(" | 0 | 0 | 0 | ", reader.readLine());

        assertEquals("Player#1's turn", reader.readLine());

    }

    // Not black box
    @Test
    void testTransition() throws IOException {
        BufferedReader reader = runApp(new String[] { "2" }, "2\nq\n");
        skipLine(reader, 5); // Hello and initial board and human move

        skipLine(reader, 3);
        assertEquals("Player#1's turn", reader.readLine());
        skipLine(reader, 3);
        assertEquals("Player#2's turn", reader.readLine());
    }

    /*
     * These 3 tests are not black box test
     */
    @Test
    void humanWin() throws IOException {
        BufferedReader reader = runApp(new String[] { "1" },
                "4\n5\n6\nq\n");

        String lastLine = null;
        String line;

        while ((line = reader.readLine()) != null) {
            lastLine = line;
        }

        assertEquals("Player#1 won!", lastLine);
    }

    // Not black box
    @Test
    void computerWin() throws IOException {
        BufferedReader reader = runApp(new String[] { "1" },
                "5\n6\n8\n9\nq\n");

        String lastLine = null;
        String line;

        while ((line = reader.readLine()) != null) {
            lastLine = line;
        }

        assertEquals("Player#2 won!", lastLine);
    }

    // Not black box
    @Test
    void drawDetectionAfterHumanMove() throws IOException {
        BufferedReader reader = runApp(new String[] { "1" },
                "2\n5\n4\n7\n9\nq\n");

        String lastLine = null;
        String line;

        while ((line = reader.readLine()) != null) {
            lastLine = line;
        }

        assertEquals("It is a draw!", lastLine);
    }

    // Not black box
    @Test
    void drawDetectionAfterComputerMove() throws IOException {
        BufferedReader reader = runApp(new String[] { "2" },
                "2\n5\n7\n9\nq\n");

        String lastLine = null;
        String line;

        while ((line = reader.readLine()) != null) {
            lastLine = line;
        }

        assertEquals("It is a draw!", lastLine);
    }

    // Not black box
    @Test
    void computerFirstAvailableCell() throws IOException {
        BufferedReader reader = runApp(new String[] { "2" }, "q\n");

        reader.readLine(); // Hello
        skipLine(reader, 3);
        reader.readLine(); // Player#2's turn

        String firstRow = reader.readLine();
        String firstCell = firstRow.replaceAll("[^0-9]", "").split("")[0];
        System.out.println(firstCell);

        assertTrue(firstCell.equals("2")); // first move at cell 1
    }

    // Not black box

    @Test
    void boardIntegrity() throws IOException {
        BufferedReader reader = runApp(new String[] { "1" },
                "1\n3\n5\nq\n");

        reader.readLine(); // Hello
        skipLine(reader, 3); // First initial board
        reader.readLine(); // Player's #1 turn

        assertEquals(" | 1 | 0 | 0 | ", reader.readLine());
        assertEquals(" | 0 | 0 | 0 | ", reader.readLine());
        assertEquals(" | 0 | 0 | 0 | ", reader.readLine());

        reader.readLine(); // Player's #2 turn

        assertEquals(" | 1 | 2 | 0 | ", reader.readLine());
        assertEquals(" | 0 | 0 | 0 | ", reader.readLine());
        assertEquals(" | 0 | 0 | 0 | ", reader.readLine());

        reader.readLine(); // Player's #1 turn

        assertEquals(" | 1 | 2 | 1 | ", reader.readLine());
        assertEquals(" | 0 | 0 | 0 | ", reader.readLine());
        assertEquals(" | 0 | 0 | 0 | ", reader.readLine());

        reader.readLine(); // Player's #2 turn

        assertEquals(" | 1 | 2 | 1 | ", reader.readLine());
        assertEquals(" | 2 | 0 | 0 | ", reader.readLine());
        assertEquals(" | 0 | 0 | 0 | ", reader.readLine());

        reader.readLine(); // Player's #1 turn

        assertEquals(" | 1 | 2 | 1 | ", reader.readLine());
        assertEquals(" | 2 | 1 | 0 | ", reader.readLine());
        assertEquals(" | 0 | 0 | 0 | ", reader.readLine());

    }

    @Test
    void turnPromptSequence() throws IOException {
        BufferedReader reader = runApp(new String[] { "1" },
                "abc\n1\n1\nq\n");

        String line;
        int count = 0;

        while ((line = reader.readLine()) != null) {
            if (line.equals("Please, input a valid number [1-9]")) {
                count++;
                break;
            }
        }

        while ((line = reader.readLine()) != null) {
            if (line.equals("Player#1's turn")) {
                count++;
                break;
            }
        }

        while ((line = reader.readLine()) != null) {
            if (line.equals("The cell is occupied!")) {
                count++;
                break;
            }
        }

        assertEquals(3, count);
    }

    @Test
    void programTerminatesCleanly() throws IOException {
        BufferedReader reader = runApp(new String[] { "1" }, "q\n");

        String lastLine = null, line;
        while ((line = reader.readLine()) != null) {
            lastLine = line;
        }

        assertEquals("End of the game", lastLine);
    }

    @Test
    void stressInvalidInputs() throws IOException {
        StringBuilder input = new StringBuilder();
        for (int i = 0; i < 100; i++)
            input.append("x\n");
        input.append("1\nq\n");

        BufferedReader reader = runApp(new String[] { "1" }, input.toString());

        String line;
        boolean accepted = false;

        while ((line = reader.readLine()) != null) {
            if (line.contains("1")) {
                accepted = true;
            }
        }

        assertTrue(accepted);
    }

    @Test
    void exactMessageCheck() throws IOException {
        BufferedReader reader = runApp(new String[] { "1" }, "abc\nq\n");

        String line;
        boolean found = false;

        while ((line = reader.readLine()) != null) {
            if (line.equals("Please, input a valid number [1-9]")) {
                found = true;
                break;
            }
        }

        assertTrue(found);
    }
}
