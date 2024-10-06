package com.ferra13671.BThack.Core.FileSystem.ConfigSystem;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Core.FileSystem.FileSystem;
import com.ferra13671.BThack.api.Gui.ActionBot.ActionBotConfigGui;
import com.ferra13671.BThack.api.Gui.ActionBot.Add.AddingMoveTaskGui;
import com.ferra13671.BThack.api.Gui.ActionBot.Add.AddingSendMessageTask;
import com.ferra13671.BThack.api.Gui.ActionBot.Add.AddingTunnelTaskGui;
import com.ferra13671.BThack.api.Gui.ActionBot.Add.AddingWaitTaskGui;
import com.ferra13671.BThack.api.Gui.ActionBot.Edit.EditMoveTaskGui;
import com.ferra13671.BThack.api.Gui.ActionBot.Edit.EditSendMessageTaskGui;
import com.ferra13671.BThack.api.Gui.ActionBot.Edit.EditTunnelTaskGui;
import com.ferra13671.BThack.api.Gui.ActionBot.Edit.EditWaitTaskGui;
import com.ferra13671.BThack.api.Gui.ClickGui.ClickGuiScreen;
import com.ferra13671.BThack.api.Gui.ClickGui.component.Frame;
import com.ferra13671.BThack.api.Gui.MainMenu.BThackMainMenuScreen;
import com.ferra13671.BThack.api.Gui.MainMenu.SelectWallpaper.SelectWallpaperScreen;
import com.ferra13671.BThack.api.Gui.MainMenu.SelectWallpaper.Wallpaper;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Language.LanguageSystem;
import com.ferra13671.BThack.api.Managers.Setting.Settings.*;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Plugin.Plugin;
import com.ferra13671.BThack.api.Plugin.PluginSystem;
import com.ferra13671.BThack.api.Social.Clans.Allies.Ally;
import com.ferra13671.BThack.api.Social.Clans.Clan;
import com.ferra13671.BThack.api.Social.Clans.ClansUtils;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.api.Utils.Texture.Texture;
import com.ferra13671.BThack.api.Utils.Texture.TextureColorMode;
import com.ferra13671.BThack.api.Utils.PathMode;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks.*;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.ActionBotTaskData;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.TaskButton;
import com.ferra13671.BThack.impl.Modules.RENDER.Search;
import com.ferra13671.BThack.impl.Modules.RENDER.Xray;
import com.google.gson.*;
import net.minecraft.block.Block;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigUtils.*;

public final class ConfigSystem {
    static final Gson gson = (new GsonBuilder()).setPrettyPrinting().create();

    public static void registerFiles(String name, String path) throws IOException {
        Path p = Paths.get("BThack/" + path + "/" + name + ".json");
        if (Files.exists(p)) {
            File file = new File("BThack/" + path + "/" + name + ".json");
            file.delete();
        }

        Files.createFile(p);
    }

    public static void saveConfig() {
        try {
            saveModules();
            saveClickGui();
            saveClans();
            saveXrayBlocks();
            saveSearchBlocks();
            saveActionBotTasks();
        } catch (IOException e) {
            BThack.error(e.getMessage());
        }
    }

    public static void loadConfig() {
        try {
            loadModules();
            loadClans();
            loadXrayBlocks();
            loadSearchBlocks();
            loadWallpaper();
            loadClickGui();
            loadActionBotTasks();
        } catch (IOException e) {
            BThack.error(e.getMessage());
        }
    }

