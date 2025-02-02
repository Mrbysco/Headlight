package com.mrbysco.headlight.client;

import com.mrbysco.headlight.light.LightManager;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ClientEventHandler {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void renderWorldLastEvent(final RenderLevelStageEvent event) {
		if (event.getStage() == RenderLevelStageEvent.Stage.AFTER_TRIPWIRE_BLOCKS) {
			LightManager.updateAll(event.getLevelRenderer());
		}
	}
}
