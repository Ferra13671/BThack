package com.ferra13671.bthack;

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
    private final InitStage initProvider = InitStage.of("BThack init provider");

    @Override
    public void preLaunch() {
        INSTANCE = (BThackClient) ClientLoader.getInstance().getClientEntrypoint();
        this.logger.info("Start BThack preLaunch init...");
        long startInitTime = System.currentTimeMillis();



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
