package com.ferra13671.BThack.Core.Client;


import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Category.Category;
import com.ferra13671.BThack.api.Managers.ColourTheme.ColorTheme;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Managers;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Utils.List.BlockList.BlockLists;
import com.ferra13671.BThack.api.Utils.List.ItemList.ItemLists;

import java.text.SimpleDateFormat;
import java.util.*;

public final class Client implements Mc {
    public static final ClientInfo clientInfo = new ClientInfo();

    static final ArrayList<Module> modules = new ArrayList<>();
    public static final ArrayList<HudComponent> hudComponents = new ArrayList<>();

    public static boolean inited = false;

    public static void startup() {
        //Display.setTitle(name);

        InitializeHelper.initCustomCategories();

        ModuleList.initModules();
        BThack.log("All modules have been initialized! Number of modules: " + modules.size());

        InitializeHelper.initHudComponents();

        BlockLists.init();
        ItemLists.init();

        InitializeHelper.initCommands();

        InitializeHelper.initManagers();

        InitializeHelper.initLibraries();

        inited = true;
    }



    public static ArrayList<Module> getModulesInCategory(Category c) {
        ArrayList<Module> mods = new ArrayList<>();
        for (Module m : modules) {
            if (m.getCategory().name().equalsIgnoreCase(c.name())) {
                mods.add(m);
            }
        }
        return mods;
    }

    /**
     * This method should be used as a last resort when it is impossible to get a module via ModuleList.
     * In any other cases, this method will be less productive than obtaining the module directly.
     */
    public static Module getModuleByName(String name) {
        return modules.stream()
                .filter(module -> module.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }


    public static void keyPress(int key) {
        for (Module m : modules) {
            if (m.getKey() == key) {
                m.toggle();
            }
        }
    }


    public static void addCTheme(String name, int frontColour, int backgroundFontColour, int backgroundFontHoveredColour, int moduleEnabledColour, int moduleDisabledColour, int arrayListColour) {
        Managers.COLOR_THEME_MANAGER.addColorTheme(new ColorTheme(name, frontColour, backgroundFontColour, backgroundFontHoveredColour, moduleEnabledColour, moduleDisabledColour, arrayListColour));
    }

    public static String getRealTime(String format) {
        return format.equals("12") ? new SimpleDateFormat("h:mm").format(new Date()) : new SimpleDateFormat("k:mm").format(new Date());
    }

    public static boolean isOptionActivated(Module module, BooleanSetting setting) {
        return module.isEnabled() && setting.getValue();
    }

    public static List<Module> getAllModules() {
        return modules;
    }

    public static List<HudComponent> getAllHudComponents() {
        return hudComponents;
    }
}
