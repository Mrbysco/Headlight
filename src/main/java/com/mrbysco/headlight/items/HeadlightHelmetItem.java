package com.mrbysco.headlight.items;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.light.LightManager;
import com.mrbysco.headlight.menu.HeadlightMenu;
import com.mrbysco.headlight.registry.LightRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jetbrains.annotations.Nullable;

public class HeadlightHelmetItem extends Item {

	public HeadlightHelmetItem(Properties properties) {
		super(properties.humanoidArmor(ArmorMaterials.IRON, ArmorType.HELMET));
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!player.isShiftKeyDown()) {
			player.openMenu(this.getContainer(stack));
			return InteractionResult.SUCCESS;
		} else {
			return super.use(level, player, hand);
		}
	}

	@Nullable
	public MenuProvider getContainer(ItemStack stack) {
		return new SimpleMenuProvider((id, inventory, player) ->
				new HeadlightMenu(id, inventory, stack), stack.has(DataComponents.CUSTOM_NAME) ?
				((MutableComponent) stack.getHoverName()).withStyle(ChatFormatting.BLACK) :
				Component.translatable(HeadlightMod.MOD_ID + ".container.headlight"));
	}

	public static class LightInventory extends ItemStacksResourceHandler {
		private ItemStack parentStack;

		public LightInventory(ItemStack parentStack) {
			super(1);
			this.parentStack = parentStack;
		}

		@Override
		public void set(int index, ItemResource resource, int amount) {
			super.set(index, resource, amount);
			ItemResource newResource = this.getResource(index);
			if (newResource.isEmpty()) {
				parentStack.remove(LightRegistry.LIGHT_SOURCE);
				parentStack.remove(LightRegistry.LIGHT_LEVEL);
			} else {
				int lightLevel = LightManager.getValue(newResource.getItem());
				if (lightLevel > 0)
					parentStack.set(LightRegistry.LIGHT_LEVEL, lightLevel);
				else
					parentStack.remove(LightRegistry.LIGHT_LEVEL);

				ResourceLocation itemID = BuiltInRegistries.ITEM.getKey(newResource.getItem());
				if (itemID != null)
					parentStack.set(LightRegistry.LIGHT_SOURCE, itemID);
				else
					parentStack.remove(LightRegistry.LIGHT_SOURCE);
			}
		}
	}
}
