package org.game;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

import static java.util.Objects.requireNonNull;
import static org.game.Constants.BOARD_FXML;
import static org.game.Constants.SCENE_SIZE;
import static org.game.Constants.STYLE_CSS;
import static org.game.Constants.TITLE;

public class Game extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        Parent board = FXMLLoader.load(requireNonNull(getClass().getResource(BOARD_FXML)));
        Tiles tiles = new Tiles();
        StackPane root = new StackPane(board, tiles);
        Scene scene = new Scene(root, SCENE_SIZE , SCENE_SIZE);
        scene.getStylesheets().add(requireNonNull(getClass().getResource(STYLE_CSS)).toExternalForm());
        stage.setTitle(TITLE);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
        tiles.requestFocus();
    }

    public static void main(String[] args) {
        launch();
    }
}
