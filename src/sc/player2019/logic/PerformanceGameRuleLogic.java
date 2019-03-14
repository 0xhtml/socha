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
            if (fieldsInDirection.get(i).getState().toString().equals(gameState.getOtherPlayerColor().toString())) {
                return false;
            }
        }

        String fieldState = fieldsInDirection.get(distance).getState().toString();
        if (fieldState.equals(gameState.getCurrentPlayerColor().toString())) {
            return false;
        }
        return !fieldState.equals("OBSTRUCTED");
    }

    private static Set<Field> getOwnFields(Board board, PlayerColor player) {
        Set<Field> fields = new HashSet<>();
        for (int x = 0; x < Constants.BOARD_SIZE; x++) {
            for (int y = 0; y < Constants.BOARD_SIZE; y++) {
                Field field = board.getField(x, y);
                if (field.getState().toString().equals(player.toString())) {
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

}
