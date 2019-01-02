package sc.player2019.logic;

import sc.plugin2019.Board;
import sc.plugin2019.Field;
import sc.plugin2019.util.Constants;
import sc.plugin2019.util.GameRuleLogic;
import sc.shared.PlayerColor;

public class BoardRater {

	public int redSwarmSize;
	public int blueSwarmSize;
	public int redNotConnectedPiranhas;
	public int blueNotConnectedPiranhas;
	public int redPiranhaPosition;
	public int bluePiranhaPosition;

	public BoardRater(Board board) {
		redSwarmSize = GameRuleLogic.greatestSwarmSize(board, PlayerColor.RED);
		blueSwarmSize = GameRuleLogic.greatestSwarmSize(board, PlayerColor.BLUE);

		redNotConnectedPiranhas = GameRuleLogic.getOwnFields(board, PlayerColor.RED).size() - redSwarmSize;
		blueNotConnectedPiranhas = GameRuleLogic.getOwnFields(board, PlayerColor.BLUE).size() - blueSwarmSize;

		redPiranhaPosition = evaluatePiranhaPositions(board, PlayerColor.RED);
		bluePiranhaPosition = evaluatePiranhaPositions(board, PlayerColor.BLUE);
	}

	@SuppressWarnings("unused")
	public int evaluate(BoardRater boardRaterAtStart, PlayerColor playerColor) {
		int mySwarmSizeDiff;
		int opponentSwarmSizeDiff;

		int myNotConnectedPiranhasDiff;
		int opponentNotConnectedPiranhasDiff;

		int myPiranhaPositionDiff;
		int opponentPiranhaPositionDiff;

		if (playerColor == PlayerColor.RED) {
			mySwarmSizeDiff = redSwarmSize - boardRaterAtStart.redSwarmSize;
			opponentSwarmSizeDiff = blueSwarmSize - boardRaterAtStart.blueSwarmSize;

			myNotConnectedPiranhasDiff = redNotConnectedPiranhas - boardRaterAtStart.redNotConnectedPiranhas;
			opponentNotConnectedPiranhasDiff = blueNotConnectedPiranhas - boardRaterAtStart.blueNotConnectedPiranhas;

			myPiranhaPositionDiff = redPiranhaPosition - boardRaterAtStart.redPiranhaPosition;
			opponentPiranhaPositionDiff = bluePiranhaPosition - boardRaterAtStart.bluePiranhaPosition;
		} else {
			mySwarmSizeDiff = blueSwarmSize - boardRaterAtStart.blueSwarmSize;
			opponentSwarmSizeDiff = redSwarmSize - boardRaterAtStart.redSwarmSize;

			myNotConnectedPiranhasDiff = blueNotConnectedPiranhas - boardRaterAtStart.blueNotConnectedPiranhas;
			opponentNotConnectedPiranhasDiff = redNotConnectedPiranhas - boardRaterAtStart.redNotConnectedPiranhas;
			
			myPiranhaPositionDiff = bluePiranhaPosition - boardRaterAtStart.bluePiranhaPosition;
			opponentPiranhaPositionDiff = redPiranhaPosition - boardRaterAtStart.redPiranhaPosition;
		}

		return (-myNotConnectedPiranhasDiff * 2) + (opponentNotConnectedPiranhasDiff * 2) + (myPiranhaPositionDiff);
	}

//	private int getDistanceBetweenFarestPiranhas(Board board, PlayerColor playerColor) {
//		int distance = 0;
//		for (int i = 0; i < Constants.BOARD_SIZE; i++) {
//			for (int j = 0; j < Constants.BOARD_SIZE; j++) {
//				Field field = board.getField(i, j);
//				if (field.getState().toString() == playerColor.toString()) {
//					for (int i2 = 0; i2 < Constants.BOARD_SIZE; i2++) {
//						for (int j2 = 0; j2 < Constants.BOARD_SIZE; j2++) {
//							Field field2 = board.getField(i2, j2);
//							if (field2.getState().toString() == playerColor.toString()) {
//								int value = Math.abs(i - i2) + Math.abs(j - j2);
//								if (value > distance) {
//									distance = value;
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//		return distance;
//	}

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
		out += "RedNotConnected: " + boardRaterAtStart.redNotConnectedPiranhas + "»" + redNotConnectedPiranhas + "\n";
		out += "BlueNotConnected: " + boardRaterAtStart.blueNotConnectedPiranhas + "»" + blueNotConnectedPiranhas + "\n";
		out += "RedPiranhaPosition: " + boardRaterAtStart.redPiranhaPosition + "»" + redPiranhaPosition + "\n";
		out += "BluePiranhaPosition: " + boardRaterAtStart.bluePiranhaPosition + "»" + bluePiranhaPosition + "\n";
		return out;
	}

}
