package sc.player2019.logic;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sc.framework.plugins.Player;
import sc.player2019.Starter;
import sc.plugin2019.FieldState;
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
	
	private static final Logger log = LoggerFactory.getLogger(AlphaBeta.class);
	
	public AlphaBeta(Starter client) {
		this.client = client;
		System.out.println("");
		System.out.println("                                                   ,/");
		System.out.println("                                                  //");
		System.out.println("                                                ,//");
		System.out.println("                                    ___   /|   |//");
		System.out.println("                                `__/\\_ --(/|___/-/");
		System.out.println("                             \\|\\_-\\___ __-_`- /-/ \\.");
		System.out.println("                            |\\_-___,-\\_____--/_)' ) \\");
		System.out.println("                             \\ -_ /     __ \\( `( __`\\|");
		System.out.println("                             `\\__|      |\\)\\ ) /(/|");
		System.out.println("     ,._____.,            ',--//-|      \\  |  '   /");
		System.out.println("    /     __. \\,          / /,---|       \\       /");
		System.out.println("   / /    _. \\  \\        `/`_/ _,'        |     |");
		System.out.println("  |  | ( (  \\   |      ,/\\'__/'/          |     |");
		System.out.println("  |  \\  \\`--, `_/_------______/           \\(   )/");
		System.out.println("  | | \\  \\_. \\,                            \\___/\\");
		System.out.println("  | |  \\_   \\  \\                                 \\");
		System.out.println("  \\ \\    \\_ \\   \\   /                             \\");
		System.out.println("   \\ \\  \\._  \\__ \\_|       |                       \\");
		System.out.println("    \\ \\___  \\      \\       |                        \\");
		System.out.println("     \\__ \\__ \\  \\_ |       \\                         |");
		System.out.println("     |  \\_____ \\  ____      |                        |");
		System.out.println("     | \\  \\__ ---' .__\\     |        |               |");
		System.out.println("     \\  \\__ ---   /   )     |        \\              /");
		System.out.println("      \\   \\____/ / ()(      \\          `---_       /|");
		System.out.println("       \\__________/(,--__    \\_________.    |    ./ |");
		System.out.println("         |     \\ \\  `---_\\--,           \\   \\_,./   |");
		System.out.println("         |      \\  \\_ ` \\    /`---_______-\\   \\\\    /");
		System.out.println("          \\      \\.___,`|   /              \\   \\\\   \\");
		System.out.println("           \\     |  \\_ \\|   \\              (   |:    |");
		System.out.println("            \\    \\      \\    |             /  / |    ;");
		System.out.println("             \\    \\      \\    \\          ( `_'   \\  |");
		System.out.println("              \\.   \\      \\.   \\          `__/   |  |");
		System.out.println("                \\   \\       \\.  \\                |  |");
		System.out.println("                 \\   \\        \\  \\               (  )");
		System.out.println("                  \\   |        \\  |              |  |");
		System.out.println("                   |  \\         \\ \\              I  `");
		System.out.println("                   ( __;        ( _;            ('-_';");
		System.out.println("                   |___\\        \\___:            \\___:");
		System.out.println("                            Finn Steffens");
	}

	private float alphaBeta(GameState gameState, int depth, float alpha, float beta) {
		
		boolean foundPV = false;
		
		if (depth <= 0 || endOfGame(gameState)) {
			return evaluate(gameState);
		}
		
		ArrayList<Move> moves = GameRuleLogic.getPossibleMoves(gameState);
		if (moves.size() == 0) {
			return evaluate(gameState);
		}
		moves = sort(moves, gameState);
		
		for (Move move : moves) {
			try {
				GameState clonedGameState = gameState.clone();
				move.perform(clonedGameState);
				float value;
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
						log.info("Found new best move.");
						bestMove = new Move(move.x, move.y, move.direction);
					}
				}
			} catch (InvalidGameStateException | InvalidMoveException e) {
				log.info("Error!");
			}
		}
		
		return alpha;
	}
	
	private float evaluate(GameState gameState) {
		float currentPlayerGreatestSwarmSize = GameRuleLogic.greatestSwarmSize(gameState.getBoard(), currentPlayer);
		float otherPlayerGreatestSwarmSize   = GameRuleLogic.greatestSwarmSize(gameState.getBoard(), currentPlayer.opponent());
		
		float currentPlayerFieldsCount = GameRuleLogic.getOwnFields(gameState.getBoard(), currentPlayer).size();
		float otherPlayerFieldsCount   = GameRuleLogic.getOwnFields(gameState.getBoard(), currentPlayer.opponent()).size();
		
		return (currentPlayerGreatestSwarmSize / currentPlayerFieldsCount) - (otherPlayerGreatestSwarmSize / otherPlayerFieldsCount);
	}
	
	private ArrayList<Move> sort(ArrayList<Move> moves, GameState gameState) {
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
	}
	
	private boolean endOfGame(GameState gameState) {
		return gameState.getTurn() >= Constants.ROUND_LIMIT * 2 || GameRuleLogic.isSwarmConnected(gameState.getBoard(), gameState.getCurrentPlayerColor())  || GameRuleLogic.isSwarmConnected(gameState.getBoard(), gameState.getOtherPlayerColor());
	}
	
	@Override
	public void onRequestAction() {
		bestMove = GameRuleLogic.getPossibleMoves(gameState).get(0);
		
		log.info("");
		log.info("Starting calculation.");
		long startMillis = System.currentTimeMillis();
		
		alphaBeta(gameState, depth, -Float.MAX_VALUE, Float.MAX_VALUE);

		long endMillis = System.currentTimeMillis();
		long millis = endMillis - startMillis;
		log.info("Calculation took " + millis + "ms.");
		
		sendAction(bestMove);
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
