package com.mrbysco.headlight;

import com.mojang.logging.LogUtils;
import com.mrbysco.headlight.client.ClientEventHandler;
import com.mrbysco.headlight.client.ClientHandler;
import com.mrbysco.headlight.light.LightManager;
import com.mrbysco.headlight.registry.LightMenus;
import com.mrbysco.headlight.registry.LightRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import org.slf4j.Logger;

@Mod(HeadlightMod.MOD_ID)
public class HeadlightMod {
	public static final String MOD_ID = "headlight";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static final TagKey<Item> LIGHTS = ItemTags.create(modLoc("lights"));
	public static final TagKey<Item> HEADLIGHT_HELMETS = ItemTags.create(modLoc("headlight_helmets"));

	public HeadlightMod(IEventBus eventBus, Dist dist) {
		eventBus.addListener(LightRegistry::registerCapabilities);

		LightRegistry.DATA_COMPONENT_TYPES.register(eventBus);
		LightRegistry.BLOCKS.register(eventBus);
		LightRegistry.ITEMS.register(eventBus);
		LightRegistry.CREATIVE_MODE_TABS.register(eventBus);
		LightMenus.MENU_TYPES.register(eventBus);

		NeoForge.EVENT_BUS.register(this);

		if (dist.isClient()) {
			eventBus.addListener(ClientHandler::registerMenuScreens);
			eventBus.addListener(ClientHandler::registerLayerDefinitions);
			NeoForge.EVENT_BUS.register(ClientEventHandler.class);
		}
	}

	@SubscribeEvent
	public void onServerStarted(ServerStartedEvent event) {
		LightManager.init();
	}

	public static ResourceLocation modLoc(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
