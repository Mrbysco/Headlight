package com.mrbysco.headlight.client;

import com.google.common.reflect.TypeToken;
import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.client.layer.HeadLightSourceLayer;
import com.mrbysco.headlight.client.model.HeadlightModel;
import com.mrbysco.headlight.client.renderer.HeadlightRenderer;
import com.mrbysco.headlight.client.screen.HeadlightScreen;
import com.mrbysco.headlight.registry.LightMenus;
import com.mrbysco.headlight.registry.LightRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.AvatarRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;

@EventBusSubscriber
public class ClientHandler {
	public static final ModelLayerLocation HEADLIGHT = new ModelLayerLocation(HeadlightMod.modLoc("headlight"), "main");
	public static final ModelLayerLocation HEADLIGHT_ADDON = new ModelLayerLocation(HeadlightMod.modLoc("headlight"), "addon");

	@SubscribeEvent
	public static void registerMenuScreens(final RegisterMenuScreensEvent event) {
		event.register(LightMenus.HEADLIGHT.get(), HeadlightScreen::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(HEADLIGHT, HeadlightModel::createHeadlightLayer);
		event.registerLayerDefinition(HEADLIGHT_ADDON, HeadlightModel::createHeadlightLayer);
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	@SubscribeEvent
	public static void addLayers(EntityRenderersEvent.AddLayers event) {
		for (EntityType<?> entityType : event.getEntityTypes()) {
			EntityRenderer<?, ?> renderer = event.getRenderer(entityType);
			if (renderer instanceof HumanoidMobRenderer humanoidMobRenderer) {
				humanoidMobRenderer.addLayer(new HeadLightSourceLayer(humanoidMobRenderer));
			}
		}
		for (var skin : event.getSkins()) {
			var renderer = event.getPlayerRenderer(skin);
			if (renderer instanceof AvatarRenderer avatarRenderer) {
				avatarRenderer.addLayer(new HeadLightSourceLayer(renderer));
			}
		}
	}

	public static final ContextKey<ItemStackRenderState> SOURCE_RENDER_STATE = new ContextKey<>(HeadlightMod.modLoc("source_render_state"));

	@SubscribeEvent
	public static void registerRenderStateModifiers(final RegisterRenderStateModifiersEvent event) {
		event.registerEntityModifier(new TypeToken<LivingEntityRenderer<LivingEntity, LivingEntityRenderState, ?>>() {
		}, (livingEntity, renderState) -> {
			if (renderState instanceof HumanoidRenderState humanoidRenderState) {
				ItemStack headStack = humanoidRenderState.headEquipment;
				if (!headStack.isEmpty()) {
					ItemStack sourceStack = HeadlightRenderer.INSTANCE.getSourceItem(headStack);
					if (sourceStack != null) {
						ItemStackRenderState itemStackRenderState = new ItemStackRenderState();
						Minecraft.getInstance().getItemModelResolver()
								.updateForLiving(itemStackRenderState, sourceStack, ItemDisplayContext.FIXED, livingEntity);
						renderState.setRenderData(SOURCE_RENDER_STATE, itemStackRenderState);
					}
				}
			}
		});
	}

	@SubscribeEvent
	public static void registerClientExtension(RegisterClientExtensionsEvent event) {
		event.registerItem(new IClientItemExtensions() {
			private final Lazy<HeadlightModel<?>> model = Lazy.of(this::provideHeadlightModel);

			public HeadlightModel<?> provideHeadlightModel() {
				return new HeadlightModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ClientHandler.HEADLIGHT));
			}

			@NotNull
			@Override
			public Model<?> getHumanoidArmorModel(ItemStack itemStack, EquipmentClientInfo.LayerType layerType, Model original) {
				return model.get();
			}

			@Override
			public Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer, Identifier _default) {
				return HeadlightMod.modLoc("textures/models/armor/headlight.png");
			}
		}, LightRegistry.HEADLIGHT.get());
	}
}