package com.ferra13671.BThack.api.Gui.ActionBot.Edit;


import com.ferra13671.BThack.api.Utils.System.buttons.TextFrameButton;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks.SendMessageTask;
import com.ferra13671.BThack.api.Gui.ActionBot.Add.AddingSendMessageTask;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.TaskButton;

public class EditSendMessageTaskGui extends AddingSendMessageTask {
    private final TaskButton taskButton;

    public EditSendMessageTaskGui(TaskButton taskButton) {
        this.taskButton = taskButton;
    }

    @Override
    public ActionBotTask getAddingTask() {
        TextFrameButton frameButton = (TextFrameButton) getButtonFromId(1);

        ActionBotConfig.tasks.set(taskButton.getId(), new SendMessageTask(frameButton.getText()));
        return null;
    }
}
