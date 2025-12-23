package com.ferra13671.bthack.loader;

import com.ferra13671.bthack.loader.api.ClientLoader;
import lombok.SneakyThrows;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class LaunchEntrypoint implements PreLaunchEntrypoint, ClientModInitializer {

    @Override
    @SneakyThrows
    public void onPreLaunch() {
        BThackLoader loader = new BThackLoader();
        ClientLoader.setInstance(loader);
        loader.load();

        loader.getClientEntrypoint().preLaunch();
    }

    @Override
    @SneakyThrows
    public void onInitializeClient() {
        ClientLoader.getInstance().getClientEntrypoint().init();
    }
}
