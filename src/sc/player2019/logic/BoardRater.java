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

	public int redNotConnectedPiranhas;
	public int blueNotConnectedPiranhas;

	public int redPiranhasPosition;
	public int bluePiranhasPosition;

	public BoardRater(Board board) {
		Set<Field> redNoBorderPiranhas = new HashSet<>();
		Set<Field> blueNoBorderPiranhas = new HashSet<>();
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

				if (x == 0 || y == 0 || x == Constants.BOARD_SIZE - 1 || y == Constants.BOARD_SIZE - 1) {
					continue;
				}

				int x2 = x;
				int y2 = y;
				if (x >= Constants.BOARD_SIZE / 2) {
					x2 = Constants.BOARD_SIZE - x2 - 1;
				}
				if (y >= Constants.BOARD_SIZE / 2) {
					y2 = Constants.BOARD_SIZE - y2 - 1;
				}

				if (field.getState() == FieldState.RED) {
					redPiranhasPosition += x2 + y2;
					redNoBorderPiranhas.add(field);
				} else {
					bluePiranhasPosition += x2 + y2;
					blueNoBorderPiranhas.add(field);
				}
			}
		}

		int redGreatestNoBorderSwarmSize = GameRuleLogic.greatestSwarmSize(board, redNoBorderPiranhas);
		int blueGreatestNoBorderSwarmSize = GameRuleLogic.greatestSwarmSize(board, blueNoBorderPiranhas);
		redNotConnectedPiranhas = redPiranhasCount - redGreatestNoBorderSwarmSize;
		blueNotConnectedPiranhas = bluePiranhasCount - blueGreatestNoBorderSwarmSize;
	}

	public int evaluate(BoardRater boardRaterAtStart, PlayerColor playerColor) {
		int myNotConnectedPiranhasDiff;
		int opponentNotConnectedPiranhasDiff;

		int myPiranhaPositionDiff;
		int opponentPiranhaPositionDiff;

		if (playerColor == PlayerColor.RED) {
			myNotConnectedPiranhasDiff = redNotConnectedPiranhas - boardRaterAtStart.redNotConnectedPiranhas;
			opponentNotConnectedPiranhasDiff = blueNotConnectedPiranhas - boardRaterAtStart.blueNotConnectedPiranhas;

			myPiranhaPositionDiff = redPiranhasPosition - boardRaterAtStart.redPiranhasPosition;
			opponentPiranhaPositionDiff = bluePiranhasPosition - boardRaterAtStart.bluePiranhasPosition;
		} else {
			myNotConnectedPiranhasDiff = blueNotConnectedPiranhas - boardRaterAtStart.blueNotConnectedPiranhas;
			opponentNotConnectedPiranhasDiff = redNotConnectedPiranhas - boardRaterAtStart.redNotConnectedPiranhas;

			myPiranhaPositionDiff = bluePiranhasPosition - boardRaterAtStart.bluePiranhasPosition;
			opponentPiranhaPositionDiff = redPiranhasPosition - boardRaterAtStart.redPiranhasPosition;
		}

		int result = 0;
		result -= myNotConnectedPiranhasDiff * 2;
		result += opponentNotConnectedPiranhasDiff * 2;
		result += myPiranhaPositionDiff;
		result -= opponentPiranhaPositionDiff * 0;
		return result;
	}

	public String toString(BoardRater boardRaterAtStart) {
		String out = "";
		out += "RedNotConnected: " + boardRaterAtStart.redNotConnectedPiranhas + "»" + redNotConnectedPiranhas + "\n";
		out += "BlueNotConnected: " + boardRaterAtStart.blueNotConnectedPiranhas + "»" + blueNotConnectedPiranhas + "\n";
		out += "RedPiranhaPosition: " + boardRaterAtStart.redPiranhasPosition + "»" + redPiranhasPosition + "\n";
		out += "BluePiranhaPosition: " + boardRaterAtStart.bluePiranhasPosition + "»" + bluePiranhasPosition + "\n";
		return out;
	}

}
