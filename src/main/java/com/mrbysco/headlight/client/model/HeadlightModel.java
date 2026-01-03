package com.mrbysco.headlight.client.model;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class HeadlightModel<S extends HumanoidRenderState> extends HumanoidModel<S> {
	private final ModelPart helmet;
	private final ModelPart addon;

	public HeadlightModel(ModelPart root) {
		super(root);
		this.helmet = this.getHead().getChild("helmet");
		this.addon = this.getHead().getChild("addon");
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
		MeshDefinition meshdefinition = createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition head = partdefinition.getChild("head");

		PartDefinition helmet = head.addOrReplaceChild("helmet", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0, 0.0F));

		PartDefinition addon = head.addOrReplaceChild("addon", CubeListBuilder.create().texOffs(0, 17).addBox(-2.0F, -10.0F, -5.0F, 4.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0, 0.0F));

		PartDefinition cube_r1 = addon.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(11, 20).addBox(0.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.85F, -7.0F, -4.15F, 0.0F, -0.1309F, 0.0F));

		PartDefinition cube_r2 = addon.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(11, 16).addBox(-1.0F, -2.0F, -1.0F, 1.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.9F, -7.0F, -4.15F, 0.0F, 0.1309F, 0.0F));

		PartDefinition cube_r3 = addon.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(1, 4).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -10.0F, -5.0F, -1.6668F, 0.0F, 0.0F));

		PartDefinition cube_r4 = addon.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(1, 1).addBox(-1.0F, -1.0F, 0.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -5.1F, -1.4923F, 0.0F, 0.0F));

		PartDefinition cube_r5 = addon.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(15, 20).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -7.0F, -4.0F, -0.829F, 0.0F, 0.0F));

		PartDefinition cube_r6 = addon.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(15, 16).addBox(0.01F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
				.texOffs(15, 16).addBox(2.99F, -1.0F, -1.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-2.0F, -8.0F, -4.0F, -0.7418F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 32);
	}
}