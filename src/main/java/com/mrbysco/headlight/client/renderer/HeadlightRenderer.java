package com.mrbysco.headlight.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mrbysco.headlight.Reference;
import com.mrbysco.headlight.client.ClientHandler;
import com.mrbysco.headlight.client.model.HeadlightModel;
import com.mrbysco.headlight.registry.LightRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ConcurrentHashMap;

public class HeadlightRenderer {
	private static final ConcurrentHashMap<String, ItemStack> stacks = new ConcurrentHashMap<>();
	public static final HeadlightRenderer INSTANCE = new HeadlightRenderer();
	private static final ResourceLocation TEXTURE = new ResourceLocation("headlight:textures/models/armor/headlight.png");

	private final ItemRenderer itemRenderer;
	private final HeadlightModel<? extends LivingEntity> addonModel;

	public HeadlightRenderer() {
		this.itemRenderer = Minecraft.getInstance().getItemRenderer();
		EntityModelSet entityModelSet = Minecraft.getInstance().getEntityModels();
		this.addonModel = new HeadlightModel<>(entityModelSet.bakeLayer(ClientHandler.HEADLIGHT_ADDON), true);
	}

	@Nullable
	public ItemStack getSourceItem(@NotNull ItemStack helmetStack) {
		if (helmetStack.getTag() != null && helmetStack.getTag().contains(Reference.SOURCE_TAG)) {
			String sourceId = helmetStack.getTag().getString(Reference.SOURCE_TAG);
			if (!sourceId.isEmpty()) {
				if (stacks.containsKey(sourceId)) {
					return stacks.get(sourceId);
				} else {
					ResourceLocation sourceLocation = ResourceLocation.tryParse(sourceId);
					if (sourceLocation != null) {
						Item sourceItem = ForgeRegistries.ITEMS.getValue(sourceLocation);
						if (sourceItem != null) {
							ItemStack sourceStack = new ItemStack(sourceItem);
							stacks.put(sourceId, sourceStack);
							return sourceStack;
						}
					}
				}
			}
		}
		return null;
	}

	public void renderHeadlight(ItemStack helmetStack, PoseStack poseStack, HumanoidModel<? extends LivingEntity> humanoidModel, MultiBufferSource buffer, int packedLight) {
		ItemStack sourceStack = getSourceItem(helmetStack);
		if (sourceStack != null) {
			addonModel.setSourceStack(sourceStack);

			copyProperties(humanoidModel, this.addonModel);

			poseStack.pushPose();
			poseStack.translate(0, 2, 0);
			boolean isSourceHelmet = helmetStack.is(LightRegistry.HEADLIGHT.get());
			if (!isSourceHelmet) {
				VertexConsumer vertexconsumer = buffer.getBuffer(this.addonModel.renderType(TEXTURE));
				this.addonModel.renderToBuffer(poseStack, vertexconsumer, 15728880, OverlayTexture.NO_OVERLAY,
						1.0F, 1.0F, 1.0F, 1.0F);
			}
			poseStack.popPose();
		} else {
			addonModel.setSourceStack(null);
		}
	}

	private void copyProperties(HumanoidModel<?> playerModel, HeadlightModel<?> headlightModel) {
		headlightModel.attackTime = playerModel.attackTime;
		headlightModel.riding = playerModel.riding;
		headlightModel.young = playerModel.young;
		headlightModel.leftArmPose = playerModel.leftArmPose;
		headlightModel.rightArmPose = playerModel.rightArmPose;
		headlightModel.crouching = playerModel.crouching;
		headlightModel.head.copyFrom(playerModel.head);
		headlightModel.hat.copyFrom(playerModel.hat);
		headlightModel.body.copyFrom(playerModel.body);
		headlightModel.rightArm.copyFrom(playerModel.rightArm);
		headlightModel.leftArm.copyFrom(playerModel.leftArm);
		headlightModel.rightLeg.copyFrom(playerModel.rightLeg);
		headlightModel.leftLeg.copyFrom(playerModel.leftLeg);
	}
}
