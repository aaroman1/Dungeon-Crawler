package com.comp301.a08dungeon.view;

import com.comp301.a08dungeon.controller.Controller;
import com.comp301.a08dungeon.model.Model;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class TitleScreenView implements FXComponent {
    private final Controller controller;
    private final Model model;

    public TitleScreenView(Controller controller, Model model) {
        this.controller = controller;
        this.model = model;
    }

    @Override
    public Parent render() {
        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.getStyleClass().add("title-screen");

        Label title = new Label("Dungeon Crawler");
        title.getStyleClass().add("title-text");

        Label highScore = new Label("High Score: " + model.getHighScore());
        highScore.getStyleClass().add("score-label");

        Label lastScore = new Label("Last Score: " + model.getCurScore());
        lastScore.getStyleClass().add("score-label");

        Button startBtn = new Button("Start Game");
        startBtn.setOnAction(e -> controller.startGame());

        root.getChildren().addAll(title, highScore, lastScore, startBtn);
        return root;
    }
}
