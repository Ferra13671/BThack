package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config;


import com.ferra13671.BThack.api.Interfaces.Mc;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class ActionBotTask implements Mc {
    private String name;
    public List<String> taskDescription = new ArrayList<>(Collections.singletonList("NULL"));

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

    public List<String> getTaskDescription() {
        return this.taskDescription;
    }


    public boolean isStartOrEndTask() {
        return this.name.equalsIgnoreCase("Start") || this.name.equalsIgnoreCase("End");
    }

    public abstract void save(JsonObject jsonObject);
    public abstract void load(JsonObject jsonObject);
}
