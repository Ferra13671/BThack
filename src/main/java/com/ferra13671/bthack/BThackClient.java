package com.ferra13671.bthack;

import com.ferra13671.MegaEvents.eventbus.IEventBus;
import com.ferra13671.MegaEvents.eventbus.impl.EventBus;
import com.ferra13671.bthack.features.category.BThackCategory;
import com.ferra13671.bthack.features.category.CategoryManager;
import com.ferra13671.bthack.features.module.Modules;
import com.ferra13671.bthack.init.InitStage;
import com.ferra13671.bthack.loader.api.ClientEntrypoint;
import com.ferra13671.bthack.loader.api.ClientLoader;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BThackClient implements ClientEntrypoint {
    @Getter
    private static BThackClient INSTANCE;
    @Getter
    private final Logger logger = LoggerFactory.getLogger("BThack");
    @Getter
    private final IEventBus eventBus = new EventBus();
    @Getter
    private final InitStage initProvider = InitStage.of("BThack init provider");
    @Getter
    private CategoryManager categoryManager;

    @Override
    public void preLaunch() {
        INSTANCE = (BThackClient) ClientLoader.getInstance().getClientEntrypoint();
        this.logger.info("Start BThack preLaunch init...");
        long startInitTime = System.currentTimeMillis();

        this.logger.info("Register main task provider stages...");
        this.initProvider.registerLast(InitStage.of("Init categories", () -> {
            this.categoryManager = new CategoryManager();

            //TODO Category icons
            this.categoryManager.register(new BThackCategory("Combat", "combat", null));
            this.categoryManager.register(new BThackCategory("Render", "render", null));
            this.categoryManager.register(new BThackCategory("Movement", "movement", null));
            this.categoryManager.register(new BThackCategory("Misc", "misc", null));
        }));
        this.initProvider.registerLast(InitStage.of("Init modules", () ->
            Modules.getModules().forEach(module ->
                this.categoryManager.registerModule(module)
            )
        ));

        this.logger.info("BThack preLaunch initialization completed in {} ms.", System.currentTimeMillis() - startInitTime);
    }

    @Override
    public void init() {
        this.logger.info("Start BThack main init...");
        long startInitTime = System.currentTimeMillis();

        this.initProvider.start();

        this.logger.info("BThack main initialization completed in {} ms.", System.currentTimeMillis() - startInitTime);
    }
}
