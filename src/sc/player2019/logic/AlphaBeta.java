package sc.player2019.logic;

import java.util.ArrayList;

import sc.framework.plugins.Player;
import sc.player2019.Starter;
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
	private int depth = 4;
	private PlayerColor currentPlayer;
	private int countCalculatedMoves = 0;
	private BoardRater boardRaterAtStart;
	private String log;
	
	private boolean LOG = true;
	private boolean LOG_EVALUATION = true;
	private boolean LOG_TIME = true;

	public AlphaBeta(Starter client) {
		this.client = client;
	}

	private int alphaBeta(GameState gameState, int depth, int alpha, int beta) {

		countCalculatedMoves++;

		boolean foundPV = false;

		if (depth <= 0 || endOfGame(gameState)) {
			return evaluate(gameState, depth);
		}

		ArrayList<Move> moves = GameRuleLogic.getPossibleMoves(gameState);
		if (moves.size() == 0) {
			return evaluate(gameState, depth);
		}
		//moves = sort(moves, gameState);

		for (Move move : moves) {
			try {
				GameState clonedGameState = gameState.clone();
				move.perform(clonedGameState);
				int value;
				if (foundPV) {
					value = -alphaBeta(clonedGameState, depth - 1, -alpha - 1, -alpha);
					if (value > alpha && value < beta) {
						value = -alphaBeta(clonedGameState, depth - 1, -beta, -alpha);
					}
				} else {
					value = -alphaBeta(clonedGameState, depth - 1, -beta, -alpha);
				}
				if (value >= beta) {
					return beta;
				}
				if (value > alpha) {
					alpha = value;
					foundPV = true;
					if (depth == this.depth) {
						bestMove = new Move(move.x, move.y, move.direction);
					}
				}
			} catch (InvalidGameStateException | InvalidMoveException e) {
				System.out.println("Error!");
			}
		}

		return alpha;
	}

	private int evaluate(GameState gameState, int depth) {
		BoardRater boardRater = new BoardRater(gameState.getBoard());
		int value = boardRater.evaluate(boardRaterAtStart, currentPlayer);

		log += "Depth: " + (depth + 1) + "\n" + boardRater.toString(boardRaterAtStart) + "Evaluate: " + value + "\n\n";

		return value;
	}

	/*private ArrayList<Move> sort(ArrayList<Move> moves, GameState gameState) {
		ArrayList<Move> sortedMoves = new ArrayList<>();

		for (Move move : moves) {
			FieldState state = gameState.getField(move.x, move.y).getState();

			boolean alone = true;

			for (int x = -1; x <= 1; x++) {
				for (int y = -1; y <= 1; y++) {
					if (move.x + x >= 0 && move.y + y >= 0 && move.x + x < Constants.BOARD_SIZE && move.y + y < Constants.BOARD_SIZE) {
						if (gameState.getField(move.x + x, move.y + y).getState() == state) {
							alone = false;
							break;
						}
					}
				}
				if (!alone) {
					break;
				}
			}

			if (alone) {
				sortedMoves.add(0, move);
			} else {
				sortedMoves.add(move);
			}
		}

		return sortedMoves;
	}*/

	private boolean endOfGame(GameState gameState) {
		return gameState.getTurn() >= Constants.ROUND_LIMIT * 2 || GameRuleLogic.isSwarmConnected(gameState.getBoard(), gameState.getCurrentPlayerColor())  || GameRuleLogic.isSwarmConnected(gameState.getBoard(), gameState.getOtherPlayerColor());
	}

	@Override
	public void onRequestAction() {
		System.out.println("\nStarting calculation.");
		
		log = "";
		
		long startMillis = 0;

		if (LOG && LOG_TIME) {
			startMillis = System.currentTimeMillis();
		}

		boardRaterAtStart = new BoardRater(gameState.getBoard());

		bestMove = GameRuleLogic.getPossibleMoves(gameState).get(0);

		alphaBeta(gameState, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);

		sendAction(bestMove);

		if (LOG && LOG_EVALUATION) {
			System.out.println(log);
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
	public void gameEnded(GameResult gameResult, PlayerColor playerColor, String errorMessage) { }

}
