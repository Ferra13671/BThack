package com.ferra13671.bthack.managers.bind;

import com.ferra13671.MegaEvents.eventbus.EventSubscriber;
import com.ferra13671.bthack.BThackClient;
import com.ferra13671.bthack.events.KeyInputEvent;
import com.ferra13671.bthack.events.MouseInputEvent;
import com.ferra13671.bthack.utils.Mc;

import java.util.ArrayList;
import java.util.List;

public class BindManager implements Mc {
    private final List<BindListener> keyboardListeners = new ArrayList<>();
    private final List<BindListener> mouseListeners = new ArrayList<>();

    @EventSubscriber(event = KeyInputEvent.class)
    public void onKey(KeyInputEvent e) {
        if (BThackClient.nullCheck() || mc.screen != null) return;

        if (!this.keyboardListeners.isEmpty())
            for (BindListener listener : this.keyboardListeners)
                if (listener.bind().getKey() == e.key)
                    listener.onKey(e.action);
    }

    @EventSubscriber(event = MouseInputEvent.class)
    public void onMouse(MouseInputEvent e) {
        if (BThackClient.nullCheck() || mc.screen != null) return;

        if (!this.mouseListeners.isEmpty())
            for (BindListener listener : this.mouseListeners)
                if (listener.bind().getKey() == e.button)
                    listener.onKey(e.action);
    }

    public void register(BindListener bindListener) {
        if (bindListener.bind().getController() == BindController.Keyboard)
            this.keyboardListeners.add(bindListener);
        else
            this.mouseListeners.add(bindListener);

        if (bindListener.bind().getType() == BindType.HoldReversed)
            bindListener.activateConsumer().accept(true);
    }

    public void unregister(BindListener bindListener) {
        if (bindListener.bind().getController() == BindController.Keyboard)
            this.keyboardListeners.remove(bindListener);
        else
            this.mouseListeners.remove(bindListener);
    }
}
