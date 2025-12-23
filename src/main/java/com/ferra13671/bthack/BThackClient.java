package com.ferra13671.bthack;

import com.ferra13671.bthack.loader.api.ClientEntrypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BThackClient implements ClientEntrypoint {
    private final Logger logger = LoggerFactory.getLogger("BThack");

    @Override
    public void preLaunch() {
        logger.info("BThack preLaunch!");
    }

    @Override
    public void init() {
        logger.info("BThack init!");
    }
}
