package com.mrbysco.headlight.client.screen;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.menu.HeadlightMenu;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class HeadlightScreen extends AbstractContainerScreen<HeadlightMenu> {
	private final Identifier TEXTURE = HeadlightMod.modLoc("textures/gui/container/headlight.png");

	public HeadlightScreen(HeadlightMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn, 176, 134);
	}

	@Override
	public void extractRenderState(@NotNull GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.extractBackground(guiGraphics, mouseX, mouseY, partialTicks);
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
		this.extractTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTicks) {
		super.extractBackground(graphics, mouseX, mouseY, partialTicks);
		graphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight, 256, 256);
	}

	@Override
	protected void extractLabels(@NotNull GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
		this.inventoryLabelY = 42;
		super.extractLabels(graphics, mouseX, mouseY);
	}
}
