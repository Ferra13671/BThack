package com.ferra13671.bthack.features.module.impl;

import com.ferra13671.MegaEvents.eventbus.EventSubscriber;
import com.ferra13671.bthack.BThackClient;
import com.ferra13671.bthack.events.TickEvent;
import com.ferra13671.bthack.features.module.BThackModule;
import com.ferra13671.bthack.features.module.ModuleInfo;

@ModuleInfo(name = "Test Module", category = "misc")
public class TestModule extends BThackModule {

    @EventSubscriber(event = TickEvent.class)
    public void onTick() {
        BThackClient.getINSTANCE().getLogger().info("Test module passed!");
    }
}
