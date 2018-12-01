package sc.player2019.logic;

import sc.plugin2019.Board;
import sc.plugin2019.Field;
import sc.plugin2019.util.Constants;
import sc.plugin2019.util.GameRuleLogic;
import sc.shared.PlayerColor;

public class BoardRater {

	private int redSwarmSize;
	private int blueSwarmSize;
	private int redDistanceBetweenFarestPiranhas;
	private int blueDistanceBetweenFarestPiranhas;
	
	public BoardRater(Board board) {
		redSwarmSize = GameRuleLogic.greatestSwarmSize(board, PlayerColor.RED);
		blueSwarmSize = GameRuleLogic.greatestSwarmSize(board, PlayerColor.BLUE);
		redDistanceBetweenFarestPiranhas = getDistanceBetweenFarestPiranhas(board, PlayerColor.RED);
		blueDistanceBetweenFarestPiranhas = getDistanceBetweenFarestPiranhas(board, PlayerColor.BLUE);
	}
	
	public int evaluate(BoardRater boardRater, PlayerColor currentPlayerColor) {
		if (currentPlayerColor == PlayerColor.RED) {
			return redSwarmSize - blueSwarmSize;
		} else {
			return blueSwarmSize - redSwarmSize;
		}
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
	
	public String toString() {
		String out = "RedSwarmSize: " + redSwarmSize + "\n";
		out += "BlueSwarmSize: " + blueSwarmSize + "\n";
		out += "RedDistance: " + redDistanceBetweenFarestPiranhas + "\n";
		out += "BlueDistance: " + blueDistanceBetweenFarestPiranhas + "\n";
		return out;
	}
	
}
