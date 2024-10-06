package com.ferra13671.BThack.api.Gui.ActionBot.Add;


import com.ferra13671.BThack.api.Gui.ActionBot.AbstractAddingTaskGui;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.TaskSettingButton;
import com.ferra13671.BThack.api.Utils.System.buttons.ModeButton;
import com.ferra13671.BThack.api.Utils.System.buttons.NumberFrameButton;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks.TunnelTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ferra13671.BThack.api.Gui.ActionBot.ActionBotConfigGui.*;

public class AddingTunnelTaskGui extends AbstractAddingTaskGui {
    @Override
    public List<TaskSettingButton> getSettingButtons() {
        return Arrays.asList(
                new TaskSettingButton(new ModeButton(1, 60, scaledResolution.getScaledHeight() / 2 - 2, 40, 10, "Direction",
                        new ArrayList<>(Arrays.asList(
                                "X+",
                                "X-",
                                "Z+",
                                "Z-"
                        ))) ,""),
                new TaskSettingButton(new NumberFrameButton(2, 60, scaledResolution.getScaledHeight() / 2 + 20, 40, 10), "Length")
        );
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

        return new TunnelTask(direction, lengthButton.getNumber());
    }
}
