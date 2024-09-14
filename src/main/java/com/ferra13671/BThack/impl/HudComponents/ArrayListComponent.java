package com.ferra13671.BThack.impl.HudComponents;

import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Core.Render.BThackRender;
import com.ferra13671.BThack.Core.Render.ColourUtils;
import com.ferra13671.BThack.api.HudComponent.HudComponent;
import com.ferra13671.BThack.api.Managers.Setting.Settings.BooleanSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.NumberSetting;
import com.ferra13671.BThack.api.Managers.Setting.Settings.Setting;
import com.ferra13671.BThack.api.Module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Formatting;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ArrayListComponent extends HudComponent {

    private final BooleanSetting drawRects = addCheckbox("Draw Rects", true);
    private final BooleanSetting rainbow = addCheckbox("ArrayList rainbow", true);
    private final NumberSetting rainbowType = addSlider("Rainbow type", 3, 1, 8, true);

    public ArrayListComponent() {
        super("ArrayList",
                MinecraftClient.getInstance().getWindow().getScaledWidth(),
                5,
                true
        );
    }

    @Override
    public List<Setting> initSettings() {
        return Arrays.asList(
                drawRects,
                rainbow,
                rainbowType
        );
    }

    @Override
    public void render(RenderType t) {
        if (nullCheck()) return;

        int y = (int) this.getY();
        int yHeight = 0;

        int type = (int) rainbowType.getValue();
        final int[] counter = {1};



        ArrayList<Module> enabledModules = new ArrayList<>();

        for (Module module : Client.getAllModules()) {
            if (module.toggled) {
                enabledModules.add(module);
            }
        }

        enabledModules.sort((module1, module2) -> mc.textRenderer.getWidth(!module2.arrayListInfo.isEmpty() ? (module2.name + " " + "[" + module2.arrayListInfo + "]") : module2.name) - mc.textRenderer.getWidth(!module1.arrayListInfo.isEmpty() ? (module1.name + " " + "[" + module1.arrayListInfo + "]") : module1.name));

        float maxLength = 0;

        for (Module module : enabledModules) {
            if (module.toggled && module.visible) {
                String text = !module.arrayListInfo.isEmpty() ? module.name + " " + "[" + module.arrayListInfo + "]" : module.name;
                String text2 = !module.arrayListInfo.isEmpty() ? module.name + " " + Formatting.GRAY + "[" + Formatting.WHITE +  module.arrayListInfo + Formatting.GRAY + "]" : module.name;

                if (maxLength < mc.textRenderer.getWidth(text)) {
                    maxLength = mc.textRenderer.getWidth(text);
                }

                if (rainbow.getValue()) {
                    if (drawRects.getValue())
                        BThackRender.drawRect((int) this.getX(), y, (int) this.getX() - 2, y + 10, ColourUtils.rainbowType(type, counter[0]));
                    drawText(text2, (int) (this.getX() - 4 - mc.textRenderer.getWidth(text)), y, ColourUtils.rainbowType(type, counter[0]));
                } else {
                    if (drawRects.getValue())
                        BThackRender.drawRect((int) this.getX(), y, (int) this.getX() - 2, y + 10, (new Color(Client.colourTheme.getArrayListColour())).hashCode());
                    drawText(text2, ((int) this.getX() - 4 - mc.textRenderer.getWidth(text)), y, (new Color(Client.colourTheme.getArrayListColour())).hashCode());
                }

                y += 10;
                yHeight += 10;
                counter[0]++;
            }
        }

        this.width = -(7 + maxLength);
        this.height = yHeight;
    }
}
