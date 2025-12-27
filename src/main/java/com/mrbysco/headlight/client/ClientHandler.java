package com.mrbysco.headlight.client;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.client.model.HeadlightModel;
import com.mrbysco.headlight.client.renderer.HeadlightRenderer;
import com.mrbysco.headlight.client.screen.HeadlightScreen;
import com.mrbysco.headlight.registry.LightMenus;
import com.mrbysco.headlight.registry.LightRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

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

	public static void registerClientExtension(RegisterClientExtensionsEvent event) {
		event.registerItem(new IClientItemExtensions() {
			private final LazyLoadedValue<HeadlightModel<?>> model = new LazyLoadedValue<>(this::provideHeadlightModel);

			public HeadlightModel<?> provideHeadlightModel() {
				return new HeadlightModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ClientHandler.HEADLIGHT));
			}

			@Override
			public Model getHumanoidArmorModel(ItemStack itemStack, EquipmentClientInfo.LayerType layerType, Model original) {
				ItemStack source = HeadlightRenderer.INSTANCE.getSourceItem(itemStack);
				var helmetModel = model.get();
				helmetModel.setSourceStack(source);
				return helmetModel;
			}

			@Override
			public ResourceLocation getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer, ResourceLocation _default) {
				return HeadlightMod.modLoc("textures/models/armor/headlight.png");
			}
		}, LightRegistry.HEADLIGHT.get());
	}
}