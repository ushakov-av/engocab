package ru.mipt.engocab.ui.fx.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * @author Alexander Ushakov
 */
public class OptionsForm {

    private Stage stage;

    public OptionsForm() {
        this.stage = new Stage();
    }

    public void init() {
        TabPane tabPane = new TabPane();
        tabPane.setSide(Side.TOP);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setMaxWidth(Double.MAX_VALUE);

        // General tab
        Tab generalTab = new Tab("General");

        // Exercise tab
        Tab exerciseTab = new Tab("Exercise");

        // Schedule tab
        Tab scheduleTab = createScheduleTab();

        tabPane.getTabs().addAll(generalTab, exerciseTab, scheduleTab);

        Scene scene = new Scene(tabPane, 500, 500);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Options");
    }

    public void show() {
        javafx.application.Platform.runLater(stage::show);
    }

    private Tab createScheduleTab() {

        Tab scheduleTab = new Tab("Schedule");

        GridPane grid = new GridPane();

        int rowIndex = 0;

        HBox enableSchedulingBox = new HBox(5);
        enableSchedulingBox.setPadding(new Insets(10, 0, 0, 10));
        CheckBox enableSchedulingCheckBox = new CheckBox();
        Label enableSchedulingLabel = new Label("Enable scheduling");
        enableSchedulingBox.getChildren().addAll(enableSchedulingCheckBox, enableSchedulingLabel);

        grid.add(enableSchedulingBox, 0, rowIndex, 2, 1);

        rowIndex++;

        ToggleGroup tg = new ToggleGroup();
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 20, 0, 10));
        vbox.setSpacing(5);

        RadioButton rb1 = new RadioButton("During the day");
        rb1.setSelected(false);
        rb1.setToggleGroup(tg);

        RadioButton rb2 = new RadioButton("Daily");
        rb2.setToggleGroup(tg);
        rb2.setSelected(true);

        RadioButton rb3 = new RadioButton("Weekly");
        rb3.setToggleGroup(tg);
        rb3.setSelected(false);

        vbox.getChildren().addAll(rb1, rb2, rb3);
        grid.add(vbox, 0, rowIndex, 1, 1);

        HBox timeBox = new HBox(5);
        timeBox.setAlignment(Pos.CENTER);
        timeBox.setPadding(new Insets(10, 0, 0, 0));
        Label everyTime = new Label("Every");
        TextField everyTimeText = new TextField();
        everyTimeText.setPrefColumnCount(5);
        Text minsText = new Text("mins");
        timeBox.getChildren().addAll(everyTime, everyTimeText, minsText);
        grid.add(timeBox, 1, rowIndex, 1, 1);

        scheduleTab.setContent(grid);

        return scheduleTab;
    }


}
