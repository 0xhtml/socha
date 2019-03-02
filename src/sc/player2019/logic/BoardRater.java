package sc.player2019.logic;

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
	
	protected int redPiranhasDistance = 0;
	protected int bluePiranhasDistance = 0;

	protected int redPiranhasCount = 0;
	protected int bluePiranhasCount = 0;
	
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

				// Die größte Distance zwischen den Fischen ermitteln
				for (int x2 = 0; x2 < Constants.BOARD_SIZE; x2++) {
					for (int y2 = 0; y2 < Constants.BOARD_SIZE; y2++) {
						Field field2 = board.getField(x2, y2);
						
						if (field2.getState() == field.getState()) {
							int distanceX = Math.abs(field.getX() - field2.getX());
							int distanceY = Math.abs(field.getY() - field2.getY());
							int distance = distanceX + distanceY;
							
							if (field.getState() == FieldState.RED) {
								if (distance > redPiranhasDistance) {
									redPiranhasDistance = distance;
								}
							} else {
								if (distance > bluePiranhasDistance) {
									bluePiranhasDistance = distance;
								}
							}
						}
					}
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
		redNoBorderSwarmSize = NewGameRuleLogic.greatestSwarmSize(board, PlayerColor.RED);
		blueNoBorderSwarmSize = NewGameRuleLogic.greatestSwarmSize(board, PlayerColor.BLUE);
	}

	public int evaluate(BoardRater boardRaterAtStart, PlayerColor playerColor) {
		int mySwarmSize;
		int otherSwarmSize;
		double myNoBorderSwarmSize;
		double otherNoBorderSwarmSize;
		int myPiranhasPosition;
		int otherPiranhasPosition;
		int myPiranhasDistance;
		int otherPiranhasDistance;
		int myPiranhasCount;
		int otherPiranhasCount;
		int startMyPiranhasCount;
		int startOtherPiranhasCount;
		if (playerColor == PlayerColor.RED) {
			mySwarmSize = redSwarmSize;
			otherSwarmSize = blueSwarmSize;
			myNoBorderSwarmSize = redNoBorderSwarmSize;
			otherNoBorderSwarmSize = blueNoBorderSwarmSize;
			myPiranhasPosition = redPiranhasPosition;
			otherPiranhasPosition = bluePiranhasPosition;
			myPiranhasDistance = redPiranhasDistance;
			otherPiranhasDistance = bluePiranhasDistance;
			myPiranhasCount = redPiranhasCount;
			otherPiranhasCount = bluePiranhasCount;
			startMyPiranhasCount = boardRaterAtStart.redPiranhasCount;
			startOtherPiranhasCount = boardRaterAtStart.bluePiranhasCount;
		} else {
			mySwarmSize = blueSwarmSize;
			otherSwarmSize = redSwarmSize;
			myNoBorderSwarmSize = blueNoBorderSwarmSize;
			otherNoBorderSwarmSize = redNoBorderSwarmSize;
			myPiranhasPosition = bluePiranhasPosition;
			otherPiranhasPosition = redPiranhasPosition;
			myPiranhasDistance = bluePiranhasDistance;
			otherPiranhasDistance = redPiranhasDistance;
			myPiranhasCount = bluePiranhasCount;
			otherPiranhasCount = redPiranhasCount;
			startMyPiranhasCount = boardRaterAtStart.bluePiranhasCount;
			startOtherPiranhasCount = boardRaterAtStart.redPiranhasCount;
		}
		
		int result = 0;
		
		if (startOtherPiranhasCount > otherPiranhasCount && Constants.eatUntilPiranhasCountDiff <= (startMyPiranhasCount - startOtherPiranhasCount)) {
			result -= 1000;
		}
		result -= (myPiranhasCount - myNoBorderSwarmSize) * Constants.myNoBorderNotConnectedPiranhasFac;
		result += (otherPiranhasCount - otherNoBorderSwarmSize) * Constants.otherNoBorderNotConnectedPiranhasFac;
		
		result += myPiranhasPosition * Constants.myPiranhasPositionFac;
		result -= otherPiranhasPosition * Constants.otherPiranhasPositionFac;
		
		result -= myPiranhasDistance * Constants.myPiranhasDistanceFac;
		result += otherPiranhasDistance * Constants.otherPiranhasDistanceFac;
		
		return result;
	}

}
