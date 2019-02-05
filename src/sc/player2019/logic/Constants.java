package sc.player2019.logic;

import java.util.ArrayList;
import java.util.Arrays;

import sc.plugin2019.Field;
import sc.plugin2019.FieldState;

public final class Constants extends sc.plugin2019.util.Constants {

	public static final int myNotConnectedPiranhasDiff = 2;
	public static final int opponentNotConnectedPiranhasDiff = 6;
	public static final int myPiranhaPositionDiff = 1;
	public static final int opponentPiranhaPositionDiff = 0;
	public static final int myPiranhaDistanceDiff = 1;
	public static final int opponentPiranhaDistanceDiff = 1;
	
	public static final boolean ignoreField(Field field) {
		if (field.getState() == FieldState.RED) {
			if (field.getX() == 0 || field.getY() == Constants.BOARD_SIZE - 1) {
				return true;
			}
		} else {
			if (field.getY() == 0 || field.getY() == Constants.BOARD_SIZE - 1) {
				return true;
			}
		}
		return false;
	}
	
	public static final double ignoredPiranhaSwarmCount = 0.5;
	
	public static final ArrayList<ArrayList<Integer>> boardPosition = new ArrayList<>();
	
	static {
		boardPosition.add(new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0)));
		boardPosition.add(new ArrayList<>(Arrays.asList(0,1,2,3,4,4,3,2,1,0)));
		boardPosition.add(new ArrayList<>(Arrays.asList(0,2,3,4,5,5,4,3,2,0)));
		boardPosition.add(new ArrayList<>(Arrays.asList(0,3,4,5,6,6,5,4,3,0)));
		boardPosition.add(new ArrayList<>(Arrays.asList(0,4,5,6,7,7,6,5,4,0)));
		boardPosition.add(new ArrayList<>(Arrays.asList(0,4,5,6,7,7,6,5,4,0)));
		boardPosition.add(new ArrayList<>(Arrays.asList(0,3,4,5,6,6,5,4,3,0)));
		boardPosition.add(new ArrayList<>(Arrays.asList(0,2,3,4,5,5,4,3,2,0)));
		boardPosition.add(new ArrayList<>(Arrays.asList(0,1,2,3,4,4,3,2,1,0)));
		boardPosition.add(new ArrayList<>(Arrays.asList(0,0,0,0,0,0,0,0,0,0)));
	}
	
}
