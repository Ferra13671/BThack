package com.ferra13671.BThack.api.Gui.ActionBot.Edit;


import com.ferra13671.BThack.api.Utils.System.buttons.ModeButton;
import com.ferra13671.BThack.api.Utils.System.buttons.NumberFrameButton;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks.TunnelTask;
import com.ferra13671.BThack.api.Gui.ActionBot.Add.AddingTunnelTaskGui;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.TaskButton;

public class EditTunnelTaskGui extends AddingTunnelTaskGui {
   private final TaskButton taskButton;

    public EditTunnelTaskGui(TaskButton taskButton) {
        this.taskButton = taskButton;
    }

    @Override
    public ActionBotTask getAddingTask() {
        ModeButton directionButton = (ModeButton) getButtonFromId(1);
        NumberFrameButton lengthButton = (NumberFrameButton) getButtonFromId(2);

        TunnelTask.Direction direction = switch (directionButton.getsVal()) {
            case "X+" -> TunnelTask.Direction.X_PLUS;
            case "X-" -> TunnelTask.Direction.X_MINUS;
            case "Z+" -> TunnelTask.Direction.Z_PLUS;
            default -> TunnelTask.Direction.Z_MINUS;
        };

        ActionBotConfig.tasks.set(taskButton.getId(), new TunnelTask(direction, lengthButton.getNumber()));
        return null;
    }
}
