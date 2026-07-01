package vgu.pe2026.ttt.basic.HTTP;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import vgu.pe2026.ttt.basic.Board;
import vgu.pe2026.ttt.basic.Board1D;
import vgu.pe2026.ttt.basic.ComputerPlayerFactory;
import vgu.pe2026.ttt.basic.Game;
import vgu.pe2026.ttt.basic.GameMoveResult;
import vgu.pe2026.ttt.basic.server.MessageSignature;
import vgu.pe2026.ttt.basic.server.NonceChallenge;
import vgu.pe2026.ttt.basic.Constant.GameStatus;
import vgu.pe2026.ttt.basic.Constant.PlayerType;
import vgu.pe2026.ttt.basic.Constant.Setting;

public class GameHandler implements HttpHandler {
    private static final int NO_MOVE = -1;

    private final GameService gameService = new GameService();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        // TODO Auto-generated method stub
        try {

            exchange.getResponseHeaders().add(
                    "Access-Control-Allow-Origin", "*");

            exchange.getResponseHeaders().add(
                    "Access-Control-Allow-Headers", "*");

            exchange.getResponseHeaders().add(
                    "Access-Control-Allow-Methods",
                    "POST, GET, OPTIONS");

            // Only for preflight requests
            if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            String body = new String(exchange.getRequestBody().readAllBytes());

            ServiceResult result = gameService.processRequest(body);

            sendResponse(
                    exchange,
                    result.getStatusCode(),
                    result.getResponseBody());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendResponse(
            HttpExchange exchange,
            int statusCode,
            String response) throws IOException {

        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set(
                "Content-Type",
                "text/plain; charset=UTF-8");

        exchange.sendResponseHeaders(
                statusCode,
                bytes.length);

        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    public static void writeResponse(DataOutputStream output, GameStatus status, Board board, String message)
            throws IOException {
        output.writeUTF(status.name());
        writeCells(output, board);
        output.writeUTF(message);
        output.flush();
    }

    public static String buildResponse(
            GameStatus status,
            Board board,
            String message) throws Exception {

        NonceChallenge nonceChallenge = NonceChallenge.create(System.currentTimeMillis());

        GameResponse response = new GameResponse(
                status,
                board.get1DCells(),
                message,
                nonceChallenge.nonce,
                nonceChallenge.createdTime,
                MessageSignature.sign(
                        board,
                        nonceChallenge.nonce,
                        nonceChallenge.createdTime));

        ObjectMapper mapper = new ObjectMapper();

        return mapper.writeValueAsString(response);
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
