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
            if (e.getKeyCode() != KeyboardUtils.RELEASE && mc.currentScreen == null) {
                Client.keyPress(e.getKeyCode());
            }

            if (!KeyboardUtils.getActiveKeys().contains(e.getKeyCode())) {
                KeyboardUtils.addActiveKey(e.getKeyCode());
            }
        } else if (e.getAction() == InputEvent.KeyInputEvent.Action.RELEASE) {
            if (KeyboardUtils.getActiveKeys().contains(e.getKeyCode())) {
                KeyboardUtils.removeActiveKey(e.getKeyCode());
            }
        }
    }
}
