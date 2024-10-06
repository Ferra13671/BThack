package com.ferra13671.BThack.api.Gui.ActionBot.Add;



import com.ferra13671.BThack.api.Gui.ActionBot.AbstractAddingTaskGui;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.TaskSettingButton;
import com.ferra13671.BThack.api.Utils.System.buttons.ModeButton;
import com.ferra13671.BThack.api.Utils.System.buttons.NumberFrameButton;
import com.ferra13671.BThack.api.Utils.System.buttons.SwitchButton;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTasks.MoveTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ferra13671.BThack.api.Gui.ActionBot.ActionBotConfigGui.*;

public class AddingMoveTaskGui extends AbstractAddingTaskGui {

    @Override
    public List<TaskSettingButton> getSettingButtons() {
        return Arrays.asList(
                new TaskSettingButton(new NumberFrameButton(1, 60, scaledResolution.getScaledHeight() / 2 - 46, 40, 10), "X (Relative to the player)"),
                new TaskSettingButton(new NumberFrameButton(2, 60, scaledResolution.getScaledHeight() / 2 - 24, 40, 10), "Y (Relative to the player)"),
                new TaskSettingButton(new SwitchButton(3, 60, scaledResolution.getScaledHeight() / 2 - 2, 40, 10, false, "Scaffold"), ""),
                new TaskSettingButton(new ModeButton(4, 90, scaledResolution.getScaledHeight() / 2 + 20, 70, 10, "Mode", new ArrayList<>(Arrays.asList("Default", "With AutoJump", "Through Obstacles"))), "")
        );
    }

    @Override
    public ActionBotTask getAddingTask() {
        NumberFrameButton xFrame = (NumberFrameButton) getButtonFromId(1);
        NumberFrameButton zFrame = (NumberFrameButton) getButtonFromId(2);

        SwitchButton scaffoldButton = (SwitchButton) getButtonFromId(3);
        ModeButton modeButton = (ModeButton) getButtonFromId(4);

        MoveTask.Type moveType = modeButton.getsVal().equals("Default") ? MoveTask.Type.Default : modeButton.getsVal().equals("With AutoJump") ? MoveTask.Type.AutoJump : MoveTask.Type.Through_Obstacles;


        return new MoveTask(xFrame.getNumber(), zFrame.getNumber(), scaffoldButton.getBVal(), moveType);
    }
}
