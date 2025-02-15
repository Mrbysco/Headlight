package com.mrbysco.headlight.client;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.client.model.HeadlightModel;
import com.mrbysco.headlight.client.screen.HeadlightScreen;
import com.mrbysco.headlight.registry.LightMenus;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

public class ClientHandler {
	public static final ModelLayerLocation HEADLIGHT = new ModelLayerLocation(HeadlightMod.modLoc("headlight"), "main");
	public static final ModelLayerLocation HEADLIGHT_ADDON = new ModelLayerLocation(HeadlightMod.modLoc("headlight"), "addon");

	public static void registerMenuScreens(final RegisterMenuScreensEvent event) {
		event.register(LightMenus.HEADLIGHT.get(), HeadlightScreen::new);
	}

	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(HEADLIGHT, HeadlightModel::createHeadlightLayer);
		event.registerLayerDefinition(HEADLIGHT_ADDON, HeadlightModel::createHeadlightLayer);
	}
}