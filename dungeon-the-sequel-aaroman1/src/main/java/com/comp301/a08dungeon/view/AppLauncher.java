package com.comp301.a08dungeon.view;

import com.comp301.a08dungeon.model.Model;
import com.comp301.a08dungeon.model.ModelImpl;
import com.comp301.a08dungeon.controller.Controller;
import com.comp301.a08dungeon.controller.ControllerImpl;
import com.comp301.a08dungeon.model.Observer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppLauncher extends Application {
    private static final int BOARD_ROWS = 10;
    private static final int BOARD_COLS = 10;
    private static final int SCENE_WIDTH = 600;
    private static final int SCENE_HEIGHT = 650;

    @Override
    public void start(Stage stage) {
        stage.setTitle("Your Name’s Dungeon Crawler");

        Model model = new ModelImpl(BOARD_ROWS, BOARD_COLS);
        Controller controller = new ControllerImpl(model);
        View view = new View(controller, model, stage, SCENE_WIDTH, SCENE_HEIGHT);

        model.addObserver((Observer) view);

        Scene scene = new Scene(view.render(), SCENE_WIDTH, SCENE_HEIGHT);
        scene.getStylesheets().add("dungeon.css");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
