package com.ferra13671.bthack.render.drawer.impl.text;

import com.ferra13671.bthack.render.RenderColor;
import com.ferra13671.gltextureutils.Pair;
import lombok.Getter;
import net.minecraft.ChatFormatting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class FormattedText {
    @Getter
    private final List<Pair<Supplier<RenderColor>, Character[]>> components = new ArrayList<>();

    public FormattedText(String s) {
        RenderColor currentColor = RenderColor.WHITE;
        boolean listenFormat = false;

        List<Character> characters = new ArrayList<>();

        for (char _char : s.toCharArray()) {
            if (_char == '§') {
                listenFormat = true;
                continue;
            }

            if (listenFormat) {
                ChatFormatting formatting = ChatFormatting.getByCode(_char);

                RenderColor c = formatting != null ? formatting.isColor() ? RenderColor.ofRGB(formatting.getColor()) : formatting == ChatFormatting.RESET ? RenderColor.WHITE : null : null;

                if (c != null) {
                    RenderColor color = currentColor;
                    this.components.add(new Pair<>(() -> color, characters.toArray(new Character[0])));
                    characters.clear();

                    currentColor = c;
                }

                listenFormat = false;
                continue;
            }

            characters.add(_char);
        }
        if (!characters.isEmpty()) {
            RenderColor color = currentColor;
            this.components.add(new Pair<>(() -> color, characters.toArray(new Character[0])));
            characters.clear();
        }
    }
}
