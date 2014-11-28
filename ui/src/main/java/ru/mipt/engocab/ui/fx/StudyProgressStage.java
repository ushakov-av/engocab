package ru.mipt.engocab.ui.fx;

import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import ru.mipt.engocab.ui.fx.model.Model;

import java.util.Map;

/**
 * Stage to show study progress.
 *
 * @author Alexander Ushakov
 */
public class StudyProgressStage {

    private final Model model;
    private final Stage stage;

    public StudyProgressStage(Model model) {
        this.model = model;
        this.stage = new Stage();
    }

    @SuppressWarnings("unchecked")
    public void init() {

        //todo: listener on model
        Map<Integer, Integer> reverseStatistics = model.getReverseLearntStatistics();
        stage.setTitle("Words Study Progress");

        final CategoryAxis xAxis = new CategoryAxis();
        xAxis.setLabel("Percent Learnt");

        final NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Words Number");

        final BarChart<String, Number> bc = new BarChart<>(xAxis, yAxis);
        bc.setTitle("Words Study Progress");

        XYChart.Series series = new XYChart.Series();
        for (Map.Entry<Integer, Integer> entry : reverseStatistics.entrySet()) {
            series.getData().add(new XYChart.Data(String.valueOf(entry.getKey()), entry.getValue()));
        }
        bc.getData().addAll(series);

        Scene scene = new Scene(bc, 800, 600);
        stage.setScene(scene);
    }

    public void show() {
        javafx.application.Platform.runLater(stage::show);
    }

}
