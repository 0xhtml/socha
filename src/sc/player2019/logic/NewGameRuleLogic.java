package sc.player2019.logic;

import java.util.HashSet;
import java.util.Set;

import sc.plugin2019.Board;
import sc.plugin2019.Field;
import sc.shared.PlayerColor;

public class NewGameRuleLogic {

	private NewGameRuleLogic() {
		throw new IllegalStateException("Can't be instantiated.");
	}

	public static Set<Field> getOwnFields(Board board, PlayerColor player) {
		Set<Field> fields = new HashSet<>();
		int size = 0;
		for (int i = 0; i < Constants.BOARD_SIZE && Constants.MAX_FISH > size; i++) {
			for (int j = 0; j < Constants.BOARD_SIZE && Constants.MAX_FISH > size; j++) {
				Field curField = board.getField(i, j);
				if (curField.getPiranha().isPresent() && curField.getPiranha().get().equals(player)) {
					fields.add(curField);
					size++;
				}
			}
		}
		return fields;
	}

	private static Set<Field> getDirectNeighbour(Board board, Field f, Set<Field> parentSet) {
		Set<Field> returnSet = new HashSet<>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				int x = f.getX() + i;
				int y = f.getY() + j;
				if (x < 0 || x >= Constants.BOARD_SIZE || y < 0 || y >= Constants.BOARD_SIZE || (i == 0 && j == 0))
					continue;

				Field field = board.getField(x, y);
				if (parentSet.contains(field)) {
					returnSet.add(field);
				}
			}
		}
		return returnSet;
	}

	private static Set<Field> getSwarm(Board board, Set<Field> found, Set<Field> swarm) {
		if (swarm.isEmpty() && !found.isEmpty()) {
			Field field = found.iterator().next();
			swarm.add(field);
			found.remove(field);
		}

		Set<Field> tmpSwarm = new HashSet<>(swarm);
		for (Field field : swarm) {
			Set<Field> neighbours = getDirectNeighbour(board, field, found);
			tmpSwarm.addAll(neighbours);
		}

		if (swarmSize(swarm) != swarmSize(tmpSwarm))
			tmpSwarm = getSwarm(board, found, tmpSwarm);

		swarm.addAll(tmpSwarm);

		found.removeAll(swarm);
		return swarm;
	}

	public static Set<Field> greatestSwarm(Board board, Set<Field> fieldsToCheck) {
		Set<Field> occupiedFields = new HashSet<>(fieldsToCheck);
		Set<Field> greatestSwarm = new HashSet<>();
		double maxSize = -1;

		while (!occupiedFields.isEmpty() && swarmSize(occupiedFields) > maxSize) {
			Set<Field> empty = new HashSet<>();
			Set<Field> swarm = getSwarm(board, occupiedFields, empty);
			if (maxSize < swarmSize(swarm)) {
				maxSize = swarmSize(swarm);
				greatestSwarm = swarm;
			}
		}
		return greatestSwarm;
	}

	private static Set<Field> greatestSwarm(Board board, PlayerColor player) {
		Set<Field> occupiedFields = getOwnFields(board, player);
		return greatestSwarm(board, occupiedFields);
	}

	public static double greatestSwarmSize(Board board, PlayerColor player) {
		return swarmSize(greatestSwarm(board, player));
	}

	private static double swarmSize(Set<Field> swarm) {
		double result = 0;
		for (Field field : swarm) {
			if (Constants.ignoreField(field)) {
				result += Constants.ignoredPiranhaSwarmCount;
			} else {
				result += 1;
			}
		}
		return result;
	}

}
