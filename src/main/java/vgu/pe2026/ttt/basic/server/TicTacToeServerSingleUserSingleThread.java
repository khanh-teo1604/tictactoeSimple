package vgu.pe2026.ttt.basic.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TicTacToeServerSingleUserSingleThread {
    private static final int DEFAULT_PORT = 1604;

    public static void main(String[] args) throws IOException {
        new TicTacToeServerSingleUserSingleThread().start(DEFAULT_PORT);
    }

    public void start(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Tic-Tac-Toe server started on port " + port);
            System.out.println("Waiting for one player...");

            try (Socket socket = serverSocket.accept()) {
                System.out.println("Human player connected");
                TicTacToeServer.handleRequest(socket);
            }
        }
    }
}
