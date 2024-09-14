package com.ferra13671.BThack.api.Language;

import com.ferra13671.BThack.Core.FileSystem.ConfigInit.ConfigUtils;
import com.ferra13671.BThack.impl.Modules.CLIENT.Language;

import java.io.BufferedReader;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class LanguageSystem {
    private static final ArrayList<TranslatedKeyText> translatedKeyTexts = new ArrayList<>();
    private static final ArrayList<String> loadedLangs = new ArrayList<>();


    public static String translate(String key) {
        String result = getText(key, Language.language.getValue());
        if (result.isEmpty()) {
            result = getText(key, "EN");
        }
        return result;
    }

    public static void loadTranslations(InputStream inputStream, String lang) {
        try {
            BufferedReader reader = ConfigUtils.createBufferedReader(inputStream, StandardCharsets.UTF_8);
            String line;
            line = reader.readLine();
            while (line != null) {
                if (line.startsWith("//") || line.isBlank()) {
                    line = reader.readLine();
                    continue;
                }
                CharSequence chars = line;
                TranslatedKeyText keyText = getTranslatedKeyText(lang, chars, line);
                translatedKeyTexts.add(keyText);
                line = reader.readLine();
            }
            loadedLangs.add(lang);
            inputStream.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static TranslatedKeyText getTranslatedKeyText(String lang, CharSequence chars, String line) {
        StringBuilder key = new StringBuilder();
        String text;
        boolean check = false;
        for (int i = 0; i < chars.length() - 1; i++) {
            char symbol = chars.charAt(i);
            if (symbol == '"') {
                check = !check;
                continue;
            }

            if (check)
                key.append(symbol);
        }
        text = line.replace('"' + key.toString() + '"' + ": ", "");
        return new TranslatedKeyText(key.toString(), text, lang);
    }

    private static String getText(String key, String lang) {
        for (TranslatedKeyText keyText : translatedKeyTexts) {
            if (keyText.key().equals(key) && keyText.lang().equals(lang))
                return keyText.text();
        }
        return "";
    }

    public static boolean isLanguageLoaded(String lang) {
        return loadedLangs.contains(lang);
    }

    public static List<String> getLoadedLangs() {
        return loadedLangs;
    }

    public static List<TranslatedKeyText> getAllTranslations() {
        return translatedKeyTexts;
    }
}
