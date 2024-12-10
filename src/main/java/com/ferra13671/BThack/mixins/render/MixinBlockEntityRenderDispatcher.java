package com.ferra13671.BThack.mixins.render;

import com.ferra13671.BThack.Core.Client.ModuleList;
import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.api.Utils.MathUtils;
import com.ferra13671.BThack.impl.Modules.RENDER.NoRender;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.block.entity.EnchantingTableBlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityRenderDispatcher.class)
public class MixinBlockEntityRenderDispatcher implements Mc {

    @Inject(method = "render(Lnet/minecraft/block/entity/BlockEntity;FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;)V", at = @At("HEAD"), cancellable = true)
    public <E extends BlockEntity> void modifyBlockEntityRender(E blockEntity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        if (ModuleList.noRender.isEnabled()) {
            double distance = MathUtils.getDistance(mc.player.getPos(), blockEntity.getPos().toCenterPos());
            if (blockEntity instanceof ChestBlockEntity && NoRender.chestRender.getValue())
                if (distance > NoRender.chestRadius.getValue())
                    ci.cancel();
            if (blockEntity instanceof ShulkerBoxBlockEntity && NoRender.shulkerRender.getValue())
                if (distance > NoRender.shulkerRadius.getValue())
                    ci.cancel();
            if (blockEntity instanceof EnchantingTableBlockEntity && NoRender.eTableRender.getValue())
                if (distance > NoRender.eTableRadius.getValue())
                    ci.cancel();
        }
    }
}
