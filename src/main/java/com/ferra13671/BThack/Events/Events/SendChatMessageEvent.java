package com.ferra13671.BThack.Events.Events;

import com.ferra13671.BThack.Events.MegaEvents.Event;

public class SendChatMessageEvent extends Event {
    private final String content;

    public SendChatMessageEvent(String content) {
        this.content = content;
    }

    public String getMessage() {
        return content;
    }
}
