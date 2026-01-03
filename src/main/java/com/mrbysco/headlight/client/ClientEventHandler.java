package com.mrbysco.headlight.client;

import com.mrbysco.headlight.light.LightManager;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

@EventBusSubscriber
public class ClientEventHandler {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void renderWorldLastEvent(final RenderLevelStageEvent.AfterTripwireBlocks event) {
		LightManager.updateAll(event.getLevelRenderer());
	}
}
