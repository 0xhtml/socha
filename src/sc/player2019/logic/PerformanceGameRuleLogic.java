package sc.player2019.logic;

import sc.plugin2019.*;
import sc.plugin2019.util.Constants;
import sc.plugin2019.util.GameRuleLogic;
import sc.plugin2019.util.Point;
import sc.shared.PlayerColor;

import java.util.*;

class PerformanceGameRuleLogic {

    static ArrayList<Move> getPossibleMoves(GameState state) {
        ArrayList<Move> possibleMoves = new ArrayList<>();
        Collection<Field> fields = getOwnFields(state.getBoard(), state.getCurrentPlayerColor());
        for (Field field : fields) {
            int x = field.getX();
            int y = field.getY();
            for (Direction direction : Direction.values()) {
                if (isValidToMove(state, x, y, direction)) {
                    Move m = new Move(x, y, direction);
                    possibleMoves.add(m);
                }
            }
        }
        return possibleMoves;
    }

    private static boolean isValidToMove(GameState gameState, int x, int y, Direction direction) {
        Board board = gameState.getBoard();

        int distance = GameRuleLogic.calculateMoveDistance(board, x, y, direction);

        List<Field> fieldsInDirection;
        try {
            fieldsInDirection = getFieldsInDirection(board, x, y, direction, distance);
        } catch (ArrayIndexOutOfBoundsException e) {
            return false;
        }

        for (int i = 0; i < fieldsInDirection.size(); i++) {
            if (i == distance) {
                continue;
            }
            if (fieldsInDirection.get(i).getPiranha().isPresent() && fieldsInDirection.get(i).getPiranha().get().equals(gameState.getOtherPlayerColor())) {
                return false;
            }
        }

        Field field = fieldsInDirection.get(distance);
        if (field.getPiranha().isPresent() && field.getPiranha().get().equals(gameState.getCurrentPlayerColor())) {
            return false;
        }
        if (field.isObstructed()) {
        	return false;
        }
        return true;
    }

    private static Set<Field> getOwnFields(Board board, PlayerColor playerColor) {
        Set<Field> fields = new HashSet<>();
        for (int x = 0; x < Constants.BOARD_SIZE; x++) {
            for (int y = 0; y < Constants.BOARD_SIZE; y++) {
                Field field = board.getField(x, y);
                if (field.getPiranha().isPresent() && field.getPiranha().get().equals(playerColor)) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }

    static boolean isSwarmConnected(Board board, PlayerColor player) {
        Set<Field> fields = getOwnFields(board, player);
        int greatestSwarmSize = GameRuleLogic.greatestSwarmSize(board, fields);
        return greatestSwarmSize == fields.size();
    }

    private static List<Field> getFieldsInDirection(Board board, int x, int y, Direction direction, int distance) {
        List<Field> fields = new ArrayList<>();
        Point shift = direction.shift();
        for (int i = 0; i < distance + 1; i++) {
            fields.add(board.getField(x + shift.getX() * i, y + shift.getY() * i));
        }
        return fields;
    }

    private static Set<Field> getDirectNeighbour(Board board, Field field) {
        Set<Field> neighbours = new HashSet<>();
        int x = field.getX();
        int y = field.getY();
        FieldState state = field.getState();

        for (int neighbourXOffset = -1; neighbourXOffset <= 1; neighbourXOffset++) {
            for (int neighbourYOffset = -1; neighbourYOffset <= 1; neighbourYOffset++) {
                if (neighbourXOffset == 0 && neighbourYOffset == 0) {
                    continue;
                }
                int neighbourX = x + neighbourXOffset;
                int neighbourY = y + neighbourYOffset;
                if (neighbourX < 0 || neighbourX >= Constants.BOARD_SIZE || neighbourY < 0 || neighbourY >= Constants.BOARD_SIZE) {
                    continue;
                }

                Field neighbourField = board.getField(neighbourX, neighbourY);
                if (neighbourField.getState().equals(state)) {
                    neighbours.add(neighbourField);
                }
            }
        }

        return neighbours;
    }

    private static Set<Field> getSwarm(Board board, Set<Field> fields) {
        Set<Field> checkedSwarm = new HashSet<>();
        Set<Field> uncheckedSwarm = new HashSet<>();

        if (!fields.isEmpty()) {
            Field field = fields.iterator().next();
            uncheckedSwarm.add(field);
            fields.remove(field);
        }

        while (uncheckedSwarm.size() > 0) {
            Set<Field> newSwarm = new HashSet<>();
            for (Field field : uncheckedSwarm) {
                Set<Field> neighbours = getDirectNeighbour(board, field);
                newSwarm.addAll(neighbours);
            }
            newSwarm.removeAll(checkedSwarm);
            checkedSwarm.addAll(uncheckedSwarm);
            uncheckedSwarm.clear();
            uncheckedSwarm.addAll(newSwarm);
        }

        fields.removeAll(checkedSwarm);
        return checkedSwarm;
    }

    static int greatestSwarmSize(Board board, Set<Field> fields) {
        int maxSize = -1;

        while (!fields.isEmpty() && fields.size() > maxSize) {
            Set<Field> swarm = getSwarm(board, fields);
            int size = swarm.size();
            if (maxSize < size) {
                maxSize = size;
            }
        }

        return maxSize;
    }

}
