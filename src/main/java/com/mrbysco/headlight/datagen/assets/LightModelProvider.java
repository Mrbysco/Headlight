package com.mrbysco.headlight.datagen.assets;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.registry.LightRegistry;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;

public class LightModelProvider extends ModelProvider {
	public LightModelProvider(PackOutput packOutput) {
		super(packOutput, HeadlightMod.MOD_ID);
	}

	@Override
	protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
		itemModels.generateFlatItem(LightRegistry.HEADLIGHT.get(), ModelTemplates.FLAT_ITEM);
	}
}