package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks;

import com.ferra13671.BThack.api.Managers.Destroy.DestroyManager;
import com.ferra13671.BThack.api.Managers.Destroy.SimpleDestroyThread;
import com.ferra13671.BThack.api.Utils.ModifyBlockPos;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;

public class BreakTask extends ActionBotTask {

    private final int x;
    private final int y;
    private final int z;

    public BreakTask(int x, int y, int z) {
        super("Break");
        mode = "Break";

        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void play() {
        SimpleDestroyThread simpleDestroyThread = new SimpleDestroyThread(new ModifyBlockPos(mc.player.getX() + x, mc.player.getY() + y, mc.player.getZ() + z));
        simpleDestroyThread.start();
        while (DestroyManager.isDestroying) {
            sleepThread(50);
        }
    }


}
