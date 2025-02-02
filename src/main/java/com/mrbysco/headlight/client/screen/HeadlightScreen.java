package com.mrbysco.headlight.client.screen;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.menu.HeadlightMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class HeadlightScreen extends AbstractContainerScreen<HeadlightMenu> {
	private final ResourceLocation TEXTURE = new ResourceLocation(HeadlightMod.MOD_ID, "textures/gui/container/headlight.png");

	public HeadlightScreen(HeadlightMenu screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);

		this.imageHeight = 134;
	}

	@Override
	public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(guiGraphics);
		super.render(guiGraphics, mouseX, mouseY, partialTicks);
		this.renderTooltip(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int x, int y) {
		guiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
	}

	@Override
	protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
		this.inventoryLabelY = 42;
		super.renderLabels(guiGraphics, mouseX, mouseY);
	}
}
