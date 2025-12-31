package com.ferra13671.bthack.features.module.impl;

import com.ferra13671.MegaEvents.eventbus.EventSubscriber;
import com.ferra13671.bthack.events.Render2DEvent;
import com.ferra13671.bthack.features.module.BThackModule;
import com.ferra13671.bthack.features.module.ModuleInfo;
import com.ferra13671.bthack.utils.Mc;

@ModuleInfo(name = "Test Module", category = "misc")
public class TestModule extends BThackModule implements Mc {


    @EventSubscriber(event = Render2DEvent.class)
    public void onRender2D() {

    }
}
