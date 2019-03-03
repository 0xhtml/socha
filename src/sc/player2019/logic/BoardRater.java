package sc.player2019.logic;

import java.util.Set;

import sc.plugin2019.Board;
import sc.plugin2019.Field;
import sc.plugin2019.FieldState;
import sc.plugin2019.util.GameRuleLogic;
import sc.shared.PlayerColor;

public class BoardRater {

	protected int redSwarmSize = 0;
	protected int blueSwarmSize = 0;

	protected double redNoBorderSwarmSize = 0;
	protected double blueNoBorderSwarmSize = 0;

	protected int redPiranhasPosition = 0;
	protected int bluePiranhasPosition = 0;

	protected int redPiranhasCount = 0;
	protected int bluePiranhasCount = 0;

	protected int redSwarmDistance = 0;
	protected int blueSwarmDistance = 0;

	public BoardRater(Board board) {
		for (int x = 0; x < Constants.BOARD_SIZE; x++) {
			for (int y = 0; y < Constants.BOARD_SIZE; y++) {
				Field field = board.getField(x, y);

				// Überspringen, wenn das aktuelle Feld kein Fisch ist.
				if (field.getState() != FieldState.RED && field.getState() != FieldState.BLUE) {
					continue;
				}

				// Die Fische zählen
				if (field.getState() == FieldState.RED) {
					redPiranhasCount++;
				} else {
					bluePiranhasCount++;
				}

				// Überspringe, wenn Feld ignoriet werden soll
				if (Constants.ignoreField(field)) {
					continue;
				}

				// Die Fischposition bewerten.
				if (field.getState() == FieldState.RED) {
					redPiranhasPosition += Constants.boardPosition.get(x).get(y);
				} else {
					bluePiranhasPosition += Constants.boardPosition.get(x).get(y);
				}
			}
		}

		redSwarmSize = GameRuleLogic.greatestSwarmSize(board, PlayerColor.RED);
		blueSwarmSize = GameRuleLogic.greatestSwarmSize(board, PlayerColor.BLUE);

		Set<Field> redSwarm = NewGameRuleLogic.greatestSwarm(board, PlayerColor.RED);
		Set<Field> blueSwarm = NewGameRuleLogic.greatestSwarm(board, PlayerColor.BLUE);
		for (Field a : redSwarm) {
			for (Field b : redSwarm) {
				int distance = Math.abs(a.getX() - b.getX()) + Math.abs(a.getX() - b.getX());
				if (distance > redSwarmDistance) {
					redSwarmDistance = distance;
				}
			}
		}
		for (Field a : blueSwarm) {
			for (Field b : blueSwarm) {
				int distance = Math.abs(a.getX() - b.getX()) + Math.abs(a.getX() - b.getX());
				if (distance > blueSwarmDistance) {
					blueSwarmDistance = distance;
				}
			}
		}

		redNoBorderSwarmSize = NewGameRuleLogic.swarmSize(redSwarm);
		blueNoBorderSwarmSize = NewGameRuleLogic.swarmSize(blueSwarm);
	}

	public int evaluate(BoardRater boardRaterAtStart, PlayerColor playerColor) {
		double myNoBorderSwarmSize;
		double otherNoBorderSwarmSize;

		int myPiranhasPosition;
		int otherPiranhasPosition;

		int myPiranhasCount;
		int otherPiranhasCount;

		int mySwarmDistance;
		int otherSwarmDistance;

		int startOtherPiranhasCount;
		if (playerColor == PlayerColor.RED) {
			myNoBorderSwarmSize = redNoBorderSwarmSize;
			otherNoBorderSwarmSize = blueNoBorderSwarmSize;

			myPiranhasPosition = redPiranhasPosition;
			otherPiranhasPosition = bluePiranhasPosition;

			myPiranhasCount = redPiranhasCount;
			otherPiranhasCount = bluePiranhasCount;

			mySwarmDistance = redSwarmDistance;
			otherSwarmDistance = blueSwarmDistance;

			startOtherPiranhasCount = boardRaterAtStart.bluePiranhasCount;
		} else {
			myNoBorderSwarmSize = blueNoBorderSwarmSize;
			otherNoBorderSwarmSize = redNoBorderSwarmSize;

			myPiranhasPosition = bluePiranhasPosition;
			otherPiranhasPosition = redPiranhasPosition;

			myPiranhasCount = bluePiranhasCount;
			otherPiranhasCount = redPiranhasCount;

			mySwarmDistance = blueSwarmDistance;
			otherSwarmDistance = redSwarmDistance;

			startOtherPiranhasCount = boardRaterAtStart.redPiranhasCount;
		}

		int result = 0;

		if (startOtherPiranhasCount > otherPiranhasCount && Constants.eatUntilPiranhasCount >= otherPiranhasCount) {
			result -= 10000;
		}

		result -= (myPiranhasCount - myNoBorderSwarmSize) * Constants.myNoBorderNotConnectedPiranhasFac;
		result += (otherPiranhasCount - otherNoBorderSwarmSize) * Constants.otherNoBorderNotConnectedPiranhasFac;

		result += myPiranhasPosition * Constants.myPiranhasPositionFac;
		result -= otherPiranhasPosition * Constants.otherPiranhasPositionFac;

		result += mySwarmDistance * Constants.mySwarmDistanceFac;
		result -= otherSwarmDistance * Constants.otherSwarmDistanceFac;

		return result;
	}

}
