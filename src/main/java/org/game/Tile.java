package org.game;

import javafx.animation.Animation;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.converter.NumberStringConverter;

import static javafx.scene.text.Font.font;
import static javafx.scene.text.FontWeight.BOLD;
import static org.game.Constants.BORDER_SIZE;
import static org.game.Constants.DARK_FONT_COLOR;
import static org.game.Constants.FONT_FAMILY;
import static org.game.Constants.LIGHT_FONT_COLOR;
import static org.game.Constants.MOVE_DURATION;
import static org.game.Constants.SCALE_DURATION;
import static org.game.Constants.TILE_COLOR_FOR_VALUE;
import static org.game.Constants.TILE_SCALE_ON_ADD;
import static org.game.Constants.TILE_SCALE_ON_PROMOTE;
import static org.game.Constants.TILE_SIZE;

public class Tile extends StackPane {
    private final SimpleIntegerProperty value = new SimpleIntegerProperty(2);
    private final Rectangle tile;
    private final Text text;

    public Tile(int x, int y) {
        setLayoutX(x);
        setLayoutY(y);
        setScaleX(TILE_SCALE_ON_ADD);
        setScaleY(TILE_SCALE_ON_ADD);
        tile = new Rectangle(TILE_SIZE, TILE_SIZE);
        text = new Text();
        text.textProperty().bindBidirectional(value, new NumberStringConverter());
        updateForValue();
        getChildren().addAll(tile, text);
    }

    public int value() {
        return value.get();
    }

    public void promote() {
        value.set(value() * 2);
        updateForValue();
    }

    public Animation getMoveAnimation(Direction direction, int distance) {
        TranslateTransition transition = new TranslateTransition(MOVE_DURATION, this);
        int value = distance * (TILE_SIZE + BORDER_SIZE);
        switch (direction) {
            case UP -> transition.setByY(-value);
            case DOWN -> transition.setByY(value);
            case LEFT -> transition.setByX(-value);
            case RIGHT -> transition.setByX(value);
        }
        return transition;
    }

    public Animation getPromoteAnimation() {
        ScaleTransition transition = new ScaleTransition(SCALE_DURATION, this);
        transition.setToX(TILE_SCALE_ON_PROMOTE);
        transition.setToY(TILE_SCALE_ON_PROMOTE);
        transition.setAutoReverse(true);
        transition.setCycleCount(2);
        return transition;
    }

    private void updateForValue() {
        tile.setFill(getTileColorForValue());
        text.setFill(getTextColorForValue());
        text.setFont(getFontForValue());
    }

    private Color getTileColorForValue() {
        return TILE_COLOR_FOR_VALUE.get(value());
    }

    private Color getTextColorForValue() {
        if (value() < 8) {
            return DARK_FONT_COLOR;
        }
        return LIGHT_FONT_COLOR;
    }

    private Font getFontForValue() {
        return font(FONT_FAMILY, BOLD, getFontSizeForValue());
    }

    private int getFontSizeForValue() {
        return switch (value()) {
            case 2, 4, 8, 16, 32, 64 -> 55;
            case 128, 256, 512 -> 45;
            default -> 35;
        };
    }
}
