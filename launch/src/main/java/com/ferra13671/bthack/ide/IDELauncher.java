package com.ferra13671.bthack.ide;

import com.ferra13671.bthack.loader.BThackLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class IDELauncher implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
        System.setProperty("bthack-loader.ide", "true");

        BThackLoader bthackLoader = new BThackLoader();
        bthackLoader.onPreLaunch();
    }
}
