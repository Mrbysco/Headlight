package com.mrbysco.headlight.datagen.assets;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.registry.LightRegistry;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class LightItemModelsProvider extends ItemModelProvider {
	public LightItemModelsProvider(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, HeadlightMod.MOD_ID, helper);
	}

	@Override
	protected void registerModels() {
		singleTexture(LightRegistry.HEADLIGHT.getId().getPath(), mcLoc("item/generated"), "layer0", modLoc("item/headlight"));
	}
}