    public static void saveModules() throws IOException {
        for (Module module : Client.getAllModules()) {
            registerFiles(module.getName(), "Modules");
            OutputStreamWriter fileOutputStreamWriter = createWriter(Paths.get("BThack/Modules/" + module.getName() + ".json"));

            JsonObject moduleObject = new JsonObject();
            JsonObject settingObject = new JsonObject();

            moduleObject.add("Name", new JsonPrimitive(module.getName()));
            moduleObject.add("Enabled", new JsonPrimitive(module.isEnabled()));
            moduleObject.add("Bind", new JsonPrimitive(module.getKey()));
            moduleObject.add("Visible", new JsonPrimitive(module.visible));

            if (BThack.instance.settingsManager.getSettingsByMod(module) != null) {
                for (Setting s : BThack.instance.settingsManager.getSettingsByMod(module)) {
                    if (s instanceof ModeSetting set) {
                        settingObject.add(s.getName(), new JsonPrimitive(set.getValue()));
                        settingObject.add(s.getName() + " Index", new JsonPrimitive(set.getIndex()));
                    }

                    if (s instanceof BooleanSetting set) {
                        settingObject.add(s.getName(), new JsonPrimitive(set.getValue()));
                    }

                    if (s instanceof NumberSetting set) {
                        settingObject.add(s.getName(), new JsonPrimitive(set.getValue()));
                    }
                    if (s instanceof KeyCodeSetting set) {
                        settingObject.add(s.getName(), new JsonPrimitive(set.getValue()));
                    }
                }
            }

            moduleObject.add("Settings", settingObject);
            String jsonString = jsonToString(moduleObject);
            fileOutputStreamWriter.write(jsonString);
            fileOutputStreamWriter.close();
        }
    }

    public static void loadModules() throws IOException {
        for (Module module : Client.getAllModules()) {
            if (!Files.exists(Paths.get("BThack/Modules/" + module.getName() + ".json"))) {
                if (module.isAutoEnabled()) {
                    module.setToggled(true);
                }
                continue;
            }

            InputStream inputStream = Files.newInputStream(Paths.get("BThack/Modules/" + module.getName() + ".json"));
            JsonObject moduleObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

            if (moduleObject.get("Name") == null || moduleObject.get("Enabled") == null || moduleObject.get("Bind") == null || moduleObject.get("Visible") == null) continue;

            JsonObject settingObject = moduleObject.get("Settings").getAsJsonObject();

            if (BThack.instance.settingsManager.getSettingsByMod(module) != null) {
                for (Setting s : BThack.instance.settingsManager.getSettingsByMod(module)) {
                    JsonElement settingValueObject;
                    JsonElement IndexObject;

                    settingValueObject = settingObject.get(s.getName());

                    if (settingValueObject != null) {
                        if (s instanceof ModeSetting set) {
                            IndexObject = settingObject.get(s.getName() + " Index");
                            set.setValue(settingValueObject.getAsString());
                            set.setIndex(IndexObject.getAsInt());
                        }

                        if (s instanceof BooleanSetting set) {
                            set.setValue(settingValueObject.getAsBoolean());
                        }

                        if (s instanceof NumberSetting set) {
                            set.setValue(settingValueObject.getAsDouble());
                        }

                        if (s instanceof KeyCodeSetting set) {
                            set.setValue(settingValueObject.getAsInt());
                        }
                    }
                }
            }
            module.setToggled(moduleObject.get("Enabled").getAsBoolean());
            module.setKey(moduleObject.get("Bind").getAsInt());
            module.visible = moduleObject.get("Visible").getAsBoolean();

            inputStream.close();
        }
    }

    public static void saveClickGui() throws IOException {
        registerFiles("ClickGui", "ClickGui");

        OutputStreamWriter fileOutputStreamWriter = createWriter(Paths.get("BThack/ClickGui/ClickGui.json"));

        JsonObject clickGuiObject = new JsonObject();

        for (Frame frame : ClickGuiScreen.frames) {
            JsonObject frameObject = new JsonObject();

            frameObject.add("x", new JsonPrimitive(frame.getX()));
            frameObject.add("y", new JsonPrimitive(frame.getY()));
            frameObject.add("opened", new JsonPrimitive(frame.isOpen()));

            clickGuiObject.add(frame.getCategory().name(), frameObject);
        }

        String jsonString = jsonToString(clickGuiObject);
        fileOutputStreamWriter.write(jsonString);
        fileOutputStreamWriter.close();
    }

