package com.ferra13671.BThack.mixins.xray;

import com.ferra13671.BThack.api.Interfaces.Mc;
import com.ferra13671.BThack.impl.Modules.RENDER.Xray;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = {
	// current target
	"me.jellysquid.mods.sodium.client.render.chunk.compile.pipeline.BlockOcclusionCache",
	// < Sodium 0.5.0
	"me.jellysquid.mods.sodium.client.render.occlusion.BlockOcclusionCache"},
	remap = false)
public class MixinSodiumBlockOcclusionCache implements Mc {


	@Inject(at = @At("HEAD"), method = "shouldDrawSide", cancellable = true)
	public void shouldDrawSide(BlockState state, BlockView world, BlockPos pos, Direction side, CallbackInfoReturnable<Boolean> cir) {
		if (Xray.doXray) {
			BlockState state2 = mc.world.getBlockState(pos);
			if (!Xray.xrayBlocks.contains(state.getBlock()))
				cir.setReturnValue(false);
			else
				cir.setReturnValue(true);

			if (!Xray.xrayBlocks.contains(state2.getBlock()))
				cir.setReturnValue(false);
			else
				cir.setReturnValue(true);
		}
	}
}