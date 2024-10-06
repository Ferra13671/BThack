package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config;


import com.ferra13671.BThack.api.Interfaces.Mc;

import java.util.ArrayList;
import java.util.Collections;

public class ActionBotTask implements Mc {
    private String name;
    public ArrayList<String> taskDescription = new ArrayList<>(Collections.singletonList("NULL"));

    public String mode = "";

    public Thread thread;


    public ActionBotTask(String name) {
        this.name = name;
    }

    //Starts playing the task
    public void play() {
        while (!isConditionsAreMet()) {
            startDoing();
        }
    }


    public void sleepThread(long millis) {
        try {
            thread.sleep(millis);
        } catch (InterruptedException e) {}
    }

    //Checks if the condition is met
    public boolean isConditionsAreMet() {
        return false;
    }

    //Task action
    public void startDoing() {

    }


    public String getName() {
        return this.name;
    }

    public String getButtonName() {
        return getName();
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getTaskDescription() {
        return this.taskDescription;
    }

    public static boolean isMoveTask(String mode) {
        return mode.equalsIgnoreCase("Move");
    }
    public static boolean isJumpTask(String mode) {
        return mode.equalsIgnoreCase("Jump");
    }
    public static boolean isSendMessageTask(String mode) {
        return mode.equalsIgnoreCase("SendMessage");
    }
    public static boolean isTunnelTask(String mode) {
        return mode.equalsIgnoreCase("Tunnel");
    }
    public static boolean isWaitTask(String mode) {
        return mode.equalsIgnoreCase("Wait");
    }


    public boolean isStartOrEndTask() {
        return this.name.equalsIgnoreCase("Start") || this.name.equalsIgnoreCase("End");
    }
}
