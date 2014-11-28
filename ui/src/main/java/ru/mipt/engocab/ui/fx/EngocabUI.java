package ru.mipt.engocab.ui.fx;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.mipt.engocab.core.config.Configuration;
import ru.mipt.engocab.core.config.Settings;
import ru.mipt.engocab.ui.fx.controller.MainController;
import ru.mipt.engocab.ui.fx.model.Model;


/**
 * @author Alexander Ushakov
 */
public class EngocabUI extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Configuration configuration = new Configuration();
        Settings settings = configuration.load();

        Model model = new Model();
        MainController mainController = new MainController(model, primaryStage, settings);
        mainController.loadDictionary();
        MainStage mainStage = new MainStage(primaryStage, model, mainController);
        mainStage.show();
    }

    public void showUI() {
        launch();
    }
}
