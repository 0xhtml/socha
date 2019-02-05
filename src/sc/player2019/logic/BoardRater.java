package sc.player2019.logic;

import sc.plugin2019.Board;
import sc.plugin2019.Field;
import sc.plugin2019.FieldState;
import sc.shared.PlayerColor;

public class BoardRater {

	public double redNotConnectedPiranhas;
	public double blueNotConnectedPiranhas;

	public int redPiranhasPosition;
	public int bluePiranhasPosition;
	
	public int redPiranhasDistance = 0;
	public int bluePiranhasDistance = 0;

	public BoardRater(Board board) { 
		int redPiranhasCount = 0;
		int bluePiranhasCount = 0;

		for (int x = 0; x < Constants.BOARD_SIZE; x++) {
			for (int y = 0; y < Constants.BOARD_SIZE; y++) {
				Field field = board.getField(x, y);

				if (field.getState() == FieldState.RED) {
					redPiranhasCount++;
				} else if (field.getState() == FieldState.BLUE) {
					bluePiranhasCount++;
				} else {
					continue;
				}

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
				
				if (Constants.ignoreField(field)) {
					continue;
				}

				if (field.getState() == FieldState.RED) {
					redPiranhasPosition += Constants.boardPosition.get(x).get(y);
				} else {
					bluePiranhasPosition += Constants.boardPosition.get(x).get(y);
				}
			}
		}
		
		
		double redGreatestNoBorderSwarmSize = NewGameRuleLogic.greatestSwarmSize(board, PlayerColor.RED);
		double blueGreatestNoBorderSwarmSize = NewGameRuleLogic.greatestSwarmSize(board, PlayerColor.BLUE);
		redNotConnectedPiranhas = redPiranhasCount - redGreatestNoBorderSwarmSize;
		blueNotConnectedPiranhas = bluePiranhasCount - blueGreatestNoBorderSwarmSize;
	}

	public int evaluate(BoardRater boardRaterAtStart, PlayerColor playerColor) {
		double myNotConnectedPiranhasDiff;
		double opponentNotConnectedPiranhasDiff;

		int myPiranhaPositionDiff;
		int opponentPiranhaPositionDiff;

		int myPiranhaDistanceDiff;
		int opponentPiranhaDistanceDiff;
		
		if (playerColor == PlayerColor.RED) {
			myNotConnectedPiranhasDiff = redNotConnectedPiranhas - boardRaterAtStart.redNotConnectedPiranhas;
			opponentNotConnectedPiranhasDiff = blueNotConnectedPiranhas - boardRaterAtStart.blueNotConnectedPiranhas;

			myPiranhaPositionDiff = redPiranhasPosition - boardRaterAtStart.redPiranhasPosition;
			opponentPiranhaPositionDiff = bluePiranhasPosition - boardRaterAtStart.bluePiranhasPosition;
			
			myPiranhaDistanceDiff = redPiranhasDistance - boardRaterAtStart.redPiranhasDistance;
			opponentPiranhaDistanceDiff = bluePiranhasDistance - boardRaterAtStart.bluePiranhasDistance;
		} else {
			myNotConnectedPiranhasDiff = blueNotConnectedPiranhas - boardRaterAtStart.blueNotConnectedPiranhas;
			opponentNotConnectedPiranhasDiff = redNotConnectedPiranhas - boardRaterAtStart.redNotConnectedPiranhas;

			myPiranhaPositionDiff = bluePiranhasPosition - boardRaterAtStart.bluePiranhasPosition;
			opponentPiranhaPositionDiff = redPiranhasPosition - boardRaterAtStart.redPiranhasPosition;
			
			myPiranhaDistanceDiff = bluePiranhasDistance - boardRaterAtStart.bluePiranhasDistance;
			opponentPiranhaDistanceDiff = redPiranhasDistance - boardRaterAtStart.redPiranhasDistance;
		}

		int result = 0;
		result -= myNotConnectedPiranhasDiff * Constants.myNotConnectedPiranhasDiff;
		result += opponentNotConnectedPiranhasDiff * Constants.opponentNotConnectedPiranhasDiff;
		result += myPiranhaPositionDiff * Constants.myPiranhaPositionDiff;
		result -= opponentPiranhaPositionDiff * Constants.opponentPiranhaPositionDiff;
		result -= myPiranhaDistanceDiff * Constants.myPiranhaDistanceDiff;
		result += opponentPiranhaDistanceDiff * Constants.opponentPiranhaDistanceDiff;
		return result;
	}

	public String toString(BoardRater boardRaterAtStart) {
		String out = "";
		out += "RedNotConnected: " + boardRaterAtStart.redNotConnectedPiranhas + "»" + redNotConnectedPiranhas + "\n";
		out += "BlueNotConnected: " + boardRaterAtStart.blueNotConnectedPiranhas + "»" + blueNotConnectedPiranhas + "\n";
		out += "RedPiranhaPosition: " + boardRaterAtStart.redPiranhasPosition + "»" + redPiranhasPosition + "\n";
		out += "BluePiranhaPosition: " + boardRaterAtStart.bluePiranhasPosition + "»" + bluePiranhasPosition + "\n";
		out += "RedPiranhaDistance: " + boardRaterAtStart.redPiranhasDistance + "»" + redPiranhasDistance + "\n";
		out += "BluePiranhaDistance: " + boardRaterAtStart.bluePiranhasDistance + "»" + bluePiranhasDistance + "\n";
		return out;
	}

}
