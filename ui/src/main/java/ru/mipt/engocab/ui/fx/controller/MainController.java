package ru.mipt.engocab.ui.fx.controller;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import ru.mipt.engocab.core.config.Settings;
import ru.mipt.engocab.core.model.Dictionary;
import ru.mipt.engocab.core.model.study.Cards;
import ru.mipt.engocab.core.model.study.Index;
import ru.mipt.engocab.data.json.DataMapper;
import ru.mipt.engocab.ui.fx.controller.wordform.WordEnterController;
import ru.mipt.engocab.ui.fx.view.CheckForm;
import ru.mipt.engocab.ui.fx.view.ChooseActiveTagForm;
import ru.mipt.engocab.ui.fx.view.DictionariesForm;
import ru.mipt.engocab.ui.fx.view.LearnForm;
import ru.mipt.engocab.ui.fx.view.OptionsForm;
import ru.mipt.engocab.ui.fx.view.StatisticsStage;
import ru.mipt.engocab.ui.fx.view.StudyProgressStage;
import ru.mipt.engocab.ui.fx.view.wordform.WordEditForm;
import ru.mipt.engocab.ui.fx.view.wordform.WordEnterForm;
import ru.mipt.engocab.ui.fx.model.Lesson;
import ru.mipt.engocab.ui.fx.model.Model;
import ru.mipt.engocab.ui.fx.model.ModelWordRecord;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Alexander Ushakov
 */
public class MainController {

    private Stage primaryStage;

    private FileChooser fileChooser = new FileChooser();

    private final Model model;

    private File currentDictionaryFile;

    private Settings settings;


    private final ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(1);

    public MainController(Model model, Stage primaryStage, Settings settings) {
        this.model = model;
        this.primaryStage = primaryStage;
        this.settings = settings;
    }

    public void stopScheduler(Event event) {
        scheduler.shutdownNow();
    }

    public void saveDictionary(Event event) {
        if (currentDictionaryFile == null) {
            fileChooser.setTitle("Save as...");
            currentDictionaryFile = fileChooser.showOpenDialog(primaryStage);
            if (currentDictionaryFile != null) {
                fileChooser.setInitialDirectory(currentDictionaryFile.getParentFile());
            } else {
                return;
            }
        }

        saveDictionaryFiles(currentDictionaryFile);

        final Stage savePopup = new Stage();

        VBox popupPane = new VBox(10);
        popupPane.setMinSize(100, 100);
        popupPane.setPadding(new Insets(10, 10, 10, 10));
        Text text = new Text("Saved!");
        text.setFont(Font.font("Tahoma", FontWeight.NORMAL, 24));
        popupPane.getChildren().addAll(text);

        Scene scene = new Scene(popupPane, popupPane.getPrefWidth(), popupPane.getPrefHeight());
        savePopup.setResizable(false);
        savePopup.setScene(scene);

        javafx.application.Platform.runLater(() -> {
            savePopup.show();
            scheduler.schedule(() -> javafx.application.Platform.runLater(savePopup::close), 2, TimeUnit.SECONDS);
        });

    }

    public void loadDictionary() {
        String path = settings.getDictionariesPath();
        String currentDictionary = settings.getCurrentDictionary();
        if (Strings.isNullOrEmpty(path) || Strings.isNullOrEmpty(currentDictionary)) {
            return;
        }
        String currentDictionaryPath = path + File.separator + currentDictionary;
        File dictionaryFile = new File(currentDictionaryPath + File.separator + currentDictionary + ".json");
        File cardsFile = new File(currentDictionaryPath + File.separator + currentDictionary + "_cards.dat");
        File indexFile = new File(currentDictionaryPath + File.separator + currentDictionary + "_index.dat");
        if (dictionaryFile.exists() && cardsFile.exists() && indexFile.exists()) {
            currentDictionaryFile = dictionaryFile;
            fileChooser.setInitialDirectory(currentDictionaryFile.getParentFile());
            loadDictionaryFiles(dictionaryFile, cardsFile, indexFile);
        }
    }

    public void showOpenWindow(Event event) {
        fileChooser.setTitle("Open dictionary...");
        currentDictionaryFile = fileChooser.showOpenDialog(primaryStage);
        if (currentDictionaryFile != null) {
            System.out.println(currentDictionaryFile.getAbsolutePath());
            String fileName = currentDictionaryFile.getName();
            int i = fileName.lastIndexOf('.');
            String prefix = fileName.substring(0, i);
            fileChooser.setInitialDirectory(currentDictionaryFile.getParentFile());
            File directoryFile = currentDictionaryFile.getParentFile();
            String directoryPath = directoryFile.getPath();
            File cardsFile = new File(directoryPath + File.separator + prefix + "_cards.dat");
            File indexFile = new File(directoryPath + File.separator + prefix + "_index.dat");
            loadDictionaryFiles(currentDictionaryFile, cardsFile, indexFile);
        }
    }

