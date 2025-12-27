package com.mrbysco.headlight.client;

import com.mrbysco.headlight.client.renderer.HeadlightRenderer;
import com.mrbysco.headlight.light.LightManager;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.RenderLivingEvent;

@EventBusSubscriber
public class ClientEventHandler {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void renderWorldLastEvent(final RenderLevelStageEvent.AfterTripwireBlocks event) {
		LightManager.updateAll(event.getLevelRenderer());
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void renderLivingEvent(RenderLivingEvent.Pre<? extends LivingEntity, ? extends LivingEntityRenderState, ? extends EntityModel<?>> event) {
		LivingEntityRenderState livingEntity = event.getRenderState();
		EntityModel<?> entityModel = event.getRenderer().getModel();
		if (livingEntity instanceof HumanoidRenderState humanoidState && entityModel instanceof HumanoidModel<?> humanoidModel) {
			ItemStack headStack = humanoidState.headEquipment;
			if (!headStack.isEmpty()) {
				HeadlightRenderer.INSTANCE.renderHeadlight(headStack, event.getPoseStack(), humanoidModel, event.getSubmitNodeCollector());
			}
		}
	}
}
