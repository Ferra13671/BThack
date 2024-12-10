package com.ferra13671.BThack.Core.Client;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.api.Module.Module;
import com.ferra13671.BThack.api.Plugin.Plugin;
import com.ferra13671.BThack.api.Plugin.PluginSystem;
import com.ferra13671.BThack.api.Plugin.PluginUtils;
import com.ferra13671.BThack.impl.Modules.CLIENT.*;
import com.ferra13671.BThack.impl.Modules.COMBAT.*;
import com.ferra13671.BThack.impl.Modules.COMBAT.KillAura.KillAura;
import com.ferra13671.BThack.impl.Modules.MISC.*;
import com.ferra13671.BThack.impl.Modules.MISC.PacketMine.PacketMine;
import com.ferra13671.BThack.impl.Modules.MOVEMENT.*;
import com.ferra13671.BThack.impl.Modules.PLAYER.*;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.ActionBot;
import com.ferra13671.BThack.impl.Modules.PLAYER.InventoryManager.InventoryManager;
import com.ferra13671.BThack.impl.Modules.PLAYER.Spammer.Spammer;
import com.ferra13671.BThack.impl.Modules.RENDER.*;
import com.ferra13671.BThack.impl.Modules.RENDER.HoleESP.HoleESP;
import com.ferra13671.BThack.impl.Modules.WORLD.*;

public final class ModuleList {
    //CLIENT
    public static BThackCape bthackCape;
    public static BThackMainMenu bthackMainMenu;
    public static ChatNotifications chatNotifications;
    public static ClickGui clickGui;
    public static DiscordRPC discordRPC;
    public static FPSReducer fpsReducer;
    public static HUD HUD;
    public static HudEditor hudEditor;
    public static Language language;
    public static MemoryCleaner memoryCleaner;
    public static MenuShader menuShader;
    public static ToggleSound toggleSound;

    //COMBAT
    public static AimBot aimBot;
    public static AutoArmor autoArmor;
    public static AutoClicker autoClicker;
    public static AutoOffhand autoOffhand;
    public static AutoShield autoShield;
    public static AutoSoup autoSoup;
    public static AutoSword autoSword;
    public static AutoTotemFall autoTotemFall;
    public static Criticals criticals;
    //public static CrystalAura crystalAura;
    public static FastBow fastBow;
    public static FireBallAura fireBallAura;
    public static HitSound hitSound;
    public static HoleFill holeFill;
    public static IgniteAura igniteAura;
    public static KillAura killAura;
    //public static LavaAura lavaAura;
    public static NoFriendDamage noFriendDamage;
    public static PearlPhase pearlPhase;
    public static SafeTrap safeTrap;
    public static Surround surround;
    public static TNTIgniter tntIgniter;
    public static TotemPopNotifier totemPopNotifier;
    public static WebAura webAura;
    public static WitherRoseAura witherRoseAura;

    //MISC
    public static AutoAuth autoAuth;
    public static Breaker breaker;
    public static CleanMemory cleanMemory;
    public static CreeperRadar creeperRadar;
    public static EnemyRadar enemyRadar;
    public static GameCrasher gameCrasher;
    public static HighwayBuilder highwayBuilder;
    public static InstaNuker instaNuker;
    //public static InstaRemine instaRemine;  //TODO: maybe?
    public static ItemRandomizer itemRandomizer;
    public static MiddleClick middleClick;
    public static MoreChatHistory moreChatHistory;
    public static NoBreakDelay noBreakDelay;
    public static NoPacketKick noPacketKick;
    public static NoSoundLag noSoundLag;
    public static OpenedGuiInfo openedGuiInfo;
    public static PacketMine packetMine;
    public static PistonSoundDelay pistonSoundDelay;
    public static PortalGod portalGod;
    public static Scrapper scrapper;
    public static SuperInstaMine superInstaMine;
    public static Timer timer;
    public static TopperRadar topperRadar;
    public static TrashThrower trashThrower;
    public static VisualRange visualRange;

