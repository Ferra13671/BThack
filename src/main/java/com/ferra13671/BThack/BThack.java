package com.ferra13671.BThack;

import com.ferra13671.BThack.Core.FileSystem.ConfigInit.ConfigInit;
import com.ferra13671.BThack.Core.FileSystem.CreateBThackFolders;
import com.ferra13671.BThack.Events.MegaEvents.EventBus;
import com.ferra13671.BThack.api.ColourThemes.ColourThemeManager;
import com.ferra13671.BThack.api.ColourThemes.ColourThemeUpdate;
import com.ferra13671.BThack.api.Gui.MainMenu.BThackMainMenuScreen;
import com.ferra13671.BThack.api.Gui.ClickGui.ClickGuiScreen;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.Setting.SettingsManager;
import com.ferra13671.BThack.api.Plugin.Plugin;
import com.ferra13671.BThack.api.Plugin.PluginSystem;
import com.ferra13671.BThack.api.Social.Enemies.EnemyList;
import com.ferra13671.BThack.api.Social.Friends.FriendList;
import com.ferra13671.BThack.api.SoundSystem.TinySound;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;
import com.ferra13671.BThack.api.key.KeyHandler;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public final class BThack implements ClientModInitializer, Mc {

    public static final String VERSION = "1.20.4-1.0";
    public static final String APP_ID = "1221431287852826676";

    public static final Logger logger = LoggerFactory.getLogger("BThack");

    public static BThack instance;

    public static final EventBus EVENT_BUS = new EventBus();

    public SettingsManager settingsManager;
    public ClickGuiScreen clickGui;
    public ColourThemeManager colourThemeManager;
    public BThackMainMenuScreen mainMenu;

    public static void log(String message) {
        logger.info(message);
    }

    public static void error(String message) {
        logger.error(message);
    }


    @Override
    public void onInitializeClient() {
        logBThackLogo();

        initLog("BThack initialization has begun. Your nickname: " + MinecraftClient.getInstance().getSession().getUsername());

        KeyboardUtils.initKeyMap();

        PluginSystem.loadPlugins();

        try {
            initLog("Starting to create BThack directory...");
            CreateBThackFolders.start();
            CreateBThackFolders.createTutorialJsonTheme();
            initLog("BThack directory successfully created!");
        } catch (IOException e) {
            initErr("There was an error when creating the BThack directory.");
            throw new RuntimeException(e);
        }

        initLog("Starting initialization of the sound engine...");
        TinySound.init();
        if (TinySound.isInitialized()) {
            initLog("The sound engine has been successfully initialized!");
        } else {
            initErr("The sound engine is not initialized!");
        }

        initLog("Starting loading languages...");
        try {
            ConfigInit.loadLanguages();
        } catch (Exception e) {
            initErr("There was an error loading languages!");
        }

        instance = this;

        settingsManager = new SettingsManager();

        colourThemeManager = new ColourThemeManager();

        EVENT_BUS.register(new ColourThemeUpdate());
        EVENT_BUS.register(new KeyHandler());

        initLog("Starting to upload friends and enemies...");
        try {
            FriendList.loadFriends();
            EnemyList.loadEnemies();
        } catch (Exception e) {
            initErr("There was an error loading friends and enemies!");
            e.printStackTrace();
        }

        PluginSystem.getLoadedPlugins().forEach(Plugin::preInit);
    }


    private void logBThackLogo() {
        log(" ---------------------------------------------------------------------------------------------------------------------------------------- ");
        log("|    ##############        ###################    ####                                                               ####                |");
        log("|    ##############        ###################    ####                                                               ####                |");
        log("|    ####          ####            ####           ####                       ##########            ##########        ####        ####    |");
        log("|    ####          ####            ####           ####    #######            ##########            ##########        ####        ####    |");
        log("|    ##############                ####           ###################                  ####    ####          ####    ####    ####        |");
        log("|    ##############                ####           ########       ####                  ####    ####          ####    ####    ####        |");
        log("|    ####          ####            ####           ####           ####        ##############    ####                  ########            |");
        log("|    ####          ####            ####           ####           ####        ##############    ####                  ########            |");
        log("|    ####          ####            ####           ####           ####    ####          ####    ####          ####    ####    ####        |");
        log("|    ####          ####            ####           ####           ####    ####          ####    ####          ####    ####    ####        |");
        log("|    ##############                ####           ####           ####        ##############        ##########        ####        ####    |");
        log("|    ##############                ####           ####           ####        ##############        ##########        ####        ####    |");
        log(" ---------------------------------------------------------------------------------------------------------------------------------------- ");
    }

    public static void initLog(CharSequence message) {
        String line = " ";
        for (int i = 2; i < message.length(); i++) {
            line = line + "-";
        }

        log(line);
        log(message.toString());
        log(line);
    }

    public static void initErr(CharSequence message) {
        String messageText = "ERROR: " + message;
        String line = " ";
        for (int i = 2; i < messageText.length(); i++) {
            line = line + "-";
        }

        error(line);
        error(message.toString());
        error(line);
    }
}
