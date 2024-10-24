package com.ferra13671.BThack.mixins.accessor;

import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerMoveC2SPacket.class)
public interface IPlayerMoveC2SPacket {

    @Accessor("onGround")
    void setOnGround(boolean onGround);

    @Accessor("onGround")
    boolean isOnGround();

    @Accessor("changePosition")
    void setChangePosition(boolean changePosition);

    @Accessor("changePosition")
    boolean isChangePosition();

    @Accessor("changeLook")
    void setChangeLook(boolean changeLook);

    @Accessor("changeLook")
    boolean isChangeLook();

    @Accessor("x")
    void setX(double x);

    @Accessor("x")
    double getX();

    @Accessor("y")
    void setY(double y);

    @Accessor("y")
    double getY();

    @Accessor("z")
    void setZ(double z);

    @Accessor("z")
    double getZ();

    @Accessor("yaw")
    void setYaw(float yaw);

    @Accessor("yaw")
    float getYaw();

    @Accessor("pitch")
    void setPitch(float pitch);

    @Accessor("pitch")
    float getPitch();
}
