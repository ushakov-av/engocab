package ru.mipt.engocab.ui.fx;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.mipt.engocab.ui.fx.controller.MainController;
import ru.mipt.engocab.ui.fx.model.Model;

/**
 * @author Alexander Ushakov
 */
public class EngocabApplication extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Model model = new Model();
        MainController mainController = new MainController(model, primaryStage);
        MainStage mainStage = new MainStage(primaryStage, model, mainController);
        mainStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
