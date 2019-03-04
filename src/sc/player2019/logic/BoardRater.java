package sc.player2019.logic;

import sc.plugin2019.Board;
import sc.plugin2019.Field;
import sc.plugin2019.FieldState;
import sc.plugin2019.util.Constants;
import sc.plugin2019.util.GameRuleLogic;
import sc.shared.PlayerColor;

public class BoardRater {

	private int turn;

	private int myPiranhasCount = 0;
	private int otherPiranhasCount = 0;

	private int myPiranhasPosition = 0;
	private int otherPiranhasPosition = 0;

	private int myPiranhasAtBorder = 0;
	private int otherPiranhasAtBorder = 0;

	private int myGreatestSwarmSize = 0;
	private int otherGreatestSwarmSize = 0;

	private int myNotInGreatestSwarmPiranhas = 0;
	private int otherNotInGreatestSwarmPiranhas = 0;

	public BoardRater(Board board, PlayerColor playerColor, int turn) {
		this.turn = turn;

		for (int x = 0; x < Constants.BOARD_SIZE; x++) {
			for (int y = 0; y < Constants.BOARD_SIZE; y++) {
				Field field = board.getField(x, y);

				// Überspringen, wenn das aktuelle Feld kein Fisch ist.
				if (field.getState() != FieldState.RED && field.getState() != FieldState.BLUE) {
					continue;
				}

				// Die Fische zählen
				if (field.getState().toString() == playerColor.toString()) {
					myPiranhasCount++;
				} else {
					otherPiranhasCount++;
				}

				// Zählen der Fische am Rand
				if (x == 0 || x == Constants.BOARD_SIZE - 1 || y == 0 || y == Constants.BOARD_SIZE - 1) {
					if (field.getState().toString() == playerColor.toString()) {
						myPiranhasAtBorder++;
					} else {
						otherPiranhasAtBorder++;
					}
				}

				// Die Fischposition bewerten.
				if (field.getState().toString() == playerColor.toString()) {
					myPiranhasPosition += evaluatePosition(x, y);
				} else {
					otherPiranhasPosition += evaluatePosition(x, y);
				}
			}
		}

		// Größter Schwarm
		myGreatestSwarmSize = GameRuleLogic.greatestSwarmSize(board, playerColor);
		otherGreatestSwarmSize = GameRuleLogic.greatestSwarmSize(board, playerColor.opponent());
		myNotInGreatestSwarmPiranhas = myPiranhasCount - myGreatestSwarmSize;
		otherNotInGreatestSwarmPiranhas = otherPiranhasCount - otherGreatestSwarmSize;
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
