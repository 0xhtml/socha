package sc.player2019.logic;

import sc.plugin2019.Board;
import sc.plugin2019.Field;
import sc.plugin2019.util.Constants;
import sc.plugin2019.util.GameRuleLogic;
import sc.shared.PlayerColor;

public class BoardRater {

	public int redSwarmSize;
	public int blueSwarmSize;
	public int redDistanceBetweenFarestPiranhas;
	public int blueDistanceBetweenFarestPiranhas;
	public int redPiranhaPosition;
	public int bluePiranhaPosition;

	public BoardRater(Board board) {
		redSwarmSize = GameRuleLogic.greatestSwarmSize(board, PlayerColor.RED);
		blueSwarmSize = GameRuleLogic.greatestSwarmSize(board, PlayerColor.BLUE);
		redDistanceBetweenFarestPiranhas = getDistanceBetweenFarestPiranhas(board, PlayerColor.RED);
		blueDistanceBetweenFarestPiranhas = getDistanceBetweenFarestPiranhas(board, PlayerColor.BLUE);
		redPiranhaPosition = evaluatePiranhaPositions(board, PlayerColor.RED);
		bluePiranhaPosition = evaluatePiranhaPositions(board, PlayerColor.BLUE);
	}

	public int evaluate(BoardRater boardRaterAtStart, PlayerColor playerColor) {
		int mySwarmSizeDiff;
		int opponentSwarmSizeDiff;
		int myPiranhaPositionDiff;
		int opponentPiranhaPositionDiff;
		if (playerColor == PlayerColor.RED) {
			mySwarmSizeDiff = redSwarmSize - boardRaterAtStart.redSwarmSize;
			opponentSwarmSizeDiff = blueSwarmSize - boardRaterAtStart.blueSwarmSize;
			myPiranhaPositionDiff = redPiranhaPosition - boardRaterAtStart.redPiranhaPosition;
			opponentPiranhaPositionDiff = bluePiranhaPosition - boardRaterAtStart.bluePiranhaPosition;
		} else {
			mySwarmSizeDiff = blueSwarmSize - boardRaterAtStart.blueSwarmSize;
			opponentSwarmSizeDiff = redSwarmSize - boardRaterAtStart.redSwarmSize;
			myPiranhaPositionDiff = bluePiranhaPosition - boardRaterAtStart.bluePiranhaPosition;
			opponentPiranhaPositionDiff = redPiranhaPosition - boardRaterAtStart.redPiranhaPosition;
		}
		int bothSwarmSizeDiff = mySwarmSizeDiff - opponentSwarmSizeDiff;
		return myPiranhaPositionDiff + bothSwarmSizeDiff + (mySwarmSizeDiff / 2);
	}

	private int getDistanceBetweenFarestPiranhas(Board board, PlayerColor playerColor) {
		int distance = 0;
		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
			for (int j = 0; j < Constants.BOARD_SIZE; j++) {
				Field field = board.getField(i, j);
				if (field.getState().toString() == playerColor.toString()) {
					for (int i2 = 0; i2 < Constants.BOARD_SIZE; i2++) {
						for (int j2 = 0; j2 < Constants.BOARD_SIZE; j2++) {
							Field field2 = board.getField(i2, j2);
							if (field2.getState().toString() == playerColor.toString()) {
								int value = Math.abs(i - i2) + Math.abs(j - j2);
								if (value > distance) {
									distance = value;
								}
							}
						}
					}
				}
			}
		}
		return distance;
	}

	private int evaluatePiranhaPositions(Board board, PlayerColor playerColor) {
		int value = 0;

		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
			for (int j = 0; j < Constants.BOARD_SIZE; j++) {
				Field field = board.getField(i, j);
				int i2 = i;
				int j2 = j;
				if (i2 >= Constants.BOARD_SIZE / 2) {
					i2 = Constants.BOARD_SIZE - i - 1;
				}
				if (j2 >= Constants.BOARD_SIZE / 2) {
					j2 = Constants.BOARD_SIZE - j - 1;
				}
				if (field.getState().toString().equals(playerColor.toString())) {
					if (i != 0 && i != Constants.BOARD_SIZE - 1 && j != 0 && j != Constants.BOARD_SIZE - 1) {
						value += (i2 + j2);
					}
				}
			}
		}

		return value;
	}

	public String toString(BoardRater boardRaterAtStart) {
		String out = "";
		out += "RedSwarmSize: " + boardRaterAtStart.redSwarmSize + "»" + redSwarmSize + "\n";
		out += "BlueSwarmSize: " + boardRaterAtStart.blueSwarmSize + "»" + blueSwarmSize + "\n";
		out += "RedDistance: " + boardRaterAtStart.redDistanceBetweenFarestPiranhas + "»"
				+ redDistanceBetweenFarestPiranhas + "\n";
		out += "BlueDistance: " + boardRaterAtStart.blueDistanceBetweenFarestPiranhas + "»"
				+ blueDistanceBetweenFarestPiranhas + "\n";
		out += "RedPiranhaPosition: " + boardRaterAtStart.redPiranhaPosition + "»" + redPiranhaPosition + "\n";
		out += "BluePiranhaPosition: " + boardRaterAtStart.bluePiranhaPosition + "»" + bluePiranhaPosition + "\n";
		return out;
	}

}