    private void loadDictionaryFiles(File dictionaryFile, File cardsFile, File indexFile) {
        Preconditions.checkArgument(dictionaryFile.exists() && cardsFile.exists() && indexFile.exists(),
                "Dictionary Files should exist");
        try {
            Dictionary dictionary = DataMapper.deserializeFromStream(new FileInputStream(dictionaryFile), Dictionary.class);
            Cards cards = DataMapper.deserializeFromStream(new FileInputStream(cardsFile), Cards.class);
            Index index = DataMapper.deserializeFromStream(new FileInputStream(indexFile), Index.class);
            model.updateModel(dictionary, cards, index);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void showSaveAsWindow(Event event) {
        fileChooser.setTitle("Save as...");
        File chosenFile = fileChooser.showOpenDialog(primaryStage);
        if (chosenFile != null) {
            currentDictionaryFile = chosenFile;
        } else {
            return;
        }
        fileChooser.setInitialDirectory(currentDictionaryFile.getParentFile());
        saveDictionaryFiles(currentDictionaryFile);
    }

    public void showStudyProgress(Event event) {
        StudyProgressStage popup = new StudyProgressStage(model);
        popup.init();
        popup.show();
    }

    public void showStatistics(Event event) {
        StatisticsStage popup = new StatisticsStage(model);
        popup.init();
        popup.show();
    }

    public void showOptions(Event event) {
        OptionsForm form = new OptionsForm();
        form.init();
        form.show();
    }

    public void showDictionaries(Event event) {
        DictionariesForm form = new DictionariesForm();
        form.show();
    }

    public void showWordEnterForm(Event event) {
        WordEnterForm enterForm = new WordEnterForm(new WordEnterController(model));
        enterForm.setX(primaryStage.getX());
        enterForm.setY(primaryStage.getY());
        enterForm.showView();
    }

    public void showWordEditForm(MouseEvent t) {
        if (t.getClickCount() > 1) {
            TableCell c = (TableCell) t.getSource();
            int index = c.getIndex();
            ObservableList<ModelWordRecord> data = model.getData();
            if (data.size() > index) {
                WordEditForm form = new WordEditForm(model, index);
                form.setX(primaryStage.getX());
                form.setY(primaryStage.getY());
                form.showView();
            }
        }
    }

    public void showLearningStage(Event event) {
        Lesson lesson = model.createLearnLesson(model.getActiveTag());
        LearnForm learnForm = new LearnForm(model, lesson);
        learnForm.showView();
    }

    public void showActiveTagForm(Event event) {
        ChooseActiveTagForm activeTagForm = new ChooseActiveTagForm(model);
        activeTagForm.showView();
    }

    public void showDirectLessonForm(Event event) {
        Lesson lesson = model.createCheckLesson(true, model.getActiveTag());
        CheckForm checkForm = new CheckForm(model, lesson, true);
        checkForm.showView();
    }

    public void showReverseLessonForm(Event event) {
        Lesson lesson = model.createCheckLesson(false, model.getActiveTag());
        CheckForm checkForm = new CheckForm(model, lesson, false);
        checkForm.showView();
    }

    private void saveDictionaryFiles(File currentDictionaryFile) {
        if (currentDictionaryFile != null) {
            String fileName = currentDictionaryFile.getName();
            int i = fileName.lastIndexOf('.');
            String prefix = fileName.substring(0, i);
            File directoryFile = currentDictionaryFile.getParentFile();
            String directoryPath = directoryFile.getPath();
            File cardsFile = new File(directoryPath + java.io.File.separator + prefix + "_cards.dat");
            File indexFile = new File(directoryPath + java.io.File.separator + prefix + "_index.dat");
            if (!cardsFile.exists()) {
                try {
                    cardsFile.createNewFile();
                } catch (IOException e) {
                    //
                }
            }
            if (!indexFile.exists()) {
                try {
                    indexFile.createNewFile();
                } catch (IOException e) {
                    //
                }
            }
            String serializedDictionary = DataMapper.serialize(model.getDictionary());
            String serializedCards = DataMapper.serialize(model.getCards());
            String serializedIndex = DataMapper.serialize(model.getIndex());
            writeToFile(currentDictionaryFile, serializedDictionary);
            writeToFile(cardsFile, serializedCards);
            writeToFile(indexFile, serializedIndex);
        }
    }

    private void writeToFile(File file, String serializedDictionary) {
        BufferedWriter out = null;
        try {
            out = new BufferedWriter(new FileWriter(file));
            out.write(serializedDictionary);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    //
                }
            }
        }
    }

}
