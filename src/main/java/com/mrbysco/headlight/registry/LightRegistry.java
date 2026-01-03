package com.mrbysco.headlight.registry;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.items.HeadlightHelmetItem;
import com.mrbysco.headlight.items.HeadlightHelmetItem.LightInventory;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;
import java.util.function.Supplier;

public class LightRegistry {
	public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES = DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, HeadlightMod.MOD_ID);
	public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(HeadlightMod.MOD_ID);
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(HeadlightMod.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HeadlightMod.MOD_ID);

	public static final Supplier<DataComponentType<ItemContainerContents>> HEADLIGHT_CONTENTS = DATA_COMPONENT_TYPES.register("headlight_contents", () ->
			DataComponentType.<ItemContainerContents>builder()
					.persistent(ItemContainerContents.CODEC)
					.networkSynchronized(ItemContainerContents.STREAM_CODEC)
					.build());

	public static final Supplier<DataComponentType<Identifier>> LIGHT_SOURCE = DATA_COMPONENT_TYPES.register("source", () ->
			DataComponentType.<Identifier>builder()
					.persistent(Identifier.CODEC)
					.networkSynchronized(Identifier.STREAM_CODEC)
					.build());

	public static final Supplier<DataComponentType<Integer>> LIGHT_LEVEL = DATA_COMPONENT_TYPES.register("level", () ->
			DataComponentType.<Integer>builder()
					.persistent(ExtraCodecs.intRange(0, 15))
					.networkSynchronized(ByteBufCodecs.VAR_INT)
					.build());


	public static final DeferredItem<HeadlightHelmetItem> HEADLIGHT = ITEMS.registerItem("headlight", HeadlightHelmetItem::new);

	public static final Supplier<CreativeModeTab> HEADLIGHT_TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
			.icon(() -> new ItemStack(LightRegistry.HEADLIGHT.get()))
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.headlight"))
			.displayItems((features, output) -> {
				List<ItemStack> stacks = LightRegistry.ITEMS.getEntries().stream().map(reg -> new ItemStack(reg.get())).toList();
				output.acceptAll(stacks);
			}).build());

	public static void registerCapabilities(RegisterCapabilitiesEvent event) {
		event.registerItem(Capabilities.Item.ITEM, (stack, access) ->
						new LightInventory(access, stack),
				LightRegistry.HEADLIGHT);
	}
}