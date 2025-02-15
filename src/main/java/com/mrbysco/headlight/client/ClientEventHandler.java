package com.mrbysco.headlight.client;

import com.mrbysco.headlight.client.renderer.HeadlightRenderer;
import com.mrbysco.headlight.light.LightManager;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;

public class ClientEventHandler {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void renderWorldLastEvent(final RenderLevelStageEvent event) {
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
			LightManager.updateAll(event.getLevelRenderer());
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void renderLivingEvent(RenderLivingEvent.Pre<? extends LivingEntity, ? extends EntityModel<? extends LivingEntity>> event) {
		LivingEntity livingEntity = event.getEntity();
		EntityModel<? extends LivingEntity> entityModel = event.getRenderer().getModel();
		if (entityModel instanceof HumanoidModel<? extends LivingEntity> humanoidModel) {
			ItemStack headStack = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
			if (!headStack.isEmpty()) {
				HeadlightRenderer.INSTANCE.renderHeadlight(headStack, event.getPoseStack(), humanoidModel, event.getMultiBufferSource(), event.getPackedLight());
			}
		}
	}
}