    public static void loadClickGui() throws IOException {
        Path path = Paths.get("BThack/ClickGui/ClickGui.json");
        if (Files.exists(path)) {
            InputStream inputStream = Files.newInputStream(path);
            JsonObject clickGuiObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();
            for (Frame frame : ClickGuiScreen.frames) {
                JsonObject settingObject = clickGuiObject.get(frame.getCategory().name()).getAsJsonObject();

                frame.setX(settingObject.get("x").getAsInt());
                frame.setY(settingObject.get("y").getAsInt());
                frame.setOpen(settingObject.get("opened").getAsBoolean());
            }
            inputStream.close();
        }
    }

    public static void saveHudComponents() throws IOException {
        for (HudComponent hudComponent : Client.getAllHudComponents()) {
            registerFiles(hudComponent.getName(), "HudComponents");

            OutputStreamWriter fileOutputStreamWriter = createWriter(Paths.get("BThack/HudComponents/" + hudComponent.getName() + ".json"));

            JsonObject jsonObject = new JsonObject();

            jsonObject.add("Name", new JsonPrimitive(hudComponent.getName()));
            jsonObject.add("X", new JsonPrimitive(hudComponent.getNoScaledX()));
            jsonObject.add("Y", new JsonPrimitive(hudComponent.getNoScaledY()));
            jsonObject.add("ScaledWidth", new JsonPrimitive(hudComponent.getScaledWidth()));
            jsonObject.add("ScaledHeight", new JsonPrimitive(hudComponent.getScaledHeight()));

            String jsonString = jsonToString(jsonObject);
            fileOutputStreamWriter.write(jsonString);
            fileOutputStreamWriter.close();
        }
    }

    public static void loadHudComponents() throws IOException {
        File folder = Paths.get("BThack/HudComponents").toFile();
        File[] files = folder.listFiles();
        if (files == null) return;

        for (File file : files) {
            if (Objects.equals(FilenameUtils.getExtension(file.getName()), "json")) {
                InputStream inputStream = Files.newInputStream(Paths.get("BThack/HudComponents/" + file.getName()));
                JsonObject compObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

                if (compObject.get("Name") != null && compObject.get("X") != null && compObject.get("Y") != null && compObject.get("ScaledWidth") != null && compObject.get("ScaledHeight") != null) {
                    String name = compObject.get("Name").getAsString();
                    for (HudComponent component : Client.getAllHudComponents()) {
                        if (component.getName().equals(name)) {
                            component.setX(compObject.get("X").getAsFloat(), compObject.get("ScaledWidth").getAsInt());
                            component.setY(compObject.get("Y").getAsFloat(), compObject.get("ScaledHeight").getAsInt());
                        }
                    }
                }
                inputStream.close();
            }
        }
    }

    public static void saveClans() throws IOException {
        FileSystem.deleteDirectory(new File("BThack/Social/Clans"));
        FileSystem.registerFolder("Clans", "/Social");
        for (Clan clan : ClansUtils.clans) {
            registerFiles(clan.getName(), "Social/Clans");

            OutputStreamWriter fileOutputStreamWriter = createWriter(Paths.get("BThack/Social/Clans/" + clan.getName() + ".json"));

            JsonObject clansObject = new JsonObject();

            clansObject.add("ClanName", new JsonPrimitive(clan.getName()));
            clansObject.add("R", new JsonPrimitive(clan.getR()));
            clansObject.add("G", new JsonPrimitive(clan.getG()));
            clansObject.add("B", new JsonPrimitive(clan.getB()));

            String jsonString = jsonToString(clansObject);
            fileOutputStreamWriter.write(jsonString);
            fileOutputStreamWriter.close();

            FileSystem.registerFolder(clan.getName() + "_Members", "/Social/Clans");

            for (Ally ally : clan.members) {
                registerFiles(ally.name() + "_Member", "Social/Clans/" + clan.getName() + "_Members");

                OutputStreamWriter allyOutputStreamWriter = createWriter(Paths.get("BThack/Social/Clans/" + clan.getName() + "_Members/" + ally.name() + "_Member" + ".json"));

                JsonObject allyObject = new JsonObject();

                allyObject.add("name", new JsonPrimitive(ally.name()));

                String allyString = jsonToString(allyObject);
                allyOutputStreamWriter.write(allyString);
                allyOutputStreamWriter.close();
            }
        }

        ClansUtils.reloadClanListOnModules();
    }

