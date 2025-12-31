package com.ferra13671.bthack.mixins;

import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MouseHandler.class)
public interface IMouseHandler {

    @Accessor("xpos")
    double bthack$$$getXPos();

    @Accessor("ypos")
    double bthack$$$getYPos();
}
