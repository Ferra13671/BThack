package com.ferra13671.BThack.mixins.xray;

import com.ferra13671.BThack.BThack;
import com.ferra13671.BThack.Events.IsNormalCubeEvent;
import com.ferra13671.BThack.api.Interfaces.Mc;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public class MixinAbstractBlockState implements Mc {

	@Inject(at = @At("TAIL"), method = "isFullCube(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Z", cancellable = true)
	private void onIsFullCube(BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
		IsNormalCubeEvent event = new IsNormalCubeEvent(pos);
		BThack.EVENT_BUS.activate(event);

		if (event.isCancelled())
			cir.setReturnValue(false);
		else
			cir.setReturnValue(cir.getReturnValue());
	}

	/*
	@Inject(at = @At("HEAD"), method = "getAmbientOcclusionLightLevel(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)F", cancellable = true)
	private void onGetAmbientOcclusionLightLevel(BlockView blockView, BlockPos blockPos, CallbackInfoReturnable<Float> cir) {

		if (Xray.doXray) {
			cir.setReturnValue(1.0f);
			cir.cancel();
		}
	}

	/*
	@Inject(at = @At("HEAD"), method = "getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", cancellable = true)
	private void onGetOutlineShape(BlockView view, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
		if(context == ShapeContext.absent())
			return;
		
		HackList hax = WurstClient.INSTANCE.getHax();
		if(hax == null)
			return;
		
		HandNoClipHack handNoClipHack = hax.handNoClipHack;
		if(!handNoClipHack.isEnabled() || handNoClipHack.isBlockInList(pos))
			return;
		
		cir.setReturnValue(VoxelShapes.empty());
	}
	
	@Inject(at = @At("HEAD"), method = "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;", cancellable = true)
	private void onGetCollisionShape(BlockView world, BlockPos pos, ShapeContext context, CallbackInfoReturnable<VoxelShape> cir) {
		if(getFluidState().isEmpty())
			return;
		
		HackList hax = WurstClient.INSTANCE.getHax();
		if(hax == null || !hax.jesusHack.shouldBeSolid())
			return;
		
		cir.setReturnValue(VoxelShapes.fullCube());
		cir.cancel();
	}
	
	@Shadow
	public abstract FluidState getFluidState();

	 */
}