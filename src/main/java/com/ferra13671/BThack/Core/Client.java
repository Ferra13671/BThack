package com.ferra13671.BThack.Core;


import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.ColourThemes.ColourTheme;
import com.ferra13671.BThack.api.CommandSystem.manager.CommandManager;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Managers.BlockSearchManager;
import com.ferra13671.BThack.api.Managers.FireWorkManager;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.TPSManager;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Plugin.Plugin;
import com.ferra13671.BThack.api.Plugin.PluginSystem;
import com.ferra13671.BThack.api.Plugin.PluginUtils;
import com.ferra13671.BThack.api.Storage.MusicStorage;
import com.ferra13671.BThack.impl.Commands.List.*;
import com.ferra13671.BThack.impl.Commands.*;
import com.ferra13671.BThack.impl.Commands.Social.Clans.*;
import com.ferra13671.BThack.impl.Commands.Social.*;
import com.ferra13671.BThack.impl.Modules.CLIENT.*;
import com.ferra13671.BThack.impl.Modules.COMBAT.*;
import com.ferra13671.BThack.impl.Modules.COMBAT.KillAura.KillAura;
import com.ferra13671.BThack.impl.Modules.MISC.Timer;
import com.ferra13671.BThack.impl.Modules.MISC.*;
import com.ferra13671.BThack.impl.Modules.MOVEMENT.*;
import com.ferra13671.BThack.impl.Modules.PLAYER.*;
import com.ferra13671.BThack.impl.Modules.PLAYER.PMSpammer.PMSpammer;
import com.ferra13671.BThack.impl.Modules.PLAYER.Spammer.Spammer;
import com.ferra13671.BThack.impl.Modules.RENDER.*;
import com.ferra13671.BThack.impl.Modules.RENDER.HoleESP.HoleESP;
import com.ferra13671.BThack.impl.Modules.WORLD.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public final class Client implements Mc {
    //public static MainMenuMusicThread mainMenuMusicThread;

    public static final String name = "BThack 1.20.4 | " + mc.getSession().getUsername();
    public static final String cName = "BThack " + BThack.VERSION;
    public static final String ChatPrefix = "$";


    private static final CopyOnWriteArrayList<Module> modules = new CopyOnWriteArrayList<>();
    public static final CopyOnWriteArrayList<HudComponent> hudComponents = new CopyOnWriteArrayList<>();

    public static Module hudModule;


    public static ColourTheme colourTheme;

    public static boolean inited = false;

    public static TPSManager tpsManager;
    public static final BlockSearchManager blockSearchManager = new BlockSearchManager();
    public static final FireWorkManager fireWorkManager = new FireWorkManager();



    public static void startup() {
        //Display.setTitle(name);

        PluginSystem.getLoadedPlugins().forEach(Plugin::onInitModules);

        modules.addAll(Arrays.asList(
                //COMBAT
                new AimBot(),
                new AutoShield(),
                new AutoSword(),
                new Criticals(),
                new FastBow(),
                new FireBallAura(),
                //new HitBox(),
                new KillAura(),
                new HitSound(),
                //new NoEntityTrace(),
                new NoFriendDamage(),
                //new SafeTrap(),
                //new Surround(),

                //EXPLOIT
                //new AntiPacketKick(),
                //new LiquidInteract(),
                //new NoBreakReset(),
                //new PingSpoof(),
                //new ThunderHack(),

                //MISC
                new CleanMemory(),
                new CreeperRadar(),
                new GameCrasher(),
                new EnemyRadar(),
                new ItemRandomizer(),
                new OpenedGuiInfo(),
                new PistonSoundDelay(),
                new PortalGod(),
                new Timer(),

                //CLIENT
                new BThackCape(),
                //new BThackMainMenu(),
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
                //new AntiAFK(),
                new AutoJump(),
                new CameraRotator(),
                new ElytraFlight(),
                new ElytraFastClose(),
                new EntitySpeed(),
                //new FastFall(),
                new Flip(),
                new Impulse(),
                //new Glide(),
                //new IceSpeed(),
                new GuiMove(),
                //new NoFall(),
                //new Jesus(),
                //new NinjaBridge(),
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
                //new ActionBot(),
                new AutoDisconnect(),
                new AutoEat(),
                new AutoFirework(),
                new AutoPearl(),
                new AutoRespawn(),
                //new AutoTool(),
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
                //new ItemSaver(),
                //new LagDetector(),
                new MultiFakePlayer(),
                new PMSpammer(),
                new Spammer(),
                new NoJumpDelay(),
                new NoServerSlot(),
                new NoSwing(),
                //new PacketPlace(),
                new Velocity(),
                new XCarry(),

                //RENDER
                new Africa(),
                new AntiHazard(),
                //new AttackTrace(),
                new BlockHighlight(),
                new Caipirinha(),
                new CameraClip(),
                new ModifyCamera(),
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
                //new LargeNicknames(),
                //new NoFog(),
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
                //new BHMaximizer(),
                //new Reach(),
                new CloudsColor(),
                new CustomDayTime(),
                //new Fly(),
                new FogColor(),
                //new Lawnmower(),
                new NoWeather(),
                new SkyColor(),
                new WorldElements()
        ));
        modules.addAll(PluginUtils.getPluginsModules());

        initCommands();

        initMusic();

        tpsManager = new TPSManager();

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
                new XrayCommand(),
                new SearchCommand(),
                new BindCommand(),


                new CommandListCommand(),
                new FriendListCommand(),
                new EnemyListCommand(),
                new ClansListCommand(),
                new XrayListCommand(),
                new SearchListCommand()
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

    public static boolean isOptionActivated(String moduleName, BooleanSetting setting) {
        return getModuleByName(moduleName).isEnabled() && setting.getValue();
    }

    public static List<Module> getAllModules() {
        return modules;
    }

    public static List<HudComponent> getAllHudComponents() {
        return hudComponents;
    }
}
