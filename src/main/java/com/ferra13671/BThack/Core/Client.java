package com.ferra13671.BThack.Core;


import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.FileSystem.ConfigSystem.ConfigUtils;
import com.ferra13671.BThack.api.ColourThemes.ColourTheme;
import com.ferra13671.BThack.api.CommandSystem.manager.CommandManager;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Language.LanguageSystem;
import com.ferra13671.BThack.api.Managers.BlockSearchManager;
import com.ferra13671.BThack.api.Managers.Destroy.DestroyManager;
import com.ferra13671.BThack.api.Managers.FireWorkManager;
import com.ferra13671.BThack.api.Managers.NetworkManager;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.TPSManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Plugin.Plugin;
import com.ferra13671.BThack.api.Plugin.PluginSystem;
import com.ferra13671.BThack.api.Plugin.PluginUtils;
import com.ferra13671.BThack.api.Storage.MusicStorage;
import com.ferra13671.BThack.api.Utils.BlockUtils;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.api.Utils.Grim.GrimFreezeUtils;
import com.ferra13671.BThack.api.Utils.List.BlockList;
import com.ferra13671.BThack.impl.Commands.OtherList.*;
import com.ferra13671.BThack.impl.Commands.*;
import com.ferra13671.BThack.impl.Commands.Social.Clans.*;
import com.ferra13671.BThack.impl.Commands.Social.*;
import com.ferra13671.BThack.impl.Modules.CLIENT.*;
import com.ferra13671.BThack.impl.Modules.COMBAT.*;
import com.ferra13671.BThack.impl.Modules.COMBAT.CrystalAura.CrystalAura;
import com.ferra13671.BThack.impl.Modules.COMBAT.KillAura.KillAura;
import com.ferra13671.BThack.impl.Modules.MISC.PacketMine.PacketMine;
import com.ferra13671.BThack.impl.Modules.MISC.Timer;
import com.ferra13671.BThack.impl.Modules.MISC.*;
import com.ferra13671.BThack.impl.Modules.MOVEMENT.*;
import com.ferra13671.BThack.impl.Modules.PLAYER.*;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.ActionBot;
import com.ferra13671.BThack.impl.Modules.PLAYER.InventoryManager.InventoryManager;
import com.ferra13671.BThack.impl.Modules.PLAYER.PMSpammer.PMSpammer;
import com.ferra13671.BThack.impl.Modules.PLAYER.Spammer.Spammer;
import com.ferra13671.BThack.impl.Modules.RENDER.*;
import com.ferra13671.BThack.impl.Modules.RENDER.HoleESP.HoleESP;
import com.ferra13671.BThack.impl.Modules.WORLD.*;
import net.minecraft.block.Block;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Client implements Mc {
    //public static MainMenuMusicThread mainMenuMusicThread;

    public static final String name = "BThack 1.20.4 | " + mc.getSession().getUsername();
    public static final String cName = "BThack " + BThack.VERSION;
    public static final String ChatPrefix = "$";


    private static final CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<>();
    public static final CopyOnWriteArrayList<HudComponent> hudComponents = new CopyOnWriteArrayList<>();

    public static final HashMap<String , BlockList> blockLists = new HashMap<>();

    public static ColourTheme colourTheme;

    public static boolean inited = false;

    public static TPSManager tpsManager;
    public static final BlockSearchManager blockSearchManager = new BlockSearchManager();
    public static final FireWorkManager fireWorkManager = new FireWorkManager();
    public static final DestroyManager destroyManager = new DestroyManager();
    public static final NetworkManager networkManager = new NetworkManager();

    public static final ChatNotifications chatNotifications = new ChatNotifications();
    public static Module hudModule;



    public static void startup() {
        //Display.setTitle(name);

        PluginSystem.getLoadedPlugins().forEach(Plugin::onInitModules);

        modules.addAll(Arrays.asList(
                //COMBAT
                new AimBot(),
                new AutoArmor(),
                new AutoOffhand(),
                new AutoShield(),
                new AutoSword(),
                new AutoTotemFall(),
                new Criticals(),
                new CrystalAura(),
                new FastBow(),
                new FireBallAura(),
                //new HitBox(),
                new KillAura(),
                new HitSound(),
                //new NoEntityTrace(),
                new HoleFill(),
                new IgniteAura(),
                new NoFriendDamage(),
                new SafeTrap(),
                new Surround(),
                new TNTIgniter(),
                new WebAura(),
                new WitherRoseAura(),

                //EXPLOIT
                //new AntiPacketKick(),
                //new LiquidInteract(),
                //new NoBreakReset(),
                //new PingSpoof(),
                //new ThunderHack(),

                //MISC
                new Breaker(),
                new CleanMemory(),
                new CreeperRadar(),
                new EnemyRadar(),
                new GameCrasher(),
                new HighwayBuilder(),
                new ItemRandomizer(),
                new OpenedGuiInfo(),
                new PacketMine(),
                new PistonSoundDelay(),
                new PortalGod(),
                //new Replanish(),  // In InventoryManager
                new Scrapper(),
                new SuperInstaMine(),
                new Timer(),
                new TopperRadar(),

                //CLIENT
                new BThackCape(),
                new BThackMainMenu(),
                chatNotifications,
                new ClickGui(),
                //new CustomSkin(),
                new DiscordRPC(),
                new FPSReducer(),
                //new Font(),
                new HUD(),
                new HudEditor(),
                new Language(),
                new MemoryCleaner(),
                //new ModuleOnOffSound(),
                //new Settings(),

                //MOVEMENT
                //new AirJump(),
                new AntiAFK(),
                new AutoJump(),
                new CameraRotator(),
                new ElytraFlight(),
                new ElytraFastClose(),
                new EntitySpeed(),
                new GrimRocket(),
                //new FastFall(),
                new Flip(),
                new Impulse(),
                new LevitationControl(),
                new NinjaBridge(),
                //new Glide(),
                //new IceSpeed(),
                new GuiMove(),
                new NoFall(),
                //new Jesus(),
                new NoRotate(),
                new NoSlow(),
                new NoSRotations(),
                new SafeWalk(),
                new Scaffold(),
                new Speed(),
                //new Spider(),
                new Sprint(),
                new ShiftSpam(),
                //new Step(),
                new Strafe(),

                //PLAYER
                new ActionBot(),
                new AutoDisconnect(),
                new AutoEat(),
                new AutoElytra(),
                new AutoFirework(),
                new AutoFish(),
                new AutoPearl(),
                new AutoRespawn(),
                new AutoTool(),
                new BabyModel(),
                new ElytraReplace(),
                new ElytraSwap(),
                //new ElytraTakeOf(),
                new ChestStealer(),
                //new FakeCreative(),
                new FakePlayer(),
                new FastPlace(),
                new FastUse(),
                new FreeCam(),
                new Introvert(),
                new InventoryManager(),
                //new ItemSaver(),  // In InventoryManager
                new LagDetector(),
                new MultiFakePlayer(),
                new PMSpammer(),
                new Spammer(),
                new NoJumpDelay(),
                new NoServerSlot(),
                new NoSwing(),
                new PacketPlace(),
                new Velocity(),
                new XCarry(),

                //RENDER
                new Africa(),
                new AntiHazard(),
                new AttackTrace(),
                new BlockHighlight(),
                new Caipirinha(),
                new CameraClip(),
                //new Chams(),
                new ChestESP(),
                //new ChinaHat(),
                new CS_Crosshair(),
                new ESP(),
                //new ExtraTab(),
                new FullBright(),
                new GammaColor(),
                new HandTweaks(),
                new HoleESP(),
                new LastOpenChest(),
                new ModifyCamera(),
                new Nametags(),
                new NoFog(),
                new NoOverlay(),
                new NoRender(),
                new Radar(),
                new Search(),
                new Tooltips(),
                new Tracers(),
                //new Trajectories(),
                //new Zoom(),
                new Xray(),

                //WORLD
                new AutoSign(),
                //new BHMaximizer(),
                //new Reach(),
                new CloudsColor(),
                new CustomDayTime(),
                new Fly(),
                new FogColor(),
                new Lawnmower(),
                new NoWeather(),
                new SkyColor(),
                new WorldElements()
        ));
        modules.addAll(PluginUtils.getPluginsModules());

        initBlockLists();

        initCommands();

        initMusic();

        tpsManager = new TPSManager();

        BThack.EVENT_BUS.register(destroyManager);
        BThack.EVENT_BUS.register(new GrimFreezeUtils());

        inited = true;
    }



    public static ArrayList<Module> getModulesInCategory(Module.Category c) {
        ArrayList<Module> mods = new ArrayList<>();
        for (Module m : modules) {
            if (m.getCategory().name().equalsIgnoreCase(c.name())) {
                mods.add(m);
            }
        }
        return mods;
    }


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
        BThack.instance.colourThemeManager.addColourTheme(new ColourTheme(name, frontColour, backgroundFontColour, backgroundFontHoveredColour, moduleEnabledColour, moduleDisabledColour, arrayListColour));
    }


    private static void initCommands() {
        PluginSystem.getLoadedPlugins().forEach(Plugin::onInitCommands);

        CommandManager.addCommands(
                new FriendsCommand(),
                new EnemiesCommand(),
                new CleanMemoryCommand(),


                new ClansCommand(),
                new ClanMembersCommand(),
                new ClanStatusCommand(),


                new RefreshCommand(),
                new RotateCommand(),
                new BindCommand(),
                new BreakCommand(),
                new BuildCommand()


        );
        blockLists.forEach((s, blockList) -> CommandManager.addCommands(
                blockList.editListCommand,
                blockList.listCommand
        ));
        CommandManager.addCommands(
                new CommandListCommand(),
                new FriendListCommand(),
                new EnemyListCommand(),
                new ClansListCommand()
        );
        CommandManager.commands.addAll(PluginUtils.getPluginsCommands());
    }

    private static void initMusic() {

        MusicStorage.init();

        /*
        if (
                !Files.exists(Paths.get("BThack_Music/CosmicMusic1.wav")) ||
                        !Files.exists(Paths.get("BThack_Music/CosmicMusic2.wav")) ||
                        !Files.exists(Paths.get("BThack_Music/CosmicMusic3.wav")) ||
                        !Files.exists(Paths.get("BThack_Music/CosmicMusic4.wav"))
        ) {
            BThack.error("No music found for main menu! Further work with the main menu will be accompanied without sounds.");
        } else {
            mainMenuMusicThread = new MainMenuMusicThread();

            mainMenuMusicThread.init();

            BThack.log("The music is initialized and ready for use.");
        }

         */
    }

    private static void initBlockLists() {
        //---------Search---------//
        addBlockList(new BlockList("Search", "search", "SearchBlocks") {
            @Override
            public void saveInFile() throws IOException {
                ConfigUtils.saveInTxt("SearchBlocks", "Search", writer -> {
                    for (String blockName : Client.blockLists.get("Search").blockNames) {
                        try {
                            writer.write(blockName + System.lineSeparator());
                        } catch (IOException ignored) {}
                    }
                });
            }

            @Override
            public void loadFromFile() throws IOException {
                ConfigUtils.loadFromTxt("SearchBlocks", "Search", line -> {
                    Block block = BlockUtils.getBlockFromNameOrID(line);
                    if (block != null) {
                        Client.blockSearchManager.addBlockToSearch(block);
                        Client.blockLists.get("Search").blockNames.add(line);
                    }
                });
            }

            @Override
            public void addToList(String arg) {
                if (!Client.blockSearchManager.getSearchBlocks().contains(block)) {
                    Client.blockSearchManager.addBlockToSearch(block);
                    Client.blockLists.get("Search").blockNames.add(arg);
                    try {
                        Client.blockLists.get("Search").saveInFile();
                    } catch (IOException ignored) {
                    }
                    ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.List.blockAdded"));
                } else {
                    ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.List.blockAlreadyAdded"));
                }
            }

            @Override
            public void removeFromList(String arg) {
                if (Client.blockSearchManager.getSearchBlocks().contains(block)) {
                    Client.blockSearchManager.removeBlockToSearch(block);
                    Client.blockLists.get("Search").blockNames.remove(arg);
                    try {
                        Client.blockLists.get("Search").saveInFile();
                    } catch (IOException ignored) {
                    }
                    ChatUtils.sendMessage(Formatting.AQUA + LanguageSystem.translate("lang.command.List.blockRemoved"));
                } else {
                    ChatUtils.sendMessage(Formatting.YELLOW + LanguageSystem.translate("lang.command.List.blockAlreadyRemoved"));
                }
            }

            @Override
            public void clearList() {
                Client.blockSearchManager.clearSearchBlocks();
                Client.blockLists.get("Search").blockNames.clear();
                try {
                    Client.blockLists.get("Search").saveInFile();
                } catch (IOException ignored) {
                }
                ChatUtils.sendMessage(Formatting.AQUA + String.format(LanguageSystem.translate("lang.command.List.listCleared"), "Search"));
            }
        });
        //------------------------//

        //---------Breaker---------//
        addBlockList(new BlockList("Breaker", "breaker", "BreakerBlocks"));
        //-------------------------//

        //---------Xray---------//
        addBlockList(new BlockList("Xray", "xray", "XrayBlocks") {
            @Override
            public void postAction() {
                if (Client.getModuleByName("Xray").isEnabled())
                    mc.worldRenderer.reload();
            }
        });
        //----------------------//
    }

    public static void addBlockList(BlockList blockList) {
        blockLists.put(blockList.editListCommand.descName, blockList);
    }

    public static boolean isOptionActivated(String moduleName, BooleanSetting setting) {
        return getModuleByName(moduleName).isEnabled() && setting.getValue();
    }

    public static List<Module> getAllModules() {
        return modules;
    }

    public static List<HudComponent> getAllHudComponents() {
        return hudComponents;
    }

    public static boolean isNeedSearch() {
        return Client.getModuleByName("Search").isEnabled();
    }
}