    //MOVEMENT
    public static AntiAFK antiAFK;
    public static AutoJump autoJump;
    public static AutoWalk autoWalk;
    public static CameraRotator cameraRotator;
    public static ElytraFastClose elytraFastClose;
    public static ElytraFlight elytraFlight;
    public static ElytraStrafe elytraStrafe;
    public static EntitySpeed entitySpeed;
    public static FastFall fastFall;
    public static Flip flip;
    public static GrimRocket grimRocket;
    public static GuiMove guiMove;
    public static Impulse impulse;
    public static LevitationControl levitationControl;
    public static NinjaBridge ninjaBridge;
    public static NoFall noFall;
    public static NoJumpDelay noJumpDelay;
    public static NoPush noPush;
    public static NoRotate noRotate;
    public static NoSlow noSlow;
    public static NoSRotations noSRotations;
    public static Parkour parkour;
    public static SafeWalk safeWalk;
    public static Scaffold scaffold;
    public static ShiftSpam shiftSpam;
    public static Speed speed;
    public static Sprint sprint;
    public static Strafe strafe;
    public static Velocity velocity;

    //PLAYER
    public static ActionBot actionBot;
    public static AutoDisconnect autoDisconnect;
    public static AutoEat autoEat;
    public static AutoElytra autoElytra;
    public static AutoFirework autoFirework;
    public static AutoFish autoFish;
    public static AutoMine autoMine;
    public static AutoMount autoMount;
    public static AutoPearl autoPearl;
    public static AutoRespawn autoRespawn;
    public static AutoTool autoTool;
    public static BabyModel babyModel;
    public static ChestStealer chestStealer;
    public static ElytraReplace elytraReplace;
    public static ElytraSwap elytraSwap;
    public static FakePlayer fakePlayer;
    public static FastDrop fastDrop;
    public static FastPlace fastPlace;
    public static FastUse fastUse;
    public static FreeCam freeCam;
    public static InventoryManager inventoryManager;
    public static LagDetector lagDetector;
    public static MultiFakePlayer multiFakePlayer;
    public static NoElytraBreak noElytraBreak;
    public static NoGlitchBlocks noGlitchBlocks;
    public static NoServerSlot noServerSlot;
    public static PacketPlace packetPlace;
    public static PMSpammer pmSpammer;
    public static Sneak sneak;
    public static Spammer spammer;
    public static XCarry xCarry;

    //RENDER
    public static Africa africa;
    public static AntiHazard antiHazard;
    public static AttackTrace attackTrace;
    public static BetterChat betterChat;
    public static BlockHighlight blockHighlight;
    public static Caipirinha caipirinha;
    public static CameraClip cameraClip;
    public static ChestESP chestESP;
    public static CS_Crosshair csCrosshair;
    public static EnchantColor enchantColor;
    public static ESP ESP;
    public static ExtraTab extraTab;
    public static FullBright fullBright;
    public static FXAA FXAA;
    public static HandTweaks handTweaks;
    public static HoleESP holeESP;
    public static LastOpenChest lastOpenChest;
    public static MinecraftShaders minecraftShaders;
    public static ModifyCamera modifyCamera;
    public static MotionBlur motionBlur;
    public static Nametags nametags;
    public static NewChunks newChunks;
    public static NoFog noFog;
    public static NoOverlay noOverlay;
    public static NoRender noRender;
    public static NoSwing noSwing;
    public static PasswordHider passwordHider;
    public static Radar radar;
    public static Search search;
    public static Tooltips tooltips;
    public static Tracers tracers;
    public static Xray xray;

    //WORLD
    //public static AutoFarm autoFarm;
    public static AutoFarmland autoFarmland;
    public static AutoSign autoSign;
    public static CloudsColor cloudsColor;
    public static CustomDayTime customDayTime;
    public static Fly fly;
    public static FogColor fogColor;
    public static Lawnmower lawnmower;
    public static LiquidFiller liquidFiller;
    public static NoWeather noWeather;
    public static SkyColor skyColor;
    public static WorldElements worldElements;


