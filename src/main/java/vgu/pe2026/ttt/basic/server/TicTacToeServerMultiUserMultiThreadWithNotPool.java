package vgu.pe2026.ttt.basic.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TicTacToeServerMultiUserMultiThreadWithNotPool {
    private static final int DEFAULT_PORT = 1604;

    public static void main(String[] args) throws IOException {
        new TicTacToeServerMultiUserMultiThreadWithNotPool().start(DEFAULT_PORT);
    }

    public void start(int port) throws IOException {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Tic-Tac-Toe server started on port " + port);
            System.out.println("Waiting for players...");
            System.out.println("Thread mode: one new Thread per request, no thread pool");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Human player connected");

                Thread requestThread = new Thread(() -> TicTacToeServer.handleRequest(socket));
                requestThread.start();
            }
        }
    }
}
