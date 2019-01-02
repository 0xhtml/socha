package sc.player2019.logic;

import java.util.ArrayList;

import sc.framework.plugins.Player;
import sc.player2019.Starter;
import sc.plugin2019.Board;
import sc.plugin2019.GameState;
import sc.plugin2019.IGameHandler;
import sc.plugin2019.Move;
import sc.plugin2019.util.Constants;
import sc.plugin2019.util.GameRuleLogic;
import sc.shared.GameResult;
import sc.shared.InvalidGameStateException;
import sc.shared.InvalidMoveException;
import sc.shared.PlayerColor;

public class AlphaBeta implements IGameHandler {

	private GameState gameState;
	private Move bestMove;
	private Starter client;
	private int depth = 2;
	private PlayerColor currentPlayer;
	private int countCalculatedMoves = 0;
	private BoardRater boardRaterAtStart;
	private String logEvaluationAll;
	private String logEvaluationBest;
	private String logEvaluationTmp;

	private final boolean LOG = true;
	private final boolean LOG_EVALUATION_ALL = true;
	private final boolean LOG_EVALUATION_BEST = true;
	private final boolean LOG_BOARD = true;
	private final boolean LOG_DEPTH = true;
	private final boolean LOG_TIME = true;

	public AlphaBeta(Starter client) {
		this.client = client;
	}

	private int alphaBeta(GameState gameState, int depth, int alpha, int beta) {

		countCalculatedMoves++;

		boolean foundPV = false;
		int best = Integer.MIN_VALUE + 1;

		if (depth <= 0 || endOfGame(gameState)) {
			return evaluate(gameState, depth);
		}

		ArrayList<Move> moves = GameRuleLogic.getPossibleMoves(gameState);
		if (moves.size() == 0) {
			return evaluate(gameState, depth);
		}
		// moves = sort(moves, gameState);

		for (Move move : moves) {
			try {
				GameState clonedGameState = gameState.clone();
				move.perform(clonedGameState);
				int value;
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
						if (LOG_EVALUATION_BEST) {
							logEvaluationBest = logEvaluationTmp;
						}
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

	private int evaluate(GameState gameState, int depth) {
		BoardRater boardRater = new BoardRater(gameState.getBoard());
		int value = boardRater.evaluate(boardRaterAtStart, currentPlayer);

		logEvaluationTmp = "\n\n\n";
		if (LOG_BOARD) {
			logEvaluationTmp += boardToString(gameState.getBoard());
		}
		if (LOG_DEPTH) {
			logEvaluationTmp += "Depth: " + (depth + 1) + "\n";
			logEvaluationTmp += "Player: " + currentPlayer.toString() + "\n";
		}
		logEvaluationTmp += boardRater.toString(boardRaterAtStart);
		logEvaluationTmp += "Evaluate: " + value + "\n";
		if (LOG_EVALUATION_ALL) {
			logEvaluationAll += logEvaluationTmp;
		}

		return value;
	}

	private boolean endOfGame(GameState gameState) {
		return gameState.getTurn() >= Constants.ROUND_LIMIT * 2 || GameRuleLogic.isSwarmConnected(gameState.getBoard(), gameState.getCurrentPlayerColor()) || GameRuleLogic.isSwarmConnected(gameState.getBoard(), gameState.getOtherPlayerColor());
	}

	@Override
	public void onRequestAction() {
		System.out.println("\nStarting calculation.");
		logEvaluationAll = "";
		long startMillis = 0;
		if (LOG && LOG_TIME) {
			startMillis = System.currentTimeMillis();
		}

		boardRaterAtStart = new BoardRater(gameState.getBoard());
		bestMove = GameRuleLogic.getPossibleMoves(gameState).get(0);
		alphaBeta(gameState, depth, Integer.MIN_VALUE + 1, Integer.MAX_VALUE - 1);
		sendAction(bestMove);

		if (LOG_EVALUATION_ALL) {
			System.out.println(logEvaluationAll);
		}
		if (LOG_EVALUATION_ALL && LOG_EVALUATION_BEST) {
			System.out.println("\n\n=== BEST ===");
		}
		if (LOG_EVALUATION_BEST) {
			System.out.println(logEvaluationBest);
		}
		if (LOG && LOG_TIME) {
			long endMillis = System.currentTimeMillis();
			long calculationTime = endMillis - startMillis;
			System.out.println("countCalculatedMoves: " + countCalculatedMoves);
			System.out.println("calculationTime: " + calculationTime + "ms");
		}
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
	}

	@Override
	public void gameEnded(GameResult gameResult, PlayerColor playerColor, String errorMessage) {
	}

	public String boardToString(Board board) {
		String str = "";
		str += "┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐\n";
		str += "│ & │ & │ & │ & │ & │ & │ & │ & │ & │ & │\n";
		str += "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤\n";
		str += "│ & │ & │ & │ & │ & │ & │ & │ & │ & │ & │\n";
		str += "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤\n";
		str += "│ & │ & │ & │ & │ & │ & │ & │ & │ & │ & │\n";
		str += "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤\n";
		str += "│ & │ & │ & │ & │ & │ & │ & │ & │ & │ & │\n";
		str += "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤\n";
		str += "│ & │ & │ & │ & │ & │ & │ & │ & │ & │ & │\n";
		str += "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤\n";
		str += "│ & │ & │ & │ & │ & │ & │ & │ & │ & │ & │\n";
		str += "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤\n";
		str += "│ & │ & │ & │ & │ & │ & │ & │ & │ & │ & │\n";
		str += "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤\n";
		str += "│ & │ & │ & │ & │ & │ & │ & │ & │ & │ & │\n";
		str += "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤\n";
		str += "│ & │ & │ & │ & │ & │ & │ & │ & │ & │ & │\n";
		str += "├───┼───┼───┼───┼───┼───┼───┼───┼───┼───┤\n";
		str += "│ & │ & │ & │ & │ & │ & │ & │ & │ & │ & │\n";
		str += "└───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘\n";
		for (int i = Constants.BOARD_SIZE - 1; i >= 0; i--) {
			for (int j = 0; j < Constants.BOARD_SIZE; j++) {
				switch (board.getField(j, i).getState()) {
				case BLUE:
					str = str.replaceFirst("&", "B");
					break;
				case RED:
					str = str.replaceFirst("&", "R");
					break;
				case OBSTRUCTED:
					str = str.replaceFirst("&", "X");
					break;
				default:
					str = str.replaceFirst("&", " ");
					break;
				}
			}
		}
		return str;
	}

}
