package com.ferra13671.BThack.api.Module;

import com.ferra13671.BThack.api.Plugin.Plugin;

public class PluginModule extends Module {

    public final Plugin plugin;

    public PluginModule(String name, String descriptionEN, int key, Category c, boolean autoEnabled, Plugin plugin) {
        super(name, descriptionEN, key, c, autoEnabled);

        this.plugin = plugin;
    }
}
