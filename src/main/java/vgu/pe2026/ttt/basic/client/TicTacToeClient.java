package vgu.pe2026.ttt.basic.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import vgu.pe2026.ttt.basic.Board;
import vgu.pe2026.ttt.basic.Board1D;
import vgu.pe2026.ttt.basic.HumanPlayerFactory;
import vgu.pe2026.ttt.basic.Player;
import vgu.pe2026.ttt.basic.Constant.GameStatus;
import vgu.pe2026.ttt.basic.Constant.Setting;

public class TicTacToeClient {
    private static final String DEFAULT_HOST = "localhost";
    private static final int DEFAULT_PORT = 1604;
    private static final int NO_MOVE = -1;
    private static final int NEW_GAME = -2;

    private Board board = new Board1D();
    private String messageSignature = "";
    private String nonce = "";
    private String createdTime = "";

    public static void main(String[] args) throws IOException {
        if (args.length != 0) {
            return;
        }

        new TicTacToeClient().start(DEFAULT_HOST, DEFAULT_PORT);
    }

    public void start(String host, int port) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Hello!");
            Player human = new HumanPlayerFactory(scanner).createPlayer();
            if (playTurnWithServer(host, port, NO_MOVE)) {
                return;
            }

            while (true) {
                int move = human.makeMove(board);
                if (move == NO_MOVE) {
                    System.out.println("End of the game!");
                    return;
                }

                if (move == NEW_GAME) {
                    System.out.println("Starting a new game!");
                    move = NO_MOVE;
                    board = new Board1D();
                    messageSignature = "";
                    nonce = "";
                    createdTime = "";
                }

                if (playTurnWithServer(host, port, move)) {
                    return;
                }
            }
        }
    }

    private boolean playTurnWithServer(String host, int port, int humanMove)
            throws IOException {
        try (Socket socket = new Socket(host, port);
                DataOutputStream output = new DataOutputStream(socket.getOutputStream());
                DataInputStream input = new DataInputStream(socket.getInputStream())) {

            sendMoveToServer(output, humanMove);
            return readResponseFromServer(input);
        }
    }

    private void sendMoveToServer(DataOutputStream output, int humanMove)
            throws IOException {
        output.writeInt(humanMove);
        writeCells(output);
        output.writeUTF(messageSignature);
        output.writeUTF(nonce);
        output.writeUTF(createdTime);
        output.flush();
    }

    private boolean readResponseFromServer(DataInputStream input) throws IOException {
        GameStatus status = GameStatus.valueOf(input.readUTF());
        int[][] updatedCells = readCells(input);
        String message = input.readUTF();
        nonce = input.readUTF();
        createdTime = input.readUTF();
        messageSignature = input.readUTF();

        board.setCells(updatedCells);

        if (status == GameStatus.USER_WRONG_INPUT) {
            System.out.println(message);
            return true;
        }
        System.out.print(board.displayBoard());
        System.out.println(message);

        return status == GameStatus.GAME_OVER || status == GameStatus.QUIT_GAME;
    }

    private void writeCells(DataOutputStream output) throws IOException {
        for (int[] row : board.get2DCells()) {
            for (int cell : row) {
                output.writeInt(cell);
            }
        }
    }

    private int[][] readCells(DataInputStream input) throws IOException {
        int[][] cells = new int[Setting.NUMBER_ROWS][Setting.NUMBER_COLUMN];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = input.readInt();
            }
        }
        return cells;
    }

}
