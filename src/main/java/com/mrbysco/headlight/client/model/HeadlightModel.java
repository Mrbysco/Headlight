package com.mrbysco.headlight.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HeadlightModel<S extends HumanoidRenderState> extends HumanoidModel<S> {
	private final ModelPart helmet;
	private final ModelPart addon;
	private final boolean isAddon;
	private ItemStack sourceStack = ItemStack.EMPTY;

	public HeadlightModel(ModelPart root) {
		this(root, false);
	}

	public HeadlightModel(ModelPart root, boolean isAddon) {
		super(root);
		this.helmet = root.getChild("helmet");
		this.addon = root.getChild("addon");
		this.isAddon = isAddon;
	}

	private static MeshDefinition createArmorMesh() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("right_foot", CubeListBuilder.create(), PartPose.ZERO);
		partdefinition.addOrReplaceChild("left_foot", CubeListBuilder.create(), PartPose.ZERO);

		return meshdefinition;
	}

	public static LayerDefinition createHeadlightLayer() {
		MeshDefinition meshdefinition = createArmorMesh();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition helmet = partdefinition.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition addon = partdefinition.addOrReplaceChild("addon", CubeListBuilder.create().texOffs(0, 17).addBox(-2.0F, -10.0F, -5.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition cube_r1 = addon.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(11, 20).addBox(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.85F, -7.0F, -4.15F, 0.0F, -0.1309F, 0.0F));

		PartDefinition cube_r2 = addon.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(11, 16).addBox(-1.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.9F, -7.0F, -4.15F, 0.0F, 0.1309F, 0.0F));

		PartDefinition cube_r3 = addon.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(1, 4).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -5.0F, -1.6668F, 0.0F, 0.0F));

		PartDefinition cube_r4 = addon.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -5.1F, -1.4923F, 0.0F, 0.0F));

		PartDefinition cube_r5 = addon.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(15, 20).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -4.0F, -0.829F, 0.0F, 0.0F));

		PartDefinition cube_r6 = addon.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(15, 16).addBox(0.01F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(15, 16).addBox(2.99F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -8.0F, -4.0F, -0.7418F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	public ModelPart getAddon() {
		return addon;
	}

	public void setSourceStack(@Nullable ItemStack stack) {
		if (stack == null) {
			sourceStack = ItemStack.EMPTY;
		} else {
			if (sourceStack.isEmpty() || stack.getItem() != sourceStack.getItem())
				this.sourceStack = stack;
		}
	}

	@Override
	public void setupAnim(S renderState) {
		super.setupAnim(renderState);
		poseStack.pushPose();
		poseStack.scale(1.15F, 1.15F, 1.15F);
		if (!isAddon) {
			this.helmet.copyFrom(this.head);
			helmet.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		}
		this.addon.copyFrom(this.head);
		addon.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
		poseStack.popPose();
		if (!sourceStack.isEmpty()) {
			ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
			poseStack.pushPose();
			helmet.translateAndRotate(poseStack);
			poseStack.translate(0.0F, -0.25F, 0.0F);
			poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
			poseStack.scale(0.3125F, -0.3125F, -0.3125F);
			poseStack.translate(0, 1.0625F, -1.1875f);
			final BakedModel model = itemRenderer.getModel(sourceStack, null, null, 0);
			if (model.isGui3d()) {
				poseStack.translate(0, -0.0625F, 0);
			} else {
				poseStack.scale(0.5625F, 0.5625F, 0.5625F);
			}
			itemRenderer.render(sourceStack, ItemDisplayContext.FIXED, false, poseStack, Minecraft.getInstance().renderBuffers().bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, model);
			poseStack.popPose();
		}
	}
}