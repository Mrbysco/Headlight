package com.mrbysco.headlight.mixin.light;

import com.mrbysco.headlight.light.LightManager;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

	@Inject(method = "getLightColor(Lnet/minecraft/client/renderer/LevelRenderer$BrightnessGetter;Lnet/minecraft/world/level/BlockAndTintGetter;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/BlockPos;)I",
			at = @At("TAIL"), cancellable = true)
	private static void onGetLightmapCoordinates(LevelRenderer.BrightnessGetter brightnessGetter, BlockAndTintGetter level, BlockState state, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		if (!LightManager.shouldUpdateDynamicLight()) return; // Do not touch to the value.
		if (!state.isSolidRender())
			cir.setReturnValue(LightManager.getLightmapWithDynamicLight(pos, cir.getReturnValue()));
	}
}
