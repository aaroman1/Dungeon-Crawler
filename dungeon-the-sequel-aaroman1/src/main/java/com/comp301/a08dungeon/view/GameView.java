package com.comp301.a08dungeon.view;

import com.comp301.a08dungeon.controller.Controller;
import com.comp301.a08dungeon.model.Model;
import com.comp301.a08dungeon.model.board.Posn;
import com.comp301.a08dungeon.model.pieces.Piece;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;

public class GameView implements FXComponent {
    private final Controller controller;
    private final Model model;

    public GameView(Controller controller, Model model) {
        this.controller = controller;
        this.model = model;
    }

    @Override
    public Parent render() {
        StackPane root = new StackPane();

        // Game Grid
        GridPane grid = new GridPane();
        grid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        for (int r = 0; r < model.getHeight(); r++) {
            for (int c = 0; c < model.getWidth(); c++) {
                Posn p = new Posn(r, c);
                Piece piece = model.get(p);
                Region cell = new Region();
                cell.getStyleClass().add("cell");
                if (piece != null) {
                    cell.setStyle("-fx-background-image: url('" + piece.getResourcePath() + "');");
                }
                grid.add(cell, c, r);
                GridPane.setHgrow(cell, Priority.ALWAYS);
                GridPane.setVgrow(cell, Priority.ALWAYS);
            }
        }

        // Info Bar
        HBox infoBar = new HBox();
        infoBar.getStyleClass().add("info-bar");
        infoBar.setAlignment(Pos.CENTER);
        infoBar.setSpacing(30);

        Label scoreLabel = new Label("Score: " + model.getCurScore());
        Label levelLabel = new Label("Level: " + model.getLevel());
        Label highScoreLabel = new Label("High Score: " + model.getHighScore());

        infoBar.getChildren().addAll(scoreLabel, levelLabel, highScoreLabel);

        // Arrow Controls at bottom center
        GridPane arrows = new GridPane();
        arrows.setAlignment(Pos.CENTER);

        Button up = new Button("↑");
        Button down = new Button("↓");
        Button left = new Button("←");
        Button right = new Button("→");

        up.setOnAction(e -> controller.moveUp());
        down.setOnAction(e -> controller.moveDown());
        left.setOnAction(e -> controller.moveLeft());
        right.setOnAction(e -> controller.moveRight());

        arrows.add(up, 1, 0);
        arrows.add(left, 0, 1);
        arrows.add(right, 2, 1);
        arrows.add(down, 1, 2);

        BorderPane overlay = new BorderPane();
        overlay.setTop(infoBar);
        overlay.setBottom(arrows);
        BorderPane.setAlignment(arrows, Pos.CENTER);

        root.getChildren().addAll(grid, overlay);

        return root;
    }
}