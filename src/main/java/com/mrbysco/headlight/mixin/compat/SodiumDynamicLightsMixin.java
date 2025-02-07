package com.mrbysco.headlight.mixin.compat;

import com.mrbysco.headlight.light.LightManager;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import toni.sodiumdynamiclights.SodiumDynamicLights;

@Mixin(value = SodiumDynamicLights.class, remap = false)
public class SodiumDynamicLightsMixin {

	@Inject(method = "getLightmapWithDynamicLight(Lnet/minecraft/core/BlockPos;I)I", at = @At("TAIL"), cancellable = true)
	public void headlight$getLightmapWithDynamicLight(BlockPos pos, int lightmap, CallbackInfoReturnable<Integer> cir) {
		int posLightLevel = LightManager.getLightmapWithDynamicLight(pos, cir.getReturnValueI());
		if (cir.getReturnValue() < posLightLevel) {
			cir.setReturnValue(posLightLevel);
		}
	}
}
