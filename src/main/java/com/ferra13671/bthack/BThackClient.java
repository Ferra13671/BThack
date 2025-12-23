package com.ferra13671.bthack;

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

    @Override
    public void preLaunch() {
        logger.info("BThack preLaunch!");
        INSTANCE = (BThackClient) ClientLoader.getInstance().getClientEntrypoint();
    }

    @Override
    public void init() {
        logger.info("BThack init!");
    }
}
