package vgu.pe2026.ttt.basic.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import vgu.pe2026.ttt.basic.Board;
import vgu.pe2026.ttt.basic.Board1D;
import vgu.pe2026.ttt.basic.Constant.GameStatus;
import vgu.pe2026.ttt.basic.Constant.PlayerType;
import vgu.pe2026.ttt.basic.Constant.Setting;
import vgu.pe2026.ttt.basic.ComputerPlayerFactory;
import vgu.pe2026.ttt.basic.Game;
import vgu.pe2026.ttt.basic.GameMoveResult;

public class TicTacToeServer {
    private static final int DEFAULT_PORT = 1604;
    private static final int NO_MOVE = -1;

    public static void main(String[] args) throws IOException {
        new TicTacToeServer().start(DEFAULT_PORT);
    }

    public void start(int port) throws IOException {
        new TicTacToeServerMultiUserSingleThread().start(port);
    }

    public static void handleRequest(Socket socket) {
        try (socket) {
            DataInputStream input = new DataInputStream(socket.getInputStream());
            DataOutputStream output = new DataOutputStream(socket.getOutputStream());

            int clientMove = input.readInt();
            int[][] cells = readCells(input);
            String messageSignature = input.readUTF();
            String nonce = input.readUTF();
            String createdTime = input.readUTF();

            Board board = new Board1D();
            board.setCells(cells);
            Game gameEngine = new Game(board);

            if (!board.isValidBoard(messageSignature, nonce, createdTime, clientMove)) {
                writeInvalidResponse(output, "Invalid board");
                return;
            }

            if (!isInitialRequest(clientMove, board)
                    && !NonceChallenge.accept(nonce, createdTime, System.currentTimeMillis())) {
                writeInvalidResponse(output, "Invalid nonce");
                return;
            }

            GameMoveResult currentBoardResult = gameEngine
                    .getCurrentBoardStatus(PlayerType.HUMAN.getplayerTypeSymbol());
            if (currentBoardResult.getStatus() == GameStatus.GAME_OVER) {
                writeResponse(output, currentBoardResult.getStatus(), board,
                        currentBoardResult.getMessage());
                return;
            }

            if (clientMove != NO_MOVE) {
                GameMoveResult clientResult = gameEngine.playMove(clientMove, PlayerType.HUMAN.getplayerTypeSymbol());
                if (clientResult.getStatus() != GameStatus.GAME_RUNNING) {
                    writeResponse(output, clientResult.getStatus(), board,
                            clientResult.getMessage());
                    return;
                }
            } else {
                writeResponse(output, GameStatus.GAME_RUNNING, board,
                        "Player#" + PlayerType.HUMAN.getplayerTypeSymbol() + "'s turn");
                return;
            }

            int computerMove = new ComputerPlayerFactory().createPlayer().makeMove(board);
            GameMoveResult result = gameEngine.playMove(computerMove, PlayerType.COMPUTER.getplayerTypeSymbol());

            if (result.getStatus() == GameStatus.GAME_OVER) {
                writeResponse(output, GameStatus.GAME_OVER, board, result.getMessage());
                return;
            }

            writeResponse(output, GameStatus.GAME_RUNNING, board,
                    "Player#" + PlayerType.HUMAN.getplayerTypeSymbol() + "'s turn");
        } catch (IOException e) {
            System.out.println("Request error: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Request ended unexpectedly: " + e.getMessage());
        }
    }

    public static void writeResponse(DataOutputStream output, GameStatus status, Board board, String message)
            throws IOException {
        NonceChallenge nonceChallenge = NonceChallenge.create(System.currentTimeMillis());
        output.writeUTF(status.name());
        writeCells(output, board);
        output.writeUTF(message);
        output.writeUTF(nonceChallenge.nonce);
        output.writeUTF(nonceChallenge.createdTime);
        output.writeUTF(MessageSignature.sign(board, nonceChallenge.nonce, nonceChallenge.createdTime));
        output.flush();
    }

    private static void writeInvalidResponse(DataOutputStream output, String message) throws IOException {
        writeResponse(output, GameStatus.USER_WRONG_INPUT, new Board1D(), message);
    }

    private static int[][] readCells(DataInputStream input) throws IOException {
        int[][] cells = new int[Setting.NUMBER_ROWS][Setting.NUMBER_COLUMN];
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j] = input.readInt();
            }
        }
        return cells;
    }

    private static void writeCells(DataOutputStream output, Board board) throws IOException {
        for (int[] row : board.get2DCells()) {
            for (int cell : row) {
                output.writeInt(cell);
            }
        }
    }

    private static boolean isInitialRequest(int clientMove, Board board) {
        return clientMove == NO_MOVE && board.isEmpty();
    }

}
