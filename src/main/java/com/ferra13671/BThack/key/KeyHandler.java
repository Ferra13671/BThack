package com.ferra13671.BThack.api.key;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Events.Events.InputEvent;
import com.ferra13671.BThack.Events.MegaEvents.EventSubscriber;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.Keyboard.KeyboardUtils;

public class KeyHandler implements Mc {

    @EventSubscriber
    public void onKey(InputEvent.KeyInputEvent e) {
        if (e.getAction() == InputEvent.KeyInputEvent.Action.PRESS) {
            if (e.getKey() != KeyboardUtils.RELEASE && mc.currentScreen == null) {
                Client.keyPress(e.getKey());
            }

            if (!KeyboardUtils.getActiveKeys().contains(e.getKey())) {
                KeyboardUtils.addActiveKey(e.getKey());
            }
        } else if (e.getAction() == InputEvent.KeyInputEvent.Action.RELEASE) {
            if (KeyboardUtils.getActiveKeys().contains(e.getKey())) {
                KeyboardUtils.removeActiveKey(e.getKey());
            }
        }
    }
}
