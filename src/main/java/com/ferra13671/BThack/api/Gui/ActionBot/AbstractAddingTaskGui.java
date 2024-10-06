package com.ferra13671.BThack.api.Gui.ActionBot;

import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotTask;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.TaskSettingButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;


import java.util.ArrayList;
import java.util.List;

import static com.ferra13671.BThack.api.Gui.ActionBot.ActionBotConfigGui.*;
import static org.lwjgl.opengl.GL11.*;

public abstract class AbstractAddingTaskGui extends BThackScreen {

    public final ArrayList<TaskSettingButton> taskSettingButtons = new ArrayList<>();


    protected AbstractAddingTaskGui() {
        super(Text.of("Adding Task"));
    }

    @Override
    protected void init() {
        widthFactor = scaledResolution.getScaledWidth() / 37D;
        heightFactor = scaledResolution.getScaledHeight() / 30D;

        taskSettingButtons.clear();
        taskSettingButtons.addAll(getSettingButtons());

        buttons.clear();
        taskSettingButtons.forEach(taskSettingButton -> buttons.add(taskSettingButton.button));
        buttons.add(new Button(-1, mc.getWindow().getScaledWidth() - 40, mc.getWindow().getScaledHeight() - 30, 30, 15, "Confirm"));
    }

    public abstract List<TaskSettingButton> getSettingButtons();

    public abstract ActionBotTask getAddingTask();

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawConfigBackGround();

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        taskSettingButtons.forEach(taskSettingButton -> {
            taskSettingButton.button.updateButton(mouseX, mouseY);
            taskSettingButton.render();
        });
        glDisable(GL_BLEND);

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (activeButton.getId() == -1) {
            AddingTaskGui.addTask(getAddingTask());
        }
        return false;
    }
}
