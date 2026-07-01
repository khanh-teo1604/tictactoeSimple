package vgu.pe2026.ttt.basic.HTTP;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

public class TicTacToeHTTPServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(1604), 0);
        server.createContext("/game", new GameHandler());
        server.setExecutor(null);
        server.start();
    }
}
