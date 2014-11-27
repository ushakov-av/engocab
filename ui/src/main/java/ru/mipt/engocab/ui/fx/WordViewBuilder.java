package ru.mipt.engocab.ui.fx;

import ru.mipt.engocab.core.model.PartOfSpeech;

/**
 * @author Alexander V. Ushakov
 */
public class WordViewBuilder {

    public final static String HEADER = "<!DOCTYPE html>\n" +
            "<html>\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <title></title>\n" +
            "    <style>\n" +
            "        .par {\n" +
            "            margin-left: 10px;\n" +
            "        }\n" +
            "        .en-word-main {\n" +
            "            font-weight: bold;\n" +
            "        }\n" +
            "        .en-word {\n" +
            "            color: blue;\n" +
            "            font-weight: bold;\n" +
            "        }\n" +
            "        .num-latin {\n" +
            "            font-weight: bold;\n" +
            "        }\n" +
            "        .num-arab {\n" +
            "            font-weight: bold;\n" +
            "        }\n" +
            "        .tag {\n" +
            "            color: red;\n" +
            "            font-weight: bold;\n" +
            "        }\n" +
            "        .grammar {\n" +
            "            color: green;\n" +
            "            font-style: italic;\n" +
            "        }\n" +
            "        .example {\n" +
            "            margin-left: 30px;\n" +
            "            color: grey;\n" +
            "        }\n" +
            "        .phrase {\n" +
            "            margin-left: 20px;\n" +
            "            color: red;\n" +
            "        }\n" +
            "        .phrase-underscore {\n" +
            "            text-decoration: underline;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<table>\n";

    public static final String FOOTER =
            "</table>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>";

    public static String createMainWord(String enWord) {
        return  "    <tr>\n" +
                "        <td>\n" +
                "            <span class=\"en-word-main\">" + enWord + "</span>\n" +
                "        </td>\n" +
                "    </tr>\n";
    }

    public static String createSubMainWord(int num, String enWord, String transcription, PartOfSpeech pos) {
        return  "    <tr>\n" +
                "        <td>\n" +
                "            <span>\n" +
                ((num == 0) ? "" : "<span class=\"num-latin\">" + getLatinNumber(num) + ".</span>&nbsp;") +
                "<span class=\"en-word\">" + enWord + "</span>\n" +
                "                <span class=\"tag\">S2&nbsp;W2&nbsp;AC&nbsp;</span><span>/" + transcription + "/</span>\n" +
                "                <span class=\"grammar\">BrE&nbsp;AmE&nbsp;</span><span class=\"grammar\">" + PartOfSpeech.value(pos) + "</span>\n" +
                "            </span>\n" +
                "        </td>\n" +
                "    </tr>\n";
    }

    private static String getLatinNumber(int num) {
        switch (num) {
            case 1: return "I";
            case 2: return "II";
            case 3: return "III";
            case 4: return "IV";
            case 5: return "V";
            default: throw new RuntimeException("");
        }
    }

    public static String createTranslation(int number, String translation, String grammar) {
        return  "    <tr>\n" +
                "        <td>\n" +
                "            <span class=\"par\">\n" +
                ((number == 0) ? "" : "<span class=\"num-arab\">" + number + ".</span>&nbsp;\n")
                 +
                "                <span class=\"grammar\">" + ((grammar != null) ? grammar : "") + "&nbsp;</span>\n" +
                "                <span>" + translation + "</span>\n" +
                "            </span>\n" +
                "        </td>\n" +
                "    </tr>\n";
    }

    public static String createPhrase(String phrase) {
        return  "    <tr>\n" +
                "        <td>\n" +
                "            <span class=\"phrase\">" + phrase + "</span>\n" +
                "        </td>\n" +
                "    </tr>\n";
    }

    public static String createExample(String example) {
        return  "    <tr>\n" +
                "        <td>\n" +
                "            <span class=\"example\">" + example + "</span>\n" +
                "        </td>\n" +
                "    </tr>\n";
    }
}
