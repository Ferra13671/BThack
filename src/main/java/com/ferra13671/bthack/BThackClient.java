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
import com.ferra13671.bthack.utils.Mc;
import lombok.Getter;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BThackClient implements ClientEntrypoint, Mc {
    @Getter
    private static BThackClient instance;
    @Getter
    private static final float clientScale = 2f;
    @Getter
    private final Logger logger = LoggerFactory.getLogger("BThack");
    @Getter
    private final IEventBus eventBus = new EventBus();
    @Getter
    private final InitStageImpl initProvider = InitStageImpl.of("BThack main stage");
    @Getter
    private CategoryManager categoryManager;
    @Getter
    private BindManager bindManager;

    @Override
    public void preLaunch() {
        instance = (BThackClient) ClientLoader.getInstance().getClientEntrypoint();
        this.logger.info("Start BThack preLaunch init...");
        long startInitTime = System.currentTimeMillis();

        this.logger.info("Register main task provider stages...");

        this.initProvider.registerLast(BThackRenderSystem.createInitStage());
        this.initProvider.registerLast(InitStageImpl.of("Init categories", () -> {
            this.categoryManager = new CategoryManager();

            //TODO Category icons
            this.categoryManager.register(new BThackCategory("Combat", "combat", null));
            this.categoryManager.register(new BThackCategory("Render", "render", null));
            this.categoryManager.register(new BThackCategory("Movement", "movement", null));
            this.categoryManager.register(new BThackCategory("Misc", "misc", null));
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
