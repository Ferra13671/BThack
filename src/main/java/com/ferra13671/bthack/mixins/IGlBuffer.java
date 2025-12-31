package com.ferra13671.bthack.mixins;

import com.mojang.blaze3d.opengl.GlBuffer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GlBuffer.class)
public interface IGlBuffer {

    @Accessor("handle")
    int bthack$$$getHandle();
}
