package com.mrbysco.headlight.datagen.data;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.registry.LightRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class LightItemTagsProvider extends ItemTagsProvider {
	public LightItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, HeadlightMod.MOD_ID);
	}

	@Override
	protected void addTags(@NotNull HolderLookup.Provider provider) {
		this.tag(HeadlightMod.HEADLIGHT_HELMETS).add(LightRegistry.HEADLIGHT.get());
		this.tag(HeadlightMod.LIGHTS).add(
				Items.TORCH,
				Items.SOUL_TORCH,
				Items.REDSTONE_TORCH,
				Items.LANTERN,
				Items.SOUL_LANTERN,
				Items.SEA_LANTERN,
				Items.GLOWSTONE,
				Items.SHROOMLIGHT,
				Items.END_ROD,
				Items.JACK_O_LANTERN,
				Items.CANDLE,
				Items.WHITE_CANDLE,
				Items.ORANGE_CANDLE,
				Items.MAGENTA_CANDLE,
				Items.LIGHT_BLUE_CANDLE,
				Items.YELLOW_CANDLE,
				Items.LIME_CANDLE,
				Items.PINK_CANDLE,
				Items.GRAY_CANDLE,
				Items.LIGHT_GRAY_CANDLE,
				Items.CYAN_CANDLE,
				Items.PURPLE_CANDLE,
				Items.BLUE_CANDLE,
				Items.BROWN_CANDLE,
				Items.GREEN_CANDLE,
				Items.RED_CANDLE,
				Items.BLACK_CANDLE,
				Items.SEA_PICKLE,
				Items.OCHRE_FROGLIGHT,
				Items.VERDANT_FROGLIGHT,
				Items.PEARLESCENT_FROGLIGHT,
				Items.CAMPFIRE,
				Items.SOUL_CAMPFIRE
		);
	}
}