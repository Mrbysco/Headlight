package com.mrbysco.headlight.client.renderer;

import com.mrbysco.headlight.registry.LightRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class HeadlightRenderer {
	private static final ConcurrentHashMap<Identifier, ItemStack> stacks = new ConcurrentHashMap<>();
	public static final HeadlightRenderer INSTANCE = new HeadlightRenderer();

	public HeadlightRenderer() {
	}

	@Nullable
	public ItemStack getSourceItem(@NotNull ItemStack helmetStack) {
		if (helmetStack.has(LightRegistry.LIGHT_SOURCE)) {
			Identifier sourceId = helmetStack.get(LightRegistry.LIGHT_SOURCE);
			if (sourceId == null) {
				return null;
			}
			if (stacks.containsKey(sourceId)) {
				return stacks.get(sourceId);
			} else {
				Optional<Holder.Reference<Item>> sourceItem = BuiltInRegistries.ITEM.get(sourceId);
				if (sourceItem.isPresent()) {
					ItemStack sourceStack = new ItemStack(sourceItem.get().value());
					stacks.put(sourceId, sourceStack);
					return sourceStack;
				}
			}
		}
		return null;
	}
}
