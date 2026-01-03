package com.mrbysco.headlight.datagen.data;

import com.mrbysco.headlight.HeadlightMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class LightBlockTagsProvider extends BlockTagsProvider {
	public LightBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
		super(output, lookupProvider, HeadlightMod.MOD_ID);
	}

	@Override
	protected void addTags(@NotNull HolderLookup.Provider provider) {

	}
}