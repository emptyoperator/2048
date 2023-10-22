package org.game;

import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.util.Map;

import static java.util.Map.entry;
import static javafx.scene.paint.Color.rgb;
import static javafx.util.Duration.millis;

public class Constants {
    public static final String TITLE = "2048";
    public static final String BOARD_FXML = "board.fxml";
    public static final String STYLE_CSS = "style.css";
    public static final String FONT_FAMILY = "Clear Sans";
    public static final int TILE_SIZE = 100;
    public static final int BORDER_SIZE = 17;
    public static final int BOARD_SIZE = 4;
    public static final int SCENE_SIZE = BOARD_SIZE * TILE_SIZE + (BOARD_SIZE + 1) * BORDER_SIZE;
    public static final double TILE_SCALE_ON_ADD = 1. / 5;
    public static final double TILE_SCALE_ON_PROMOTE = 1.34;
    public static final Duration MOVE_DURATION = millis(100);
    public static final Duration SCALE_DURATION = millis(100);
    public static final Color DARK_FONT_COLOR = Color.rgb(119, 110, 101);
    public static final Color LIGHT_FONT_COLOR = Color.rgb(249, 246, 242);
    public static final Map<Integer, Color> TILE_COLOR_FOR_VALUE = Map.ofEntries(
            entry(2, rgb(238, 228, 218)),
            entry(4, rgb(237, 224, 200)),
            entry(8, rgb(242, 177, 121)),
            entry(16, rgb(245, 149, 99)),
            entry(32, rgb(246, 124, 95)),
            entry(64, rgb(246, 94, 59)),
            entry(128, rgb(237, 207, 114)),
            entry(256, rgb(237, 204, 97)),
            entry(512, rgb(237, 200, 80)),
            entry(1024, rgb(237, 197, 63)),
            entry(2048, rgb(237, 194, 46))
    );
}
