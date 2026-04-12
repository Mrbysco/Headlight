package com.mrbysco.headlight.handler;

import com.mrbysco.headlight.registry.LightRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber
public class TooltipHandler {
	@SubscribeEvent
	public static void onTooltip(ItemTooltipEvent event) {
		ItemStack stack = event.getItemStack();
		if (stack.has(LightRegistry.HEADLIGHT_CONTENTS)) {
			event.getToolTip().add(1, Component.translatable("tooltip.headlight").withStyle(ChatFormatting.GOLD));
		}
	}
}
