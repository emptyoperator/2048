package org.game;

import javafx.scene.input.KeyCode;

public enum Direction {
    UP, DOWN, LEFT, RIGHT;

    public static Direction fromKeyCode(KeyCode code) {
        return switch (code) {
            case UP -> UP;
            case DOWN -> DOWN;
            case LEFT -> LEFT;
            case RIGHT -> RIGHT;
            default -> null;
        };
    }
}
