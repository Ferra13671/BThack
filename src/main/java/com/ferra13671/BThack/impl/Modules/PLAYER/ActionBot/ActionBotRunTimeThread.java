package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot;


import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.api.Utils.ChatUtils;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import net.minecraft.util.Formatting;

public class ActionBotRunTimeThread extends Thread {
    public static boolean isPlaying = false;

    @Override
    public void run() {
        ChatUtils.sendMessage("[ActionBot] "  + Formatting.AQUA + "Starting to reproduce the task chain.");

        if (ActionBot.repeat.getValue()) {
            while (Client.getModuleByName("ActionBot").isEnabled() && ActionBot.repeat.getValue()) {
                play();
            }

            Client.getModuleByName("ActionBot").setToggled(false);
        } else {
            play();

            Client.getModuleByName("ActionBot").setToggled(false);
        }
    }




    public void play() {
        isPlaying = true;

        for (ActionBotTask task : ActionBotConfig.tasks) {
            if (!task.isStartOrEndTask()) {
                task.thread = this;

                task.play();
            }
        }

        isPlaying = false;
    }
}
