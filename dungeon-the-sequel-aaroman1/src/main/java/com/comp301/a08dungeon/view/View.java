package com.comp301.a08dungeon.view;

import com.comp301.a08dungeon.controller.Controller;
import com.comp301.a08dungeon.model.Model;
import com.comp301.a08dungeon.model.Observer;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class View implements FXComponent, Observer {
  private final Controller controller;
  private final Model model;
  private final int width, height;
  private final Stage stage;

  public View(Controller controller, Model model, Stage stage, int width, int height) {
    this.controller = controller;
    this.model = model;
    this.stage = stage;
    this.width = width;
    this.height = height;
  }

  @Override
  public Parent render() {
    StackPane root = new StackPane();
    if (model.getStatus() == Model.STATUS.END_GAME) {
      root.getChildren().add(new TitleScreenView(controller, model).render());
    } else {
      root.getChildren().add(new GameView(controller, model).render());
    }
    return root;
  }

  @Override
  public void update() {
    Platform.runLater(() -> {
      Parent newRoot = render();
      stage.getScene().setRoot(newRoot);
    });
  }
}