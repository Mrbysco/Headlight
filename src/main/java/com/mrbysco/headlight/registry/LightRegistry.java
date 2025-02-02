package com.mrbysco.headlight.registry;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.items.HeadlightHelmetItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class LightRegistry {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, HeadlightMod.MOD_ID);
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, HeadlightMod.MOD_ID);
	public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HeadlightMod.MOD_ID);

	public static final RegistryObject<Item> HEADLIGHT = ITEMS.register("headlight", () ->
			new HeadlightHelmetItem(ArmorMaterials.IRON, itemBuilder()));

	private static Item.Properties itemBuilder() {
		return new Item.Properties();
	}

	public static final RegistryObject<CreativeModeTab> HEADLIGHT_TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
			.icon(() -> new ItemStack(LightRegistry.HEADLIGHT.get()))
			.withTabsBefore(CreativeModeTabs.SPAWN_EGGS)
			.title(Component.translatable("itemGroup.headlight"))
			.displayItems((features, output) -> {
				List<ItemStack> stacks = LightRegistry.ITEMS.getEntries().stream().map(reg -> new ItemStack(reg.get())).toList();
				output.acceptAll(stacks);
			}).build());
}
