package vgu.pe2026.ttt.basic.HTTP;

import com.fasterxml.jackson.databind.ObjectMapper;

import vgu.pe2026.ttt.basic.Board;
import vgu.pe2026.ttt.basic.Board1D;
import vgu.pe2026.ttt.basic.ComputerPlayerFactory;
import vgu.pe2026.ttt.basic.Game;
import vgu.pe2026.ttt.basic.GameMoveResult;
import vgu.pe2026.ttt.basic.Constant.GameStatus;
import vgu.pe2026.ttt.basic.Constant.PlayerType;
import vgu.pe2026.ttt.basic.server.NonceChallenge;

public class GameService {

    private static final int NO_MOVE = -1;

    private final ObjectMapper mapper = new ObjectMapper();

    public ServiceResult processRequest(String body) throws Exception {

        GameRequest request = mapper.readValue(body, GameRequest.class);

        int clientMove = request.getClientMove();
        int[] boardData = request.getBoard();

        String messageSignature = request.getMessageSignature();
        String nonce = request.getNonce();
        String createdTime = request.getCreatedTime();

        Board board = new Board1D();
        board.setCells(boardData);

        Game gameEngine = new Game(board);

        // Validate board signature
        if (!board.isValidBoard(
                messageSignature,
                nonce,
                createdTime,
                clientMove)) {

            return new ServiceResult(
                    400,
                    GameHandler.buildResponse(
                            GameStatus.USER_WRONG_INPUT,
                            new Board1D(),
                            "Invalid board"));
        }

        // Validate nonce
        if (!isInitialRequest(clientMove, board)
                && !NonceChallenge.accept(
                        nonce,
                        createdTime,
                        System.currentTimeMillis())) {

            return new ServiceResult(
                    400,
                    GameHandler.buildResponse(
                            GameStatus.USER_WRONG_INPUT,
                            new Board1D(),
                            "Invalid nonce"));
        }

        // Current board status
        GameMoveResult currentBoardResult = gameEngine.getCurrentBoardStatus(
                PlayerType.HUMAN.getplayerTypeSymbol());

        if (currentBoardResult.getStatus() == GameStatus.GAME_OVER) {

            return new ServiceResult(
                    200,
                    GameHandler.buildResponse(
                            currentBoardResult.getStatus(),
                            board,
                            currentBoardResult.getMessage()));
        }

        // Initial request
        if (clientMove == NO_MOVE) {

            return new ServiceResult(
                    200,
                    GameHandler.buildResponse(
                            GameStatus.GAME_RUNNING,
                            board,
                            "Player#"
                                    + PlayerType.HUMAN.getplayerTypeSymbol()
                                    + "'s turn"));
        }

        // Human move
        GameMoveResult clientResult = gameEngine.playMove(
                clientMove,
                PlayerType.HUMAN.getplayerTypeSymbol());

        if (clientResult.getStatus() != GameStatus.GAME_RUNNING) {

            return new ServiceResult(
                    200,
                    GameHandler.buildResponse(
                            clientResult.getStatus(),
                            board,
                            clientResult.getMessage()));
        }

        // Computer move
        int computerMove = new ComputerPlayerFactory()
                .createPlayer()
                .makeMove(board);

        GameMoveResult computerResult = gameEngine.playMove(
                computerMove,
                PlayerType.COMPUTER.getplayerTypeSymbol());

        return new ServiceResult(
                200,
                GameHandler.buildResponse(
                        computerResult.getStatus(),
                        board,
                        computerResult.getStatus() == GameStatus.GAME_OVER
                                ? computerResult.getMessage()
                                : "Player#"
                                        + PlayerType.HUMAN.getplayerTypeSymbol()
                                        + "'s turn"));
    }

    private boolean isInitialRequest(
            int clientMove,
            Board board) {

        return clientMove == NO_MOVE
                && board.isEmpty();
    }
}