package ru.mipt.engocab;

import ru.mipt.engocab.ui.fx.EngocabUI;

import java.io.IOException;

/**
 * @author Alexander Ushakov
 */
public class EngocabApplication {

    public static void main(String[] args) throws IOException {
        EngocabUI ui = new EngocabUI();
        ui.showUI();
    }
}
