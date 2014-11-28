package ru.mipt.engocab.ui.fx.view;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import ru.mipt.engocab.ui.fx.model.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Alexander Ushakov
 */
public class StatisticsStage {

    private final Model model;
    private final Stage stage;

    public StatisticsStage(Model model) {
        this.model = model;
        this.stage = new Stage();
    }

    public void init() {
        GridPane grid = new GridPane();

        int rowIndex = 0;

        // row 0

        Label numberLabel = new Label("Words Number : ");
        Label numberValue = new Label(String.valueOf(model.getDictionary().getContainers().size()));
        grid.add(numberLabel, 0, rowIndex);
        grid.add(numberValue, 1, rowIndex);

        // row 1
        rowIndex++;

        Label activeLabel = new Label("Active cards : ");
        Label activeValue = new Label(String.valueOf(model.getCards().getActive().size()));
        grid.add(activeLabel, 0, rowIndex);
        grid.add(activeValue, 1, rowIndex);

        // row 2
        rowIndex++;

        Label learntLabel = new Label("Learnt cards : ");
        Label learntValue = new Label(String.valueOf(model.getCards().getLearnt().size()));
        grid.add(learntLabel, 0, rowIndex);
        grid.add(learntValue, 1, rowIndex);

        // Tags :

        List<String> tags = new ArrayList<>(model.getTags().keySet());
        Collections.sort(tags);
        for (String tag : tags) {
            rowIndex++;
            Label tagLabel = new Label("Tag " + tag + " : ");
            Label tagCount = new Label(String.valueOf(model.getTags().get(tag)));
            grid.add(tagLabel, 0, rowIndex);
            grid.add(tagCount, 1, rowIndex);
        }

        Scene scene = new Scene(grid, grid.getPrefWidth(), grid.getPrefHeight());
        stage.setResizable(false);
        stage.setScene(scene);

    }

    public void show() {
        javafx.application.Platform.runLater(stage::show);
    }
}
