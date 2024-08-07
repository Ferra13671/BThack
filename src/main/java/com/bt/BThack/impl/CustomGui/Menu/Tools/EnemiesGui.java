package com.bt.BThack.impl.CustomGui.Menu.Tools;

import com.bt.BThack.api.Social.Enemies.EnemiesUtils;
import com.bt.BThack.api.Utils.System.BThackGui;
import com.bt.BThack.api.Utils.System.Buttons.Button;
import com.bt.BThack.impl.CustomGui.Menu.BThackSettingsGui;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class EnemiesGui extends BThackGui {
    GuiTextField inputField;

    @Override
    public void initGui() {
        int i = this.height / 4 + 48;

        this.buttons.clear();
        inputField = new GuiTextField(1, fontRenderer, this.width / 2 - 100, i + 60, 200, 20);

        inputField.setText("Enemy");

        this.buttons.add(new Button(1, width / 2 - 51, i + 94, 49, 10, "Add Enemy"));
        this.buttons.add(new Button(2, width / 2 + 51, i + 94, 49, 10, "Remove Enemy"));
    }


    @Override
    public void updateScreen() {
        inputField.updateCursorCounter();
    }

    @Override
    protected void keyTyped(char charKey, int p_73869_2_) {
        if (p_73869_2_ == Keyboard.KEY_ESCAPE) {
            if (mc.player != null) {
                mc.displayGuiScreen(null);
            } else {
                mc.displayGuiScreen(new BThackSettingsGui());
            }
        } else {
            inputField.textboxKeyTyped(charKey, p_73869_2_);
        }
    }

    @Override
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_) throws IOException {
        inputField.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);

        if (activeButton.getId() == 1) {
            EnemiesUtils.addEnemy(inputField.getText());
        }
        if (activeButton.getId() == 2) {
            EnemiesUtils.removeEnemy(inputField.getText());
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GL11.glColor4f(1, 1, 1, 1);

        if (mc.player != null) {
            drawDefaultBackground();
        } else {
            mc.renderEngine.bindTexture(ChangeNicknameGui.MainTexture);
            Gui.drawScaledCustomSizeModalRect(0,0,0,0, this.width, this.height, this.width, this.height, this.width, this.height);
        }

        inputField.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
