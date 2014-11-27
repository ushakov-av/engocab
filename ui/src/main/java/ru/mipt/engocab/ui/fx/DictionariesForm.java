package ru.mipt.engocab.ui.fx;

import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * @author Alexander Ushakov
 */
public class DictionariesForm {

    private Stage stage;

    public DictionariesForm() {

        this.stage = new Stage();

        GridPane grid = new GridPane();

        Scene scene = new Scene(grid, 500, 500);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Dictionaries");
    }

    public void showView() {
        javafx.application.Platform.runLater(stage::show);
    }
}