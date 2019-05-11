package sc.player2019.logic;

import sc.plugin2019.Board;
import sc.plugin2019.Field;
import sc.plugin2019.FieldState;
import sc.plugin2019.util.Constants;
import sc.shared.PlayerColor;

import java.util.HashSet;
import java.util.Set;

class BoardRater {

    private int turn;

    private int ownPiranhasCount;
    private int oppPiranhasCount;

    private int ownPiranhasPosition = 0;
    private int oppPiranhasPosition = 0;

    private int ownPiranhasAtBorder = 0;
    private int oppPiranhasAtBorder = 0;

    private int ownGreatestSwarmSize;
    private int oppGreatestSwarmSize;

    private int ownNotInGreatestSwarmPiranhas;
    private int oppNotInGreatestSwarmPiranhas;

    BoardRater(Board board, PlayerColor playerColor, int turn) {
        this.turn = turn;

        Set<Field> ownPiranhas = new HashSet<>();
        Set<Field> oppPiranhas = new HashSet<>();

        for (int x = 0; x < Constants.BOARD_SIZE; x++) {
            for (int y = 0; y < Constants.BOARD_SIZE; y++) {
                Field field = board.getField(x, y);

                if (field.getState() != FieldState.RED && field.getState() != FieldState.BLUE) {
                    continue;
                }

                if (field.getState().toString().equals(playerColor.toString())) {
                    ownPiranhas.add(field);
                    ownPiranhasPosition += evaluatePosition(x, y);
                } else {
                    oppPiranhas.add(field);
                    oppPiranhasPosition += evaluatePosition(x, y);
                }

                if (x == 0 || x == Constants.BOARD_SIZE - 1 || y == 0 || y == Constants.BOARD_SIZE - 1) {
                    if (field.getState().toString().equals(playerColor.toString())) {
                        ownPiranhasAtBorder++;
                    } else {
                        oppPiranhasAtBorder++;
                    }
                }
            }
        }

        ownPiranhasCount = ownPiranhas.size();
        oppPiranhasCount = oppPiranhas.size();

        ownGreatestSwarmSize = PerformanceGameRuleLogic.greatestSwarmSize(board, ownPiranhas);
        oppGreatestSwarmSize = PerformanceGameRuleLogic.greatestSwarmSize(board, oppPiranhas);

        ownNotInGreatestSwarmPiranhas = ownPiranhasCount - ownGreatestSwarmSize;
        oppNotInGreatestSwarmPiranhas = oppPiranhasCount - oppGreatestSwarmSize;
    }

    double evaluate() {
        double result = 0D;

        ***REMOVED***
        ***REMOVED***
        ***REMOVED***
        if (turn < 45) {
        	***REMOVED***
        } else {
        	***REMOVED***
        }
        ***REMOVED***

        return result;
    }

    private int evaluatePosition(int x, int y) {
        int result = 0;
        if (x >= Constants.BOARD_SIZE / 2) {
            result += Constants.BOARD_SIZE - 1 - x;
        } else {
            result += x;
        }
        if (y >= Constants.BOARD_SIZE / 2) {
            result += Constants.BOARD_SIZE - 1 - y;
        } else {
            result += y;
        }
        return result;
    }

}
