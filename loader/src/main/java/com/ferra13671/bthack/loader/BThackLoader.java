package com.ferra13671.bthack.loader;

import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BThackLoader implements PreLaunchEntrypoint {
    private static BThackLoader instance;
    private final Logger logger = LoggerFactory.getLogger("BThack Loader");
    private boolean isIDE = false;

    @Override
    public void onPreLaunch() {
        instance = this;
    }

    private void checkIsIDE() {
        this.isIDE = Boolean.parseBoolean(System.getProperty("bthack-loader.ide", "false"));
    }

    public static BThackLoader getInstance() {
        return instance;
    }
}
