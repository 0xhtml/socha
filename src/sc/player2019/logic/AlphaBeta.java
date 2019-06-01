package sc.player2019.logic;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import sc.framework.plugins.Player;
import sc.player2019.Starter;
import sc.plugin2019.Direction;
import sc.plugin2019.GameState;
import sc.plugin2019.IGameHandler;
import sc.plugin2019.Move;
import sc.plugin2019.util.Constants;
import sc.shared.GameResult;
import sc.shared.InvalidGameStateException;
import sc.shared.InvalidMoveException;
import sc.shared.PlayerColor;

public class AlphaBeta implements IGameHandler {

    private GameState gameState;
    private Move bestMove;
    private double best;
    private Starter client;
    private int depth = 3;
    private PlayerColor currentPlayer;

    private long time;

    public AlphaBeta(Starter client) {
        this.client = client;
        System.out.println("v1.7");
    }

    private double alphaBeta(GameState gameState, int depth, double alpha, double beta) {
        boolean foundPV = false;
        double best = Double.NEGATIVE_INFINITY;

        if (depth <= 0 || endOfGame(gameState)) {
            return evaluate(gameState, depth);
        }

        ArrayList<Move> moves = PerformanceGameRuleLogic.getPossibleMoves(gameState);
        if (moves.size() == 0) {
            return evaluate(gameState, depth);
        }
        moves = sortMoves(moves);

        for (Move move : moves) {
            try {
                GameState clonedGameState = gameState.clone();
                move.perform(clonedGameState);

                double value;
                if (foundPV) {
                    value = -alphaBeta(clonedGameState, depth - 1, -alpha - 1, -alpha);
                    if (value > alpha && value < beta) {
                        value = -alphaBeta(clonedGameState, depth - 1, -beta, -value);
                    }
                } else {
                    value = -alphaBeta(clonedGameState, depth - 1, -beta, -alpha);
                }

                if (value > best) {
                    if (value >= beta) {
                        return value;
                    }
                    best = value;
                    if (depth == this.depth) {
                        bestMove = new Move(move.x, move.y, move.direction);
                    }
                    if (value > alpha) {
                        alpha = value;
                        foundPV = true;
                    }
                }
            } catch (InvalidGameStateException | InvalidMoveException e) {
                System.out.println("Error!");
                e.printStackTrace();
            }
        }
        return best;
    }

    private double evaluate(GameState gameState, int depth) {
        BoardRater boardRater = new BoardRater(gameState.getBoard(), currentPlayer, gameState.getTurn());
        double value = boardRater.evaluate();
        if ((this.depth - depth) % 2 != 0) {
            value = -value;
        }
        return value;
    }

    private boolean endOfGame(GameState gameState) {
        if (gameState.getTurn() >= Constants.ROUND_LIMIT * 2) {
            return true;
        }
        if (gameState.getTurn() % 2 == 0) {
            if (PerformanceGameRuleLogic.isSwarmConnected(gameState.getBoard(), PlayerColor.RED) || PerformanceGameRuleLogic.isSwarmConnected(gameState.getBoard(), PlayerColor.BLUE)) {
                return true;
            }
        }
        return false;
    }

    private ArrayList<Move> sortMoves(ArrayList<Move> moves) {
        ArrayList<Move> sortedMoves = new ArrayList<>();
        for (Move move : moves) {
            if (move.x < Constants.BOARD_SIZE / 2 - 1) {
                if (move.y < Constants.BOARD_SIZE / 2 - 1) {
                    // Unten Links
                    if (move.direction == Direction.UP_RIGHT) {
                        sortedMoves.add(0, move);
                        continue;
                    }
                } else {
                    // Oben Links
                    if (move.direction == Direction.DOWN_RIGHT) {
                        sortedMoves.add(0, move);
                        continue;
                    }
                }
            } else {
                if (move.y < Constants.BOARD_SIZE / 2 - 1) {
                    // Unten Rechts
                    if (move.direction == Direction.UP_LEFT) {
                        sortedMoves.add(0, move);
                        continue;
                    }
                } else {
                    // Oben Rechts
                    if (move.direction == Direction.DOWN_LEFT) {
                        sortedMoves.add(0, move);
                        continue;
                    }
                }
            }
            sortedMoves.add(move);
        }
        return sortedMoves;
    }

    @Override
    public void onRequestAction() {
        time = System.currentTimeMillis();
        System.out.println("\nStarting calculation for " + currentPlayer);

        ArrayList<Move> possibelMoves = PerformanceGameRuleLogic.getPossibleMoves(gameState);
        bestMove = possibelMoves.get(0);

        if (gameState.getTurn() == 0 && possibelMoves.contains(new Move(9, 2, Direction.DOWN_LEFT))) {
            bestMove = new Move(9, 2, Direction.DOWN_LEFT);
        } else {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<?> handler = executor.submit(() -> {
                best = alphaBeta(gameState, depth, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
            });

            try {
                handler.get(1850, TimeUnit.MILLISECONDS);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                handler.cancel(true);
                System.out.println("Timeout!");
            }

            executor.shutdownNow();
        }

        sendAction(bestMove);
        System.out.println("Evaluation: " + best);
    }

    @Override
    public void onUpdate(GameState gameState) {
        this.gameState = gameState;
        currentPlayer = gameState.getCurrentPlayerColor();
    }

    @Override
    public void onUpdate(Player currentPlayer, Player otherPlayer) {
        this.currentPlayer = currentPlayer.getColor();
    }

    @Override
    public void sendAction(Move move) {
        client.sendMove(move);
        System.out.println("Send after " + (System.currentTimeMillis() - time) + "ms");
    }

    @Override
    public void gameEnded(GameResult gameResult, PlayerColor playerColor, String errorMessage) {
        System.out.println("\nGame ended.");
        System.out.println(gameResult.toString());
        System.out.println(errorMessage);
    }

}
