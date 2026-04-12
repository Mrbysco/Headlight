package com.mrbysco.headlight.handler;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.menu.HeadlightMenu;
import com.mrbysco.headlight.registry.LightRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber
public class HelmetHandler {
	@SubscribeEvent
	public static void onHelmetRightClick(PlayerInteractEvent.RightClickItem event) {
		final ItemStack stack = event.getItemStack();
		if (!stack.isEmpty() && !stack.is(LightRegistry.HEADLIGHT) && stack.has(LightRegistry.HEADLIGHT_CONTENTS)) {
			final Player player = event.getEntity();
			if (!player.isShiftKeyDown()) {
				player.openMenu(getContainer(stack));
				event.setCancellationResult(InteractionResult.SUCCESS);
				event.setCanceled(true);
			}
		}
	}

	private static MenuProvider getContainer(ItemStack stack) {
		return new SimpleMenuProvider((id, inventory, player) ->
				new HeadlightMenu(id, inventory, stack), stack.has(DataComponents.CUSTOM_NAME) ?
				((MutableComponent) stack.getHoverName()).withStyle(ChatFormatting.BLACK) :
				Component.translatable(HeadlightMod.MOD_ID + ".container.headlight")
		);
	}
}
