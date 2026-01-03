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
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.equipment.ArmorMaterials;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.item.ItemAccessItemHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class HeadlightHelmetItem extends Item {

	public HeadlightHelmetItem(Properties properties) {
		super(properties.humanoidArmor(ArmorMaterials.IRON, ArmorType.HELMET));
	}

	@NotNull
	@Override
	public InteractionResult use(@NonNull Level level, Player player, @NonNull InteractionHand hand) {
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
		return new SimpleMenuProvider((id, inventory, player) -> new HeadlightMenu(id, inventory, stack), stack.has(DataComponents.CUSTOM_NAME) ? ((MutableComponent) stack.getHoverName()).withStyle(ChatFormatting.BLACK) : Component.translatable(HeadlightMod.MOD_ID + ".container.headlight"));
	}

	public static class LightInventory extends ItemAccessItemHandler {
		private ItemStack parentStack;

		public LightInventory(ItemAccess itemAccess, ItemStack parentStack) {
			super(itemAccess, LightRegistry.HEADLIGHT_CONTENTS.get(), 1);
			this.parentStack = parentStack;
		}

		public IndexModifier<ItemResource> indexModifier(ItemStack stack) {
			return (index, resource, amount) -> {
				ItemContainerContents result = update(ItemResource.of(stack), index, resource, amount).get(this.component);
				updateStack(stack, resource);

				stack.set(this.component, result);
			};
		}

		@Override
		public boolean isValid(int index, ItemResource resource) {
			if (!resource.isEmpty()) {
				Item item = resource.getItem();
				int lightValue = LightManager.getValue(item);
				return lightValue > 0;
			}
			return super.isValid(index, resource);
		}

		private void updateStack(ItemStack stack, ItemResource newResource) {
			if (newResource.isEmpty()) {
				stack.remove(LightRegistry.LIGHT_SOURCE);
				stack.remove(LightRegistry.LIGHT_LEVEL);
			} else {
				int lightLevel = LightManager.getValue(newResource.getItem());
				if (lightLevel > 0) stack.set(LightRegistry.LIGHT_LEVEL, lightLevel);
				else stack.remove(LightRegistry.LIGHT_LEVEL);

				Identifier itemID = BuiltInRegistries.ITEM.getKey(newResource.getItem());
				if (itemID != null) stack.set(LightRegistry.LIGHT_SOURCE, itemID);
				else stack.remove(LightRegistry.LIGHT_SOURCE);
			}
		}
	}
}