    static void initModules() {
        PluginSystem.getLoadedPlugins().forEach(Plugin::onInitModules);


        //CLIENT
        bthackCape = register(new BThackCape());
        bthackMainMenu = register(new BThackMainMenu());
        chatNotifications = register(new ChatNotifications());
        clickGui = register(new ClickGui());
        discordRPC = register(new DiscordRPC());
        fpsReducer = register(new FPSReducer());
        HUD = register(new HUD());
        hudEditor = register(new HudEditor());
        language = register(new Language());
        memoryCleaner = register(new MemoryCleaner());
        menuShader = register(new MenuShader());
        toggleSound = register(new ToggleSound());

        //COMBAT
        aimBot = register(new AimBot());
        autoArmor = register(new AutoArmor());
        autoClicker = register(new AutoClicker());
        autoOffhand = register(new AutoOffhand());
        autoShield = register(new AutoShield());
        autoSoup = register(new AutoSoup());
        autoSword = register(new AutoSword());
        autoTotemFall = register(new AutoTotemFall());
        criticals = register(new Criticals());
        //crystalAura = register(new CrystalAura());
        fastBow = register(new FastBow());
        fireBallAura = register(new FireBallAura());
        hitSound = register(new HitSound());
        holeFill = register(new HoleFill());
        igniteAura = register(new IgniteAura());
        killAura = register(new KillAura());
        //lavaAura = register(new LavaAura());
        noFriendDamage = register(new NoFriendDamage());
        pearlPhase = register(new PearlPhase());
        safeTrap = register(new SafeTrap());
        surround = register(new Surround());
        tntIgniter = register(new TNTIgniter());
        totemPopNotifier = register(new TotemPopNotifier());
        webAura = register(new WebAura());
        witherRoseAura = register(new WitherRoseAura());

        //MISC
        autoAuth = register(new AutoAuth());
        breaker = register(new Breaker());
        cleanMemory = register(new CleanMemory());
        creeperRadar = register(new CreeperRadar());
        enemyRadar = register(new EnemyRadar());
        gameCrasher = register(new GameCrasher());
        highwayBuilder = register(new HighwayBuilder());
        instaNuker = register(new InstaNuker());
        //instaRemine = register(new InstaRemine());
        itemRandomizer = register(new ItemRandomizer());
        middleClick = register(new MiddleClick());
        moreChatHistory = register(new MoreChatHistory());
        noBreakDelay = register(new NoBreakDelay());
        noPacketKick = register(new NoPacketKick());
        noSoundLag = register(new NoSoundLag());
        openedGuiInfo = register(new OpenedGuiInfo());
        packetMine = register(new PacketMine());
        pistonSoundDelay = register(new PistonSoundDelay());
        portalGod = register(new PortalGod());
        scrapper = register(new Scrapper());
        superInstaMine = register(new SuperInstaMine());
        timer = register(new Timer());
        topperRadar = register(new TopperRadar());
        trashThrower = register(new TrashThrower());
        visualRange = register(new VisualRange());

        //MOVEMENT
        antiAFK = register(new AntiAFK());
        autoJump = register(new AutoJump());
        autoWalk = register(new AutoWalk());
        cameraRotator = register(new CameraRotator());
        elytraFastClose = register(new ElytraFastClose());
        elytraFlight = register(new ElytraFlight());
        elytraStrafe = register(new ElytraStrafe());
        entitySpeed = register(new EntitySpeed());
        fastFall = register(new FastFall());
        flip = register(new Flip());
        grimRocket = register(new GrimRocket());
        guiMove = register(new GuiMove());
        impulse = register(new Impulse());
        levitationControl = register(new LevitationControl());
        ninjaBridge = register(new NinjaBridge());
        noFall = register(new NoFall());
        noJumpDelay = register(new NoJumpDelay());
        noPush = register(new NoPush());
        noRotate = register(new NoRotate());
        noSlow = register(new NoSlow());
        noSRotations = register(new NoSRotations());
        parkour = register(new Parkour());
        safeWalk = register(new SafeWalk());
        scaffold = register(new Scaffold());
        shiftSpam = register(new ShiftSpam());
        speed = register(new Speed());
        sprint = register(new Sprint());
        strafe = register(new Strafe());
        velocity = register(new Velocity());

        //PLAYER
        actionBot = register(new ActionBot());
        autoDisconnect = register(new AutoDisconnect());
        autoEat = register(new AutoEat());
        autoElytra = register(new AutoElytra());
        autoFirework = register(new AutoFirework());
        autoFish = register(new AutoFish());
        if (BThack.isBaritonePresent()) autoMine = register(new AutoMine());
        autoMount = register(new AutoMount());
        autoPearl = register(new AutoPearl());
        autoRespawn = register(new AutoRespawn());
        autoTool = register(new AutoTool());
        babyModel = register(new BabyModel());
        chestStealer = register(new ChestStealer());
        elytraReplace = register(new ElytraReplace());
        elytraSwap = register(new ElytraSwap());
        fakePlayer = register(new FakePlayer());
        fastDrop = register(new FastDrop());
        fastPlace = register(new FastPlace());
        fastUse = register(new FastUse());
        freeCam = register(new FreeCam());
        inventoryManager = register(new InventoryManager());
        lagDetector = register(new LagDetector());
        multiFakePlayer = register(new MultiFakePlayer());
        noElytraBreak = register(new NoElytraBreak());
        noGlitchBlocks = register(new NoGlitchBlocks());
        noServerSlot = register(new NoServerSlot());
        packetPlace = register(new PacketPlace());
        pmSpammer = register(new PMSpammer());
        sneak = register(new Sneak());
        spammer = register(new Spammer());
        xCarry = register(new XCarry());

        //RENDER
        africa = register(new Africa());
        antiHazard = register(new AntiHazard());
        attackTrace = register(new AttackTrace());
        betterChat = register(new BetterChat());
        blockHighlight = register(new BlockHighlight());
        caipirinha = register(new Caipirinha());
        cameraClip = register(new CameraClip());
        chestESP = register(new ChestESP());
        csCrosshair = register(new CS_Crosshair());
        enchantColor = register(new EnchantColor());
        ESP = register(new ESP());
        extraTab = register(new ExtraTab());
        fullBright = register(new FullBright());
        FXAA = register(new FXAA());
        handTweaks = register(new HandTweaks());
        holeESP = register(new HoleESP());
        lastOpenChest = register(new LastOpenChest());
        minecraftShaders = register(new MinecraftShaders());
        modifyCamera = register(new ModifyCamera());
        motionBlur = register(new MotionBlur());
        nametags = register(new Nametags());
        newChunks = register(new NewChunks());
        noFog = register(new NoFog());
        noOverlay = register(new NoOverlay());
        noRender = register(new NoRender());
        noSwing = register(new NoSwing());
        passwordHider = register(new PasswordHider());
        radar = register(new Radar());
        search = register(new Search());
        tooltips = register(new Tooltips());
        tracers = register(new Tracers());
        xray = register(new Xray());

        //WORLD
        //autoFarm = register(new AutoFarm());
        autoFarmland = register(new AutoFarmland());
        autoSign = register(new AutoSign());
        cloudsColor = register(new CloudsColor());
        customDayTime = register(new CustomDayTime());
        fly = register(new Fly());
        fogColor = register(new FogColor());
        lawnmower = register(new Lawnmower());
        liquidFiller = register(new LiquidFiller());
        noWeather = register(new NoWeather());
        skyColor = register(new SkyColor());
        worldElements = register(new WorldElements());



        Client.modules.addAll(PluginUtils.getPluginsModules());
    }

    private static <T extends Module> T register(T module) {
        Client.modules.add(module);
        return module;
    }
}
