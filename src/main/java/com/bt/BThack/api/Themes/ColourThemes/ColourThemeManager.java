package com.bt.BThack.api.Themes.ColourThemes;

import java.util.ArrayList;

public class ColourThemeManager {

    private final ArrayList<ColourTheme> colourThemes;

    public ColourThemeManager() {
        this.colourThemes = new ArrayList<>();
    }

    public void addColourTheme(ColourTheme in){
        this.colourThemes.add(in);
    }

    public ArrayList<ColourTheme> getColourThemes(){
        return this.colourThemes;
    }
}
