package com.ferra13671.BThack.api.Utils;

import net.minecraft.util.math.Vec3i;

public final class Vec3iNew extends Vec3i {

    public Vec3iNew(double x, double y, double z) {
        super((int) Math.round(x), (int) Math.round(y), (int) Math.round(z));
    }
}
