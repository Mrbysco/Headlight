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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(HeadlightMod.MOD_ID)
public class HeadlightMod {
	public static final String MOD_ID = "headlight";
	public static final Logger LOGGER = LogUtils.getLogger();

	public static final TagKey<Item> LIGHTS = ItemTags.create(modLoc("lights"));
	public static final TagKey<Item> HEADLIGHT_HELMETS = ItemTags.create(modLoc("headlight_helmets"));

	public HeadlightMod() {
		IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();

		LightRegistry.BLOCKS.register(eventBus);
		LightRegistry.ITEMS.register(eventBus);
		LightRegistry.CREATIVE_MODE_TABS.register(eventBus);
		LightMenus.MENU_TYPES.register(eventBus);

		MinecraftForge.EVENT_BUS.register(this);

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
			eventBus.addListener(ClientHandler::onClientSetup);
			eventBus.addListener(ClientHandler::registerLayerDefinitions);
			MinecraftForge.EVENT_BUS.register(ClientEventHandler.class);
		});
	}

	@SubscribeEvent
	public void onServerStarted(ServerStartedEvent event) {
		LightManager.init();
	}

	public static ResourceLocation modLoc(String path) {
		return new ResourceLocation(MOD_ID, path);
	}
}
