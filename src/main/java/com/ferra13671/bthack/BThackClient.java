package com.ferra13671.bthack;

import com.ferra13671.MegaEvents.eventbus.IEventBus;
import com.ferra13671.MegaEvents.eventbus.impl.EventBus;
import com.ferra13671.bthack.features.category.BThackCategory;
import com.ferra13671.bthack.managers.CategoryManager;
import com.ferra13671.bthack.features.module.Modules;
import com.ferra13671.bthack.init.InitStageImpl;
import com.ferra13671.bthack.loader.api.ClientEntrypoint;
import com.ferra13671.bthack.loader.api.ClientLoader;
import com.ferra13671.bthack.managers.bind.Bind;
import com.ferra13671.bthack.managers.bind.BindController;
import com.ferra13671.bthack.managers.bind.BindManager;
import com.ferra13671.bthack.managers.bind.BindType;
import com.ferra13671.bthack.render.BThackRenderSystem;
import com.ferra13671.bthack.render.BThackTextureAtlases;
import com.ferra13671.bthack.screen.BThackScreen;
import com.ferra13671.bthack.screen.impl.ui.UIScreen;
import com.ferra13671.bthack.utils.Mc;
import lombok.Getter;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BThackClient implements ClientEntrypoint, Mc {
    @Getter
    private static BThackClient instance;
    @Getter
    private final Logger logger = LoggerFactory.getLogger("BThack");
    @Getter
    private final IEventBus eventBus = new EventBus();
    @Getter
    private final InitStageImpl initProvider = InitStageImpl.of("BThack main stage");
    @Getter
    private ExecutorService executor;
    @Getter
    private CategoryManager categoryManager;
    @Getter
    private BindManager bindManager;
    @Getter
    private BThackScreen uiScreen;

    @Override
    public void preLaunch() {
        instance = (BThackClient) ClientLoader.getInstance().getClientEntrypoint();
        this.logger.info("Start BThack preLaunch init...");
        long startInitTime = System.currentTimeMillis();

        this.logger.info("Register main task provider stages...");

        this.initProvider.registerLast(InitStageImpl.of("Create executor", () -> this.executor = Executors.newSingleThreadExecutor()));
        this.initProvider.registerLast(BThackRenderSystem.createInitStage());
        this.initProvider.registerLast(InitStageImpl.of("Init categories", () -> {
            this.categoryManager = new CategoryManager();

            this.categoryManager.register(new BThackCategory("Combat", "combat", BThackRenderSystem.TEXTURES.COMBAT_CATEGORY_ICON));
            this.categoryManager.register(new BThackCategory("Render", "render", BThackRenderSystem.TEXTURES.RENDER_CATEGORY_ICON));
            this.categoryManager.register(new BThackCategory("Player", "player", BThackRenderSystem.TEXTURES.PLAYER_CATEGORY_ICON));
            this.categoryManager.register(new BThackCategory("Misc", "misc", BThackRenderSystem.TEXTURES.MISC_CATEGORY_ICON));
        }));
        this.initProvider.registerLast(InitStageImpl.of("Init modules", () -> {
            this.bindManager = new BindManager();
            this.eventBus.register(this.bindManager);

            Modules.getModules().forEach(module ->
                    this.categoryManager.registerModule(module)
            );

            //TODO remove later
            Modules.test.setBind(new Bind(GLFW.GLFW_KEY_K, BindType.Toggle, BindController.Keyboard));
        }));
        this.initProvider.registerLast(InitStageImpl.of("Freeze managers", () -> {
            categoryManager.freeze();
        }));
        this.initProvider.registerLast(InitStageImpl.of("Create texture atlases", () -> BThackRenderSystem.TEXTURE_ATLASES = new BThackTextureAtlases()));
        this.initProvider.registerLast(InitStageImpl.of("Init UI screen", () -> uiScreen = new UIScreen()));

        this.logger.info("BThack preLaunch initialization completed in {} ms.", System.currentTimeMillis() - startInitTime);
    }

    @Override
    public void init() {
        this.logger.info("Start BThack main init...");
        long startInitTime = System.currentTimeMillis();

        this.initProvider.start();

        this.logger.info("BThack main initialization completed in {} ms.", System.currentTimeMillis() - startInitTime);
    }

    public static boolean nullCheck() {
        return mc.player == null || mc.level == null;
    }
}
