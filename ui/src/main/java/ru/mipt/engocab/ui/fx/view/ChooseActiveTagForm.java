package ru.mipt.engocab.ui.fx.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.mipt.engocab.ui.fx.model.Model;

/**
 * @author Alexander V. Ushakov
 */
public class ChooseActiveTagForm {

    private final Stage stage;

    public ChooseActiveTagForm(final Model model) {
        this.stage = new Stage();
        final GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        int rowIndex = 0;

        // row 0
        Label tag = new Label("Choose Tag");
        grid.add(tag, 0, rowIndex);

        final TextField field = new TextField();
        grid.add(field, 1, rowIndex);

        // row 1
        rowIndex++;

        Button button = new Button("Set Tag");
        grid.add(button, 1, rowIndex);

        button.setOnAction(actionEvent -> {
            model.setActiveTag(field.getText());
            stage.hide();
        });

        Scene scene = new Scene(grid, grid.getPrefWidth(), grid.getPrefHeight());
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Set Tag");
    }

    public void showView() {
        javafx.application.Platform.runLater(stage::show);
    }
}
