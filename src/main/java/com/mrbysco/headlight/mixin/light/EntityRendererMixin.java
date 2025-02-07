package com.mrbysco.headlight.mixin.light;

import com.mrbysco.headlight.light.LambDynamicLight;
import com.mrbysco.headlight.light.LightManager;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = EntityRenderer.class, priority = 999)
public class EntityRendererMixin<T extends Entity> {

	@Inject(method = "getBlockLightLevel", at = @At("RETURN"), cancellable = true)
	private void onGetBlockLight(T entity, BlockPos pos, CallbackInfoReturnable<Integer> cir) {
		if (!LightManager.shouldUpdateDynamicLight())
			return; // Do not touch to the value.

		int vanilla = cir.getReturnValueI();
		int entityLuminance = ((LambDynamicLight) entity).headlight$getLuminance();
		if (entityLuminance >= 15)
			cir.setReturnValue(entityLuminance);

		int posLuminance = (int) LightManager.getDynamicLightLevel(pos);

		int newValue = Math.max(Math.max(vanilla, entityLuminance), posLuminance);
		if (newValue > vanilla)
			cir.setReturnValue(newValue);
	}
}
