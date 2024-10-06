package com.ferra13671.BThack.api.Gui.ActionBot;

import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.api.Utils.System.BThackScreen;
import com.ferra13671.BThack.api.Utils.System.buttons.Button;
import com.ferra13671.BThack.api.Utils.System.buttons.NumberFrameButton;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.ActionBotConfig;
import com.ferra13671.BThack.impl.Modules.PLAYER.ActionBot.Config.Utils.TaskButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;

import java.awt.*;

import static com.ferra13671.BThack.api.Gui.ActionBot.ActionBotConfigGui.*;

public class MoveTaskGui extends BThackScreen {
    private final TaskButton taskButton;

    public MoveTaskGui(TaskButton taskButton) {
        super(Text.of("MoveTask"));
        this.taskButton = taskButton;
    }

    @Override
    public void init() {
        this.buttons.clear();

        this.buttons.add(new NumberFrameButton(1, (scaledResolution.getScaledWidth() / 2), (int) ((scaledResolution.getScaledHeight() / 2) - (heightFactor * 2.5)), (int) (widthFactor * 8.5), (int) heightFactor));
        this.buttons.add(new Button(-1, (scaledResolution.getScaledWidth() / 2), ((scaledResolution.getScaledHeight() / 2)), (int) (widthFactor * 8.5), (int) heightFactor, "Confirm"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        drawConfigBackGround();

        BThackRender.drawString("New task number", (int) ((scaledResolution.getScaledWidth() / 2) - (mc.textRenderer.getWidth("New task number") / 2)), (int) ((scaledResolution.getScaledHeight() / 2) - (heightFactor * 4.5)), Color.white.hashCode());

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (activeButton.getId() == -1) {
            NumberFrameButton numberButton = (NumberFrameButton) getButtonFromId(1);

            ActionBotConfig.tasks.remove(this.taskButton.getId());
            ActionBotConfig.tasks.add((int) Math.min(ActionBotConfig.tasks.size() - 2, numberButton.getNumber()), taskButton.task);

            mc.setScreen(new ActionBotConfigGui());
        }

        return false;
    }
}
