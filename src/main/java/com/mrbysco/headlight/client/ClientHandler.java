package com.mrbysco.headlight.client;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.client.model.HeadlightModel;
import com.mrbysco.headlight.client.screen.HeadlightScreen;
import com.mrbysco.headlight.registry.LightMenus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientHandler {
	public static final ModelLayerLocation HEADLIGHT = new ModelLayerLocation(HeadlightMod.modLoc("headlight"), "main");

	public static void onClientSetup(final FMLClientSetupEvent event) {
		MenuScreens.register(LightMenus.HEADLIGHT.get(), HeadlightScreen::new);
	}

	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(HEADLIGHT, HeadlightModel::createArmorDefinition);
	}
}