package com.mrbysco.headlight.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mrbysco.headlight.client.ClientHandler;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class HeadLightSourceLayer<S extends HumanoidRenderState, M extends HumanoidModel<S>> extends RenderLayer<S, M> {
	public HeadLightSourceLayer(RenderLayerParent<S, M> renderer) {
		super(renderer);
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, S renderState, float yRot, float xRot) {
		ItemStackRenderState headStack = renderState.getRenderData(ClientHandler.SOURCE_RENDER_STATE);
		if (headStack != null) {
			poseStack.pushPose();
			M m = this.getParentModel();
			m.root().translateAndRotate(poseStack);
			m.translateToHead(poseStack);

			poseStack.scale(0.3125F, -0.3125F, -0.3125F);
			poseStack.translate(0F, 1.6125F, 1F);

			if (headStack.usesBlockLight()) {
				poseStack.translate(0, -0.0625F, 0);
			} else {
				poseStack.scale(0.5625F, 0.5625F, 0.5625F);
			}

			headStack.submit(poseStack, nodeCollector, packedLight, OverlayTexture.NO_OVERLAY, renderState.outlineColor);

			poseStack.popPose();
		}
	}
}
