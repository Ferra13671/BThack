package com.ferra13671.BThack.api.Gui.ActionBot.Edit;


import com.ferra13671.BThack.api.Utils.System.buttons.ModeButton;
import com.ferra13671.BThack.api.Utils.System.buttons.NumberFrameButton;
import com.ferra13671.BThack.api.Utils.System.buttons.SwitchButton;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks.MoveTask;
import com.ferra13671.BThack.api.Gui.ActionBot.Add.AddingMoveTaskGui;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.TaskButton;

public class EditMoveTaskGui extends AddingMoveTaskGui {
    private final TaskButton taskButton;

    public EditMoveTaskGui(TaskButton taskButton) {
        this.taskButton = taskButton;
    }

    @Override
    public ActionBotTask getAddingTask() {
        NumberFrameButton xFrame = (NumberFrameButton) getButtonFromId(1);
        NumberFrameButton zFrame = (NumberFrameButton) getButtonFromId(2);

        SwitchButton scaffoldButton = (SwitchButton) getButtonFromId(3);
        ModeButton modeButton = (ModeButton) getButtonFromId(4);

        MoveTask.Type moveType = modeButton.getsVal().equals("Default") ? MoveTask.Type.Default : modeButton.getsVal().equals("With AutoJump") ? MoveTask.Type.AutoJump : MoveTask.Type.Through_Obstacles;

        ActionBotConfig.tasks.set(taskButton.getId(), new MoveTask(xFrame.getNumber(), zFrame.getNumber(), scaffoldButton.getBVal(), moveType));
        return null;
    }
}
