package com.ferra13671.BThack;

import com.ferra13671.BTbot.api.Utils.Controller.ClientPlayerController;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigSystem;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigUtils;
import com.ferra13671.BThack.Core.FileSystem.FileSystem;
import com.ferra13671.BThack.api.Gui.MainMenu.BThackMainMenuScreen;
import com.ferra13671.BThack.api.Gui.ClickGui.ClickGuiScreen;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Plugin.Plugin;
import com.ferra13671.BThack.api.Plugin.PluginSystem;
import com.ferra13671.BThack.api.Social.SocialManager;
import com.ferra13671.BThack.api.Social.SocialManagers;
import com.ferra13671.BThack.api.SoundSystem.Sounds;
import com.ferra13671.BThack.api.SoundSystem.yaw.TinySound;
import com.ferra13671.BThack.Core.Client.KeyHandler;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.MegaEvents.Base.IEventBus;
import com.ferra13671.MegaEvents.Base.UpdatedEventBus;
import com.google.gson.JsonPrimitive;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;

public final class BThack implements ClientModInitializer, Mc {

    public final String MC_VERSION;
    public final String VERSION;
    public static final String APP_ID = "1221431287852826676";

    public static BThack instance;

    public static final Logger logger = LoggerFactory.getLogger("BThack");
    public static final IEventBus EVENT_BUS = new UpdatedEventBus();

    public ClickGuiScreen clickGui;
    public BThackMainMenuScreen mainMenu;
    public final ClientPlayerController playerController = new ClientPlayerController();
    public final VersionInfo versionInfo = new VersionInfo();

    public BThack() {
        ModMetadata mod = FabricLoader.getInstance().getModContainer("bthack").get().getMetadata();
        MC_VERSION = mod.getCustomValue("mcVersion").getAsString();
        VERSION = mod.getVersion().getFriendlyString();
    }

    public static void log(String message) {
        logger.info(message);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static boolean isBaritonePresent() {
        FabricLoader fl = FabricLoader.getInstance();
        return fl.getModContainer("baritone").isPresent() || fl.getModContainer("baritone-meteor").isPresent();
    }

    @Override
    public void onInitializeClient() {
        logBThackLogo();

        initLog("BThack initialization has begun. Your nickname: " + mc.getSession().getUsername());

        PluginSystem.loadPlugins();

        try {
            initLog("Starting to create BThack directory...");
            FileSystem.start();
            FileSystem.createTutorialJsonTheme();
            initLog("BThack directory successfully created!");
        } catch (IOException e) {
            initErr("There was an error when creating the BThack directory.");
            throw new RuntimeException(e);
        }

        initLog("Starting initialization of the sound engine...");
        TinySound.init();
        Sounds.initSounds();
        if (TinySound.isInitialized()) {
            initLog("The sound engine has been successfully initialized!");
        } else {
            initErr("The sound engine is not initialized!");
        }

        initLog("Starting loading languages...");
        try {
            ConfigSystem.loadLanguages();
        } catch (Exception e) {
            initErr("There was an error loading languages!");
        }

        initLog("Starting loading ActionBot tasks...");
        try {
            ActionBotConfig.loadActionBotTasksData();
        } catch (Exception e) {
            initErr("There was an error loading ActionBot tasks!");
        }

        instance = this;

        EVENT_BUS.register(new KeyHandler());

        initLog("Starting to upload social info...");
        try {
            for (Field field : SocialManagers.class.getFields()) {
                if ( Modifier.isStatic(field.getModifiers())
                        && Modifier.isPublic(field.getModifiers())
                        && Modifier.isFinal(field.getModifiers())
                        && field.getType().equals(SocialManager.class)
                ) {
                    SocialManager socialManager = (SocialManager) field.get(null);
                    socialManager.load();
                }
            }
        } catch (Exception e) {
            initErr("There was an error loading social info!");
            e.printStackTrace(); //Okay
        }

        checkForOutdate();
        loadVersionInfo();

        PluginSystem.getLoadedPlugins().forEach(Plugin::preInit);
    }

    private void checkForOutdate() {
        try {
            String text = new BufferedReader(new InputStreamReader(new URL("https://raw.githubusercontent.com/Ferra13671/BThack/" + MC_VERSION + "/currentVersion.txt").openStream())).readLine();
            if (!text.equals(VERSION)) {
                versionInfo.setOutdated(true);
                versionInfo.setNewVersion(text);
            }
        } catch (Exception ignored) {
            error("Failed getting information on the current release.");
        }
    }

    private void loadVersionInfo() {
        try {
            ConfigUtils.loadFromJson("VersionInfo", "", jsonObject -> {
                if (jsonObject.get("lastCheckVersion") != null && jsonObject.get("needShowAgainOneRelease") != null && jsonObject.get("needShowAgainAllReleases") != null) {
                    if (!jsonObject.get("lastCheckVersion").getAsString().equals(versionInfo.getNewVersion())) {
                        versionInfo.setNeedShowAgainOneRelease(true);
                    } else {
                        versionInfo.setNewVersion(jsonObject.get("lastCheckVersion").getAsString());
                        versionInfo.setNeedShowAgainOneRelease(jsonObject.get("needShowAgainOneRelease").getAsBoolean());
                    }
                    versionInfo.setNeedShowAgainAllReleases(jsonObject.get("needShowAgainAllReleases").getAsBoolean());
                }
            }, () -> {});
        } catch (IOException ignored) {}
    }

    public void saveVersionInfo() {
        try {
            ConfigUtils.saveInJson("VersionInfo", "", jsonObject -> {
                jsonObject.add("lastCheckVersion", new JsonPrimitive(versionInfo.getNewVersion()));
                jsonObject.add("needShowAgainOneRelease", new JsonPrimitive(versionInfo.isNeedShowAgainOneRelease()));
                jsonObject.add("needShowAgainAllReleases", new JsonPrimitive(versionInfo.isNeedShowAgainAllReleases()));
            });
        } catch (IOException ignored) {}
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
