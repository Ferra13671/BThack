package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks;


import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;

import java.util.ArrayList;
import java.util.Arrays;

public class SendMessageTask extends ActionBotTask {
    private final String message;

    public SendMessageTask(String message) {
        super("Send Message");
        this.mode = "SendMessage";

        this.message = message;

        this.taskDescription = new ArrayList<>(Arrays.asList(
                "When a task is activated, the player sends the desired message to the chat.",
                "This task only supports the English language."
        ));
    }

    @Override
    public void play() {
        ChatUtils.sendChatMessage(this.getMessage());
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public String getButtonName() {
        return getName() + ": " + getMessage();
    }
}
