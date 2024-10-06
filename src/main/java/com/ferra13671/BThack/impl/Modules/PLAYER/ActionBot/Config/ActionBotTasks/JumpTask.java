package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks;


import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;

import java.util.ArrayList;
import java.util.Arrays;

public class JumpTask extends ActionBotTask {

    public JumpTask() {
        super("Jump");
        this.mode = "Jump";

        this.taskDescription = new ArrayList<>(Arrays.asList(
                "When the task is activated, the player starts jumping."
        ));

    }


    @Override
    public void play() {
        mc.player.jump();
        sleepThread(50);

        while (!mc.player.verticalCollision) {
            sleepThread(50);
            Thread.yield();
        }
    }
}
