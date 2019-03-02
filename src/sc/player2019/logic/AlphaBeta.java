package sc.player2019.logic;

import java.util.ArrayList;

import sc.framework.plugins.Player;
import sc.player2019.Starter;
import sc.plugin2019.Direction;
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
	private int depth = 3;
	private PlayerColor currentPlayer;
	private BoardRater boardRaterAtStart;
	
	private boolean timeout;
	private long time;

	public AlphaBeta(Starter client) {
		this.client = client;
	}

	private int alphaBeta(GameState gameState, int depth, int alpha, int beta) {
		if (System.currentTimeMillis() - time >= 1890) {
			System.out.println("TimeoutTime: " + (System.currentTimeMillis()- time) + "ms");
			timeout = true;
		}
		if (timeout) {
			return 0;
		}
		
		boolean foundPV = false;
		int best = Integer.MIN_VALUE;
		
		if (depth <= 0 || endOfGame(gameState)) {
			return evaluate(gameState, depth);
		}

		ArrayList<Move> moves = GameRuleLogic.getPossibleMoves(gameState);

		if (moves.size() == 0) {
			return evaluate(gameState, depth);
		}
		
		moves = sortMoves(moves);
		
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
			if (timeout) {
				return 0;
			}
		}
		return best;
	}

	private int evaluate(GameState gameState, int depth) {
		BoardRater boardRater = new BoardRater(gameState.getBoard());
		int value = boardRater.evaluate(boardRaterAtStart, currentPlayer);
		if (this.depth % 2 == 0) {
			return value;
		} else {
			return -value;
		}
	}

	private boolean endOfGame(GameState gameState) {
		return gameState.getRound() >= Constants.ROUND_LIMIT || GameRuleLogic.isSwarmConnected(gameState.getBoard(), gameState.getCurrentPlayerColor()) || GameRuleLogic.isSwarmConnected(gameState.getBoard(), gameState.getOtherPlayerColor());
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
		System.out.println("\nStarting calculation.");
		time = System.currentTimeMillis();
		timeout = false;
		
		boardRaterAtStart = new BoardRater(gameState.getBoard());
		bestMove = GameRuleLogic.getPossibleMoves(gameState).get(0);
		int best = alphaBeta(gameState, depth, Integer.MIN_VALUE, Integer.MAX_VALUE);
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
		System.out.println("SendActionTime: " + (System.currentTimeMillis() - time) + "ms");
	}

	@Override
	public void gameEnded(GameResult gameResult, PlayerColor playerColor, String errorMessage) {
		System.out.println("\nGame ended.");
		System.out.println(gameResult.toString());
	}

}
