package com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config;

import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.ActionBotTaskData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ActionBotConfig {
    public static final ActionBotTask startTask = new ActionBotTask("Start");
    public static final ActionBotTask endTask = new ActionBotTask("End");

    public static ArrayList<ActionBotTask> tasks = new ArrayList<>(Arrays.asList(
            startTask,
            endTask
    ));

    private static final ArrayList<ActionBotTaskData> actionBotTasks = new ArrayList<>();

    public static void addFullActionBotTask(ActionBotTaskData actionBotTaskData) {
        actionBotTasks.add(actionBotTaskData);
    }

    public static List<ActionBotTaskData> getFullActionBotTasks() {
        return actionBotTasks;
    }


    public static void addActionTaskToList(ActionBotTask task) {
        tasks.remove(endTask);
        tasks.add(task);
        tasks.add(endTask);
    }

    public static void removeActionTaskFromList() {
        tasks.remove(endTask);
        tasks.remove(tasks.size() - 1);
        tasks.add(endTask);
    }
}
