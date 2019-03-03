package sc.player2019.logic;

import java.util.ArrayList;
import java.util.Arrays;

import sc.plugin2019.Field;
import sc.plugin2019.FieldState;

public final class Constants extends sc.plugin2019.util.Constants {

	public static int myNoBorderNotConnectedPiranhasFac = 32; // (-) 16-0
	public static int otherNoBorderNotConnectedPiranhasFac = 16; // (+) 16-0

	public static int myPiranhasPositionFac = 2; // (+) 0-124
	public static int otherPiranhasPositionFac = 1; // (-) 0-124

	public static int mySwarmDistanceFac = 10; // (+) 0-18
	public static int otherSwarmDistanceFac = 0; // (-) 0-18

	public static int eatUntilPiranhasCount = MAX_FISH / 2;

	public static boolean ignoreField(Field field) {
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

	public static double onlyIgnoredPiranhaSwarmCount = 0;
	public static double mixedIgnoredPiranhaSwarmCount = 0.6;

	public static ArrayList<ArrayList<Integer>> boardPosition = new ArrayList<>();

	static {
		boardPosition.add(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 5, 4, 3, 2, 1)));
		boardPosition.add(new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6, 6, 5, 4, 3, 2)));
		boardPosition.add(new ArrayList<>(Arrays.asList(3, 4, 5, 6, 7, 7, 6, 5, 4, 3)));
		boardPosition.add(new ArrayList<>(Arrays.asList(4, 5, 6, 7, 8, 8, 7, 6, 5, 4)));
		boardPosition.add(new ArrayList<>(Arrays.asList(5, 6, 7, 8, 8, 8, 8, 7, 6, 5)));
		boardPosition.add(new ArrayList<>(Arrays.asList(5, 6, 7, 8, 8, 8, 8, 7, 6, 5)));
		boardPosition.add(new ArrayList<>(Arrays.asList(4, 5, 6, 7, 8, 8, 7, 6, 5, 4)));
		boardPosition.add(new ArrayList<>(Arrays.asList(3, 4, 5, 6, 7, 7, 6, 5, 4, 3)));
		boardPosition.add(new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6, 6, 5, 4, 3, 2)));
		boardPosition.add(new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 5, 4, 3, 2, 1)));
	}

}
