package vgu.pe2026.ttt.basic.HTTP;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import com.fasterxml.jackson.databind.ObjectMapper;

import vgu.pe2026.ttt.basic.Board;
import vgu.pe2026.ttt.basic.Board1D;
import vgu.pe2026.ttt.basic.HumanPlayerFactory;
import vgu.pe2026.ttt.basic.Player;
import vgu.pe2026.ttt.basic.Constant.GameStatus;
import vgu.pe2026.ttt.basic.Constant.Setting;

public class TicTacToeHTTPClient {

    private static final String DEFAULT_URL = "http://localhost:1604/game";
    private static final int NO_MOVE = -1;
    private static final int NEW_GAME = -2;

    private String messageSignature = "";
    private String nonce = "";
    private String createdTime = "";

    private final HttpClient client = HttpClient.newHttpClient();
    private Board board = new Board1D();

    public static void main(String[] args) throws Exception {
        new TicTacToeHTTPClient().start(DEFAULT_URL);
    }

    public void start(String url) throws Exception {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Hello!");

            Player human = new HumanPlayerFactory(scanner).createPlayer();

            if (playTurnWithServer(url, NO_MOVE)) {
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
                }

                if (playTurnWithServer(url, move)) {
                    return;
                }
            }
        }
    }

    private boolean playTurnWithServer(String url, int humanMove)
            throws Exception {

        String requestBody = buildRequestBody(humanMove);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/plain")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<byte[]> response = client.send(
                request,
                HttpResponse.BodyHandlers.ofByteArray());

        if (response.statusCode() != 200) {
            System.out.println("Wrong board or invalid nonce");
            return true;
        }

        String responseText = new String(response.body(), StandardCharsets.UTF_8);

        return readResponseFromServer(responseText);
    }

    private String buildRequestBody(int humanMove) throws Exception {
        GameRequest request = new GameRequest(
                humanMove,
                board.get1DCells(),
                messageSignature,
                nonce,
                createdTime);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(request);
    }

    private boolean readResponseFromServer(String responseBody) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        GameResponse response = mapper.readValue(responseBody, GameResponse.class);

        board.setCells(response.getBoard());

        nonce = response.getNonce();
        createdTime = response.getCreatedTime();
        messageSignature = response.getMessageSignature();

        if (response.getStatus() == GameStatus.USER_WRONG_INPUT) {
            System.out.println(response.getMessage());
            return true;
        }

        System.out.print(board.displayBoard());
        System.out.println(response.getMessage());

        return response.getStatus() == GameStatus.GAME_OVER
                || response.getStatus() == GameStatus.QUIT_GAME;
    }

    private int[][] readCells(String boardText) {
        int[][] cells = new int[Setting.NUMBER_ROWS][Setting.NUMBER_COLUMN];

        String numbersOnly = boardText
                .replace("[", "")
                .replace("]", "")
                .replace(" ", "");

        String[] parts = numbersOnly.split(",");

        int k = 0;
        for (int i = 0; i < Setting.NUMBER_ROWS; i++) {
            for (int j = 0; j < Setting.NUMBER_COLUMN; j++) {
                cells[i][j] = Integer.parseInt(parts[k++]);
            }
        }

        return cells;
    }
}