    public static void loadClans() throws IOException {
        ClansUtils.clans.clear();
        File folder = new File(Paths.get("BThack/Social/Clans").toUri());
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (Objects.equals(FilenameUtils.getExtension(file.getName()), "json")) {
                        String name = file.getName();
                        InputStream inputStream = Files.newInputStream(Paths.get("BThack/Social/Clans/" + name));
                        JsonObject clanOject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

                        if (clanOject.get("ClanName") != null && clanOject.get("R") != null && clanOject.get("G") != null && clanOject.get("B") != null) {
                            String clanName = clanOject.get("ClanName").getAsString();
                            float r = clanOject.get("R").getAsFloat();
                            float g = clanOject.get("G").getAsFloat();
                            float b = clanOject.get("B").getAsFloat();

                            Clan clan = new Clan(clanName, r, g, b);

                            File clanMembersFolder = new File(Paths.get("BThack/Social/Clans/" + clan.getName() + "_Members").toUri());
                            File[] clanMembers = clanMembersFolder.listFiles();

                            if (clanMembers != null) {
                                for (File memberFile : clanMembers) {
                                    if (memberFile.isFile()) {
                                        if (Objects.equals(FilenameUtils.getExtension(memberFile.getName()), "json")) {
                                            String memberName = memberFile.getName();
                                            InputStream memberInputStream = Files.newInputStream(Paths.get("BThack/Social/Clans/" + clan.getName() + "_Members/" + memberName));
                                            JsonObject memberObject = JsonParser.parseReader(new InputStreamReader(memberInputStream)).getAsJsonObject();

                                            if (memberObject.get("name") != null) {
                                                String member = memberObject.get("name").getAsString();
                                                clan.members.add(new Ally(member));
                                            }

                                            memberInputStream.close();
                                        }
                                    }
                                }
                            }

                            ClansUtils.clans.add(clan);
                        }
                        inputStream.close();
                    }
                }
            }
        }

        ClansUtils.reloadClanListOnModules();
    }

    public static void saveXrayBlocks() throws IOException {
        Path savePath = Paths.get("BThack/Xray/XrayBlocks.txt");

        if (Files.exists(savePath)) {
            File file = savePath.toFile();
            file.delete();
            FileSystem.registerFile("XrayBlocks", "Xray", "txt");
        }

        BufferedWriter writer = Files.newBufferedWriter(savePath, StandardCharsets.UTF_8);
        for (String blockName : Xray.xrayBlockNames) {
            writer.write(blockName + System.lineSeparator());
        }
        writer.close();
    }

    public static void loadXrayBlocks() throws IOException {
        Path savePath = Paths.get("BThack/Xray/XrayBlocks.txt");

        if (!Files.exists(savePath)) {
            FileSystem.registerFile("XrayBlocks", "Xray", "txt");
            return;
        }

        BufferedReader reader = Files.newBufferedReader(savePath, StandardCharsets.UTF_8);
        String line = reader.readLine();
        while (line != null) {
            Block block = BlockUtils.getBlockFromNameOrID(line);
            if (block != null) {
                Xray.xrayBlocks.add(block);
                Xray.xrayBlockNames.add(line);
            }
            line = reader.readLine();
        }
        reader.close();
    }

    public static void loadSearchBlocks() throws IOException {
        Path savePath = Paths.get("BThack/Search/SearchBlocks.txt");

        if (!Files.exists(savePath)) {
            FileSystem.registerFile("SearchBlocks", "Search", "txt");
            return;
        }

        BufferedReader reader = Files.newBufferedReader(savePath, StandardCharsets.UTF_8);
        String line = reader.readLine();
        while (line != null) {
            Block block = BlockUtils.getBlockFromNameOrID(line);
            if (block != null) {
                Client.blockSearchManager.addBlockToSearch(block);
                Search.searchBlockNames.add(line);
            }
            line = reader.readLine();
        }
        reader.close();
    }

    public static void saveSearchBlocks() throws IOException {
        Path savePath = Paths.get("BThack/Search/SearchBlocks.txt");

        if (Files.exists(savePath)) {
            File file = savePath.toFile();
            file.delete();
            FileSystem.registerFile("SearchBlocks", "Search", "txt");
        }

        BufferedWriter writer = Files.newBufferedWriter(savePath, StandardCharsets.UTF_8);
        for (String blockName : Search.searchBlockNames) {
            writer.write(blockName + System.lineSeparator());
        }
        writer.close();
    }


    public static void saveActionBotTasks() throws IOException {
        Path configInfoFile = Paths.get("BThack/ActionBot/ConfigInfo.txt");

        BufferedWriter writer = Files.newBufferedWriter(configInfoFile, StandardCharsets.UTF_8);
        ArrayList<ActionBotTask> tasks = new ArrayList<>(ActionBotConfig.tasks);
        tasks.remove(ActionBotConfig.startTask);
        tasks.remove(ActionBotConfig.endTask);

        int taskNumber = 1;

        boolean m = false;

        for (ActionBotTask task : tasks) {
            String fileName = taskNumber + ". " + task.getName();

            registerFiles(fileName, "ActionBot/DefaultConfig");

            OutputStreamWriter fileOutputStreamWriter = new OutputStreamWriter(Files.newOutputStream(Paths.get("BThack/ActionBot/DefaultConfig/" + fileName + ".json")), StandardCharsets.UTF_8);

            JsonObject taskObject = new JsonObject();

            if (task instanceof MoveTask moveTask) {

                String type = switch (moveTask.getType()) {
                    case Default -> "Default";
                    case AutoJump -> "With AutoJump";
                    default -> "Through Obstacles";
                };

                taskObject.add("Mode", new JsonPrimitive(moveTask.mode));

                taskObject.add("NeedX", new JsonPrimitive(moveTask.getNeedX()));
                taskObject.add("NeedZ", new JsonPrimitive(moveTask.getNeedZ()));
                taskObject.add("Scaffold", new JsonPrimitive(moveTask.isScaffold()));
                taskObject.add("Type", new JsonPrimitive(type));
            } else
            if (task instanceof SendMessageTask sendMessageTask) {

                taskObject.add("Mode", new JsonPrimitive(sendMessageTask.mode));

                taskObject.add("Message", new JsonPrimitive(sendMessageTask.getMessage()));
            } else
            if (task instanceof JumpTask jumpTask) {

                taskObject.add("Mode", new JsonPrimitive(jumpTask.mode));
            } else
            if (task instanceof WaitTask waitTask) {

                taskObject.add("Mode", new JsonPrimitive(waitTask.mode));

                taskObject.add("Seconds", new JsonPrimitive(waitTask.getSeconds()));
            } else
            if (task instanceof TunnelTask tunnelTask) {

                String direction = switch (tunnelTask.getDirection()) {
                    case X_PLUS -> "X+";
                    case X_MINUS -> "X-";
                    case Z_PLUS -> "Z+";
                    default -> "Z-";
                };

                taskObject.add("Mode", new JsonPrimitive(tunnelTask.mode));

                taskObject.add("Direction", new JsonPrimitive(direction));
                taskObject.add("Length", new JsonPrimitive(tunnelTask.getLength()));
            }



            if (!m) {
                writer.write(fileName);
                m = true;
            } else {
                writer.write(System.lineSeparator() + fileName);
            }

            taskNumber++;

            String jsonString = gson.toJson(JsonParser.parseString(taskObject.toString()));
            fileOutputStreamWriter.write(jsonString);
            fileOutputStreamWriter.close();
        }

        writer.close();
    }

    public static void loadActionBotTasks() throws IOException {
        Path configInfoFile = Paths.get("BThack/ActionBot/ConfigInfo.txt");

        if (Files.exists(configInfoFile)) {
            BufferedReader readerConfigInfo = Files.newBufferedReader(configInfoFile, StandardCharsets.UTF_8);
            String line = readerConfigInfo.readLine();

            ArrayList<String> tasksNames = new ArrayList<>();

            ArrayList<ActionBotTask> tasks = new ArrayList<>();

            while (line != null) {
                tasksNames.add(line);
                line = readerConfigInfo.readLine();
            }

            readerConfigInfo.close();

            for (String taskName : tasksNames) {
                Path taskPath = Paths.get("BThack/ActionBot/DefaultConfig/" + taskName + ".json");

                if (Files.exists(taskPath)) {
                    InputStream inputStream = Files.newInputStream(taskPath);

                    JsonObject taskObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

                    if (taskObject.get("Mode") != null) {
                        String mode = taskObject.get("Mode").getAsString();

                        if (ActionBotTask.isJumpTask(mode)) {
                            tasks.add(new JumpTask());
                        }
                        if (ActionBotTask.isMoveTask(mode)) {
                            if (taskObject.get("NeedX") != null && taskObject.get("NeedZ") != null && taskObject.get("Scaffold") != null && taskObject.get("Type") != null) {
                                double needX = taskObject.get("NeedX").getAsDouble();
                                double needZ = taskObject.get("NeedZ").getAsDouble();
                                boolean scaffold = taskObject.get("Scaffold").getAsBoolean();
                                String type = taskObject.get("Type").getAsString();

                                MoveTask.Type moveType = type.equals("Default") ? MoveTask.Type.Default : type.equals("With AutoJump") ? MoveTask.Type.AutoJump : MoveTask.Type.Through_Obstacles;

                                tasks.add(new MoveTask(needX, needZ, scaffold, moveType));
                            }
                        }
                        if (ActionBotTask.isTunnelTask(mode)) {
                            if (taskObject.get("Direction") != null && taskObject.get("Length") != null) {
                                String directionString = taskObject.get("Direction").getAsString();
                                double length = taskObject.get("Length").getAsDouble();

                                TunnelTask.Direction direction = switch (directionString) {
                                    case "X+" -> TunnelTask.Direction.X_PLUS;
                                    case "X-" -> TunnelTask.Direction.X_MINUS;
                                    case "Z+" -> TunnelTask.Direction.Z_PLUS;
                                    default -> TunnelTask.Direction.Z_MINUS;
                                };


                                tasks.add(new TunnelTask(direction, length));
                            }
                        }
                        if (ActionBotTask.isSendMessageTask(mode)) {
                            if (taskObject.get("Message") != null) {
                                String message = taskObject.get("Message").getAsString();

                                tasks.add(new SendMessageTask(message));
                            }
                        }
                        if (ActionBotTask.isWaitTask(mode)) {
                            if (taskObject.get("Seconds") != null) {
                                double seconds = taskObject.get("Seconds").getAsDouble();

                                tasks.add(new WaitTask(seconds));
                            }
                        }
                    }
                }
            }

            ActionBotConfig.tasks.remove(ActionBotConfig.endTask);
            ActionBotConfig.tasks.addAll(tasks);
            ActionBotConfig.tasks.add(ActionBotConfig.endTask);
        }
    }


    public static void loadColourThemes() throws IOException {
        Client.addCTheme("Default", 0x191CFF, 0xFF111111, 0xFF222222, 0x191CFF, 0xFFFFFF, 0x191CFF);
        Client.addCTheme("PurpleNight", 0xD902EE, 0x320D3E, 0x360E42, 0xF162FF, 0xFFD79D, 0xD902EE);
        Client.addCTheme("PinkAndBrown", 0xBE1558, 0x322514, 0x3A2B17, 0xE75874, 0xFBCBC9, 0xBE1558);
        Client.addCTheme("Chestnut", 0xDD8F2E, 0x2E1104, 0x341406, 0xDDB869, 0x956429, 0xDD8F2E);
        Client.addCTheme("ChocolateBrownie", 0xB6452C, 0x301B28, 0x381F2E, 0xDDC5A2, 0x523634, 0xB6452C);
        Client.addCTheme("CitrusMix", 0x902B04, 0x2B2E0E, 0x333611, 0xD9901C, 0xF7E9B3, 0xD9901C);
        Client.addCTheme("CopperMountain", 0x206F7C, 0x211B1A, 0x251F1D, 0x77341D, 0xB17365, 0x206F7C);
        Client.addCTheme("Crocuses", 0xDFBD99, 0x4C1905, 0x561D07, 0xBA4A1B, 0xC4A189, 0xDFBD99);
        Client.addCTheme("Freshness", 0x62A77C, 0x153626, 0x183D2B, 0x7CC398, 0xADCBB5, 0x62A77C);
        Client.addCTheme("Ocean", 0x5A89B9, 0x0A1C34, 0x0B1F3A, 0xC1D9F9, 0x2E4553, 0x5A89B9);
        Client.addCTheme("Spice", 0xA96946, 0x271007, 0x2F1308, 0xA96946, 0xECCDB1, 0xA96946);

        PluginSystem.getLoadedPlugins().forEach(Plugin::onLoadColourThemes);

        File folder = Paths.get("BThack/Themes/ColourThemes").toFile();
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (Objects.equals(FilenameUtils.getExtension(file.getName()), "json")) {
                        String name = file.getName();
                        InputStream inputStream = Files.newInputStream(Paths.get("BThack/Themes/ColourThemes/" + name));
                        JsonObject colourThemeObject = JsonParser.parseReader(new InputStreamReader(inputStream)).getAsJsonObject();

                        if (colourThemeObject.get("Name") != null) {
                            JsonObject colourObject = colourThemeObject.get("Colours").getAsJsonObject();

                            JsonElement fontColour;
                            JsonElement backgroundFontColour;
                            JsonElement backgroundFontHoveredColour;
                            JsonElement moduleEnabledColour;
                            JsonElement moduleDisabledColour;
                            JsonElement arrayListColour;

                            fontColour = colourObject.get("fontColour");
                            backgroundFontColour = colourObject.get("backgroundFontColour");
                            backgroundFontHoveredColour = colourObject.get("backgroundFontColour");
                            moduleEnabledColour = colourObject.get("moduleEnabledColour");
                            moduleDisabledColour = colourObject.get("moduleDisabledColour");
                            arrayListColour = colourObject.get("arrayListColour");

                            Client.addCTheme(colourThemeObject.get("Name").getAsString(), fontColour.getAsInt(), backgroundFontColour.getAsInt(), backgroundFontHoveredColour.getAsInt(), moduleEnabledColour.getAsInt(), moduleDisabledColour.getAsInt(), arrayListColour.getAsInt());
                        }
                        inputStream.close();
                    }
                }
            }
        }
    }

    public static void refreshWallpapers() {
        SelectWallpaperScreen.wallpapers.clear();

        File imagesFolder = Paths.get("BThack/Wallpapers").toFile();
        File[] files = imagesFolder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    if (imageFormats.contains(FilenameUtils.getExtension(file.getName()))) {
                        SelectWallpaperScreen.wallpapers.add(new Wallpaper(file.getName(), new Texture("BThack/Wallpapers/" + file.getName(), PathMode.OUTSIDEJAR, TextureColorMode.RGBA)));
                    }
                }
            }
        }
    }

    public static void saveWallpaper(String fileName) throws IOException {
        Path infoPath = Paths.get("BThack/Wallpapers/WallpaperInfo.txt");
        if (Files.exists(infoPath)) {
            BufferedWriter writer = Files.newBufferedWriter(infoPath);
            writer.write(fileName);
            writer.close();
        }
    }

    public static void loadWallpaper() throws IOException {
        Path infoPath = Paths.get("BThack/Wallpapers/WallpaperInfo.txt");
        if (Files.exists(infoPath)) {
            BufferedReader reader = Files.newBufferedReader(infoPath, StandardCharsets.UTF_8);
            String line = reader.readLine();
            if (line != null) {
                BThack.log("BThack/Wallpapers/" + line);
                if (!line.equals("default") && Files.exists(Paths.get("BThack/Wallpapers/" + line))) {
                    BThackMainMenuScreen.mainMenuTexture = new Texture("BThack/Wallpapers/" + line, PathMode.OUTSIDEJAR, TextureColorMode.RGBA);
                }
            }
            reader.close();
        }
    }

    private static final Set<String> imageFormats = new HashSet<>(Arrays.asList(
            "png", "jpg", "gif"
    ));

    public static void loadLanguages() {
        LanguageSystem.loadTranslations(ConfigUtils.newInputStream("assets/bthack/langs/EN.lng", PathMode.INSIDEJAR), "EN");
        LanguageSystem.loadTranslations(ConfigUtils.newInputStream("assets/bthack/langs/RU.lng", PathMode.INSIDEJAR), "RU");
        LanguageSystem.loadTranslations(ConfigUtils.newInputStream("assets/bthack/langs/PL.lng", PathMode.INSIDEJAR), "PL");

        PluginSystem.getLoadedPlugins().forEach(Plugin::onLoadLanguages);
    }

    public static void loadActionBotTasksData() {
        //---------Move Task---------//
        ActionBotConfig.addFullActionBotTask(new ActionBotTaskData() {
            @Override
            public ActionBotTask getTask() {
                return new MoveTask(0,0, false, MoveTask.Type.Default);
            }

            @Override
            public BThackScreen getAddingTaskScreen() {
                return new AddingMoveTaskGui();
            }

            @Override
            public BThackScreen getEditTaskScreen(TaskButton taskButton) {
                return new EditMoveTaskGui(taskButton);
            }
        });
        //---------------------------//

        //---------Jump Task---------//
        ActionBotConfig.addFullActionBotTask(new ActionBotTaskData() {
            @Override
            public ActionBotTask getTask() {
                return new JumpTask();
            }

            @Override
            public BThackScreen getAddingTaskScreen() {
                ActionBotConfig.addActionTaskToList(new JumpTask());
                return new ActionBotConfigGui();
            }

            @Override
            public BThackScreen getEditTaskScreen(TaskButton taskButton) {
                return new ActionBotConfigGui();
            }
        });
        //---------------------------//

        //---------Send Message Task---------//
        ActionBotConfig.addFullActionBotTask(new ActionBotTaskData() {
            @Override
            public ActionBotTask getTask() {
                return new SendMessageTask("");
            }

            @Override
            public BThackScreen getAddingTaskScreen() {
                return new AddingSendMessageTask();
            }

            @Override
            public BThackScreen getEditTaskScreen(TaskButton taskButton) {
                return new EditSendMessageTaskGui(taskButton);
            }
        });
        //-----------------------------------//

        //---------Tunnel Task---------//
        ActionBotConfig.addFullActionBotTask(new ActionBotTaskData() {
            @Override
            public ActionBotTask getTask() {
                return new TunnelTask(TunnelTask.Direction.X_MINUS, 0);
            }

            @Override
            public BThackScreen getAddingTaskScreen() {
                return new AddingTunnelTaskGui();
            }

            @Override
            public BThackScreen getEditTaskScreen(TaskButton taskButton) {
                return new EditTunnelTaskGui(taskButton);
            }
        });
        //-----------------------------//

        //---------Wait Task---------//
        ActionBotConfig.addFullActionBotTask(new ActionBotTaskData() {
            @Override
            public ActionBotTask getTask() {
                return new WaitTask(0);
            }

            @Override
            public BThackScreen getAddingTaskScreen() {
                return new AddingWaitTaskGui();
            }

            @Override
            public BThackScreen getEditTaskScreen(TaskButton taskButton) {
                return new EditWaitTaskGui(taskButton);
            }
        });
        //---------------------------//
    }
}
