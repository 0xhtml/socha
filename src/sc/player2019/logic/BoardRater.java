package sc.player2019.logic;

import java.util.HashSet;
import java.util.Set;

import sc.plugin2019.Board;
import sc.plugin2019.Field;
import sc.plugin2019.FieldState;
import sc.plugin2019.util.Constants;
import sc.plugin2019.util.GameRuleLogic;
import sc.shared.PlayerColor;

public class BoardRater {

	private int turn;

	private int ownPiranhasCount = 0;
	private int oppPiranhasCount = 0;

	private int ownPiranhasPosition = 0;
	private int oppPiranhasPosition = 0;

	private int ownPiranhasAtBorder = 0;
	private int oppPiranhasAtBorder = 0;

	private int ownGreatestSwarmSize = 0;
	private int oppGreatestSwarmSize = 0;

	private int ownNotInGreatestSwarmPiranhas = 0;
	private int oppNotInGreatestSwarmPiranhas = 0;

	public BoardRater(Board board, PlayerColor playerColor, int turn) {
		this.turn = turn;

		Set<Field> ownPiranhas = new HashSet<>();
		Set<Field> oppPiranhas = new HashSet<>();
		
		for (int x = 0; x < Constants.BOARD_SIZE; x++) {
			for (int y = 0; y < Constants.BOARD_SIZE; y++) {
				Field field = board.getField(x, y);

				if (field.getState() != FieldState.RED && field.getState() != FieldState.BLUE) {
					continue;
				}

				if (field.getState().toString() == playerColor.toString()) {
					ownPiranhas.add(field);
					ownPiranhasPosition += evaluatePosition(x, y);
				} else {
					oppPiranhas.add(field);
					oppPiranhasPosition += evaluatePosition(x, y);
				}
				
				if (x == 0 || x == Constants.BOARD_SIZE - 1 || y == 0 || y == Constants.BOARD_SIZE - 1) {
					if (field.getState().toString() == playerColor.toString()) {
						ownPiranhasAtBorder++;
					} else {
						oppPiranhasAtBorder++;
					}
				}
			}
		}

		ownPiranhasCount = ownPiranhas.size();
		oppPiranhasCount = oppPiranhas.size();
		
		ownGreatestSwarmSize = GameRuleLogic.greatestSwarmSize(board, ownPiranhas);
		oppGreatestSwarmSize = GameRuleLogic.greatestSwarmSize(board, oppPiranhas);
		
		ownNotInGreatestSwarmPiranhas = ownPiranhasCount - ownGreatestSwarmSize;
		oppNotInGreatestSwarmPiranhas = oppPiranhasCount - oppGreatestSwarmSize;
	}

	public double evaluate() {
		double result = 0D;

		***REMOVED***
		***REMOVED***
		***REMOVED***
		***REMOVED***
		***REMOVED***
		
		return result;
	}

	private int evaluatePosition(int x, int y) {
		int result = 0;
		if (x >= Constants.BOARD_SIZE / 2) {
			result += Constants.BOARD_SIZE - 1 - x;
		} else {
			result += x;
		}
		if (y >= Constants.BOARD_SIZE / 2) {
			result += Constants.BOARD_SIZE - 1 - y;
		} else {
			result += y;
		}
		return result;
	}

}
