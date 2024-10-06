package com.ferra13671.BThack.api.Managers.Setting.Settings;

import com.ferra13671.BThack.api.Module.Module;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ModeSetting extends Setting {

    private ArrayList<String> options;
    private String modeVal;
    private int modeIndex;


    public ModeSetting(String name, Module module, ArrayList<String> options, Supplier<Boolean> dependence) {
        super(name, module, dependence);
        if (options.isEmpty())
            options.add("NULL");
        this.options = options;
        modeVal = options.get(0);
        modeIndex = 0;
    }

    public ModeSetting(String name, Module module, ArrayList<String> options) {
        this(name, module, options, null);
    }

    public ArrayList<String> getOptions(){
        return this.options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public String getValue(){
        return modeVal;
    }

    public void setValue(String value){
        modeVal = value;
    }

    public int getIndex() {
        return this.modeIndex;
    }

    public void setIndex(int index) {
        this.modeIndex = index;
    }

    @Override
    public void toDefault() {
        modeIndex = 0;
        modeVal = options.get(0);
    }
}
