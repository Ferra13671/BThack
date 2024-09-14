package com.ferra13671.BThack.mixins;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Core.Client;
import com.ferra13671.BThack.Events.Events.Camera.PositionCameraEvent;
import com.ferra13671.BThack.Events.Events.Camera.RotateCameraEvent;
import com.ferra13671.BThack.impl.Modules.RENDER.ModifyCamera;
import com.ferra13671.BThack.impl.Modules.RENDER.NoRender;
import com.ferra13671.BThack.mixins.accessor.IProjection;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;
import java.util.List;

@Mixin(Camera.class)
public abstract class MixinCamera {

    @Shadow private Vec3d pos;

    @Shadow private BlockView area;

    @Shadow @Final private Vector3f horizontalPlane;

    @Shadow private Entity focusedEntity;

    @Shadow protected abstract void setPos(double x, double y, double z);

    @Shadow private float lastTickDelta;

    @Shadow protected abstract void setRotation(float yaw, float pitch);

    @Shadow private boolean ready;

    @Shadow public abstract Camera.Projection getProjection();

    @Shadow @Final private BlockPos.Mutable blockPos;

    @Inject(method = "clipToSpace", at = @At(value = "HEAD"), cancellable = true)
    public void modifyClipToSpace(double defaultDistance, CallbackInfoReturnable<Double> cir) {
        double distance = ModifyCamera.distance.getValue();
        boolean rewriteDistance = Client.getModuleByName("ModifyCamera").isEnabled() && ModifyCamera.rewriteDistance.getValue();
        if (Client.getModuleByName("CameraClip").isEnabled()) {
            cir.setReturnValue(rewriteDistance ? distance : defaultDistance);
            cir.cancel();
            return;
        }
        if (rewriteDistance) {
            for(int i = 0; i < 8; ++i) {
                float f = (float)((i & 1) * 2 - 1);
                float g = (float)((i >> 1 & 1) * 2 - 1);
                float h = (float)((i >> 2 & 1) * 2 - 1);
                f *= 0.1F;
                g *= 0.1F;
                h *= 0.1F;
                Vec3d vec3d = pos.add(f, g, h);
                Vec3d vec3d2 = new Vec3d(this.pos.x - (double)horizontalPlane.x() * distance + (double)f, this.pos.y - (double)this.horizontalPlane.y() * distance + (double)g, this.pos.z - (double)this.horizontalPlane.z() * distance + (double)h);
                HitResult hitResult = area.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.VISUAL, RaycastContext.FluidHandling.NONE, focusedEntity));
                if (hitResult.getType() != HitResult.Type.MISS) {
                    double d = hitResult.getPos().distanceTo(this.pos);
                    if (d < distance) {
                        distance = d;
                    }
                }
            }

            cir.setReturnValue(distance);
        }
    }

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setPos(DDD)V"))
    private void hookUpdatePosition(Camera instance, double x, double y, double z) {
        PositionCameraEvent event = new PositionCameraEvent(x, y, z, lastTickDelta);
        BThack.EVENT_BUS.activate(event);

        setPos(event.getX(), event.getY(), event.getZ());
    }

    @Redirect(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/Camera;setRotation(FF)V"))
    private void hookUpdateRotation(Camera instance, float yaw, float pitch) {
        RotateCameraEvent event = new RotateCameraEvent(yaw, pitch);
        BThack.EVENT_BUS.activate(event);

        setRotation(event.getYaw(), event.getPitch());
    }

    @Inject(method = "getSubmersionType", at = @At("HEAD"), cancellable = true)
    public void modifyGetSubmersionType(CallbackInfoReturnable<CameraSubmersionType> cir) {
        CameraSubmersionType returnValue = CameraSubmersionType.NONE;
        if (!this.ready) {
            returnValue = CameraSubmersionType.NONE;
        }
        FluidState fluidState = this.area.getFluidState(this.blockPos);
        if (fluidState.isIn(FluidTags.WATER) && this.pos.y < (double)((float)this.blockPos.getY() + fluidState.getHeight(this.area, this.blockPos))) {
            returnValue = CameraSubmersionType.WATER;
        }
        Camera.Projection projection = this.getProjection();
        List<Vec3d> list = Arrays.asList(((IProjection) projection).getCenter(), projection.getBottomRight(), projection.getTopRight(), projection.getBottomLeft(), projection.getTopLeft());
        for (Vec3d vec3d : list) {
            Vec3d vec3d2 = this.pos.add(vec3d);
            BlockPos blockPos = BlockPos.ofFloored(vec3d2);
            FluidState fluidState2 = this.area.getFluidState(blockPos);
            if (fluidState2.isIn(FluidTags.LAVA)) {
                if (!(vec3d2.y <= (double)(fluidState2.getHeight(this.area, blockPos) + (float)blockPos.getY()))) continue;
                returnValue = CameraSubmersionType.LAVA;
            }
            BlockState blockState = this.area.getBlockState(blockPos);
            if (!blockState.isOf(Blocks.POWDER_SNOW)) continue;
            returnValue = CameraSubmersionType.POWDER_SNOW;
        }

        switch (returnValue) {
            case LAVA:
                if (Client.isOptionActivated("NoRender", NoRender.lavaFog))
                    returnValue = CameraSubmersionType.NONE;
                break;
            case WATER:
                if (Client.isOptionActivated("NoRender", NoRender.waterFog))
                    returnValue = CameraSubmersionType.NONE;
                break;
            case POWDER_SNOW:
                if (Client.isOptionActivated("NoRender", NoRender.powderSnowFog))
                    returnValue = CameraSubmersionType.NONE;
                break;
        }
        cir.setReturnValue(returnValue);
    }
}
