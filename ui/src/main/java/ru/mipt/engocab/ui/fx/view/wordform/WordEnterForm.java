package ru.mipt.engocab.ui.fx.view.wordform;

import javafx.stage.Stage;
import ru.mipt.engocab.ui.fx.controller.wordform.WordEnterController;

/**
 * Form for entering a word.
 *
 * @author Alexander V. Ushakov
 */
public class WordEnterForm {

    private Stage stage;
    private WordForm form;

    private WordEnterController enterController;

    public WordEnterForm(WordEnterController enterController) {
        this.enterController = enterController;

        stage = new Stage();
        stage.setTitle("Enter Word Form");

        // render word form on stage
        form = new WordForm(stage);
        this.enterController.setForm(form);
        form.saveButton.setOnAction(enterController::saveWord);
        form.cancelButton.setOnAction(event -> stage.close());
    }

    public void setX(double x) {
        stage.setX(x);
    }

    public void setY(double y) {
        stage.setY(y);
    }

    public void showView() {
        javafx.application.Platform.runLater(stage::show);
    }

}
