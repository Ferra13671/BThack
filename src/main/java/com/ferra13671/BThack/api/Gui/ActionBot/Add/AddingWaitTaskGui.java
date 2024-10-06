package com.ferra13671.BThack.api.Gui.ActionBot.Add;

import com.ferra13671.BThack.api.Gui.ActionBot.AbstractAddingTaskGui;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.TaskSettingButton;
import com.ferra13671.BThack.api.Utils.System.buttons.NumberFrameButton;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks.WaitTask;

import java.util.List;

import static com.ferra13671.BThack.api.Gui.ActionBot.ActionBotConfigGui.*;

public class AddingWaitTaskGui extends AbstractAddingTaskGui {

    @Override
    public List<TaskSettingButton> getSettingButtons() {
        return List.of(
                new TaskSettingButton(new NumberFrameButton(1, (scaledResolution.getScaledWidth() / 2), (int) ((scaledResolution.getScaledHeight() / 2) - (heightFactor * 2.5)), (int) (widthFactor * 8.5), (int) heightFactor), "Seconds")
        );
    }

    @Override
    public ActionBotTask getAddingTask() {
        NumberFrameButton frameButton = (NumberFrameButton) getButtonFromId(1);
        return new WaitTask(frameButton.getNumber());
    }
}
