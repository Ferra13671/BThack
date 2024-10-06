package com.ferra13671.BThack.api.Gui.ActionBot.Edit;


import com.ferra13671.BThack.api.Utils.System.buttons.NumberFrameButton;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks.WaitTask;
import com.ferra13671.BThack.api.Gui.ActionBot.Add.AddingWaitTaskGui;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.TaskButton;

public class EditWaitTaskGui extends AddingWaitTaskGui {
    private final TaskButton taskButton;

    public EditWaitTaskGui(TaskButton taskButton) {
        this.taskButton = taskButton;
    }

    @Override
    public ActionBotTask getAddingTask() {
        NumberFrameButton frameButton = (NumberFrameButton) getButtonFromId(1);

        ActionBotConfig.tasks.set(taskButton.getId(), new WaitTask(frameButton.getNumber()));
        return null;
    }
}
