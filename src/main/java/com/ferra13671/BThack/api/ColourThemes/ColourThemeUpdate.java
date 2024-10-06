package com.ferra13671.BThack.api.ColourThemes;


import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Events.Events.TickEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.impl.Modules.CLIENT.ClickGui;

import java.util.Objects;

public class ColourThemeUpdate {


    @EventSubscriber
    public void onTheme(TickEvent.ClientTickEvent e) {
        for (ColourTheme theme : BThack.instance.colourThemeManager.getColourThemes()) {
            if (Objects.equals(ClickGui.activeTheme.getValue(), theme.getName())) {
                Client.colourTheme = theme;
            }
        }
    }
}
