package vgu.pe2026.ttt.basic.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import vgu.pe2026.ttt.basic.Board1D;
import vgu.pe2026.ttt.basic.Constant.GameStatus;

public class TicTacToeServerMultiUserMultiThreadWithPool {
    private static final int DEFAULT_PORT = 1604;
    private static final int MAX_REQUESTS = 4;

    public static void main(String[] args) throws IOException {
        new TicTacToeServerMultiUserMultiThreadWithPool().start(DEFAULT_PORT);
    }

    public void start(int port) throws IOException {
        ThreadPoolExecutor requestPool = new ThreadPoolExecutor(
                MAX_REQUESTS,
                MAX_REQUESTS,
                0L,
                TimeUnit.MILLISECONDS,
                new SynchronousQueue<>());

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Tic-Tac-Toe server started on port " + port);
            System.out.println("Waiting for players...");
            System.out.println("Maximum simultaneous requests: " + MAX_REQUESTS);

            while (true) {
                Socket socket = serverSocket.accept();
                try {
                    System.out.println("Human player connected");
                    requestPool.execute(() -> TicTacToeServer.handleRequest(socket));
                } catch (RejectedExecutionException e) {
                    rejectRequest(socket);
                }
            }
        } finally {
            requestPool.shutdownNow();
            requestPool.close();
        }
    }

    private void rejectRequest(Socket socket) {
        try (socket;
                DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {
            TicTacToeServer.writeResponse(output, GameStatus.GAME_OVER, new Board1D(),
                    "Server is busy. Only 4 requests can be handled at the same time.");
            System.out.println("Rejected request because 4 requests are already running");
        } catch (IOException e) {
            System.out.println("Could not reject request cleanly: " + e.getMessage());
        }
    }
}
