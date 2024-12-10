package com.ferra13671.BThack.mixins.xray;

import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.block.AbstractBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractBlock.AbstractBlockState.class)
@Deprecated
public class MixinAbstractBlockState implements Mc {

	/*
	@Inject(at = @At("TAIL"), method = "isFullCube(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z", cancellable = true)
	private void onIsFullCube(BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		IsNormalCubeEvent event = new IsNormalCubeEvent(pos);
		BThack.EVENT_BUS.activate(event);

		if (event.isCancelled())
			cir.setReturnValue(false);
		else
			cir.setReturnValue(cir.getReturnValue());
	}

	 */
}