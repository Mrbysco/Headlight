package com.mrbysco.headlight.datagen;

import com.mrbysco.headlight.datagen.assets.LightItemModelsProvider;
import com.mrbysco.headlight.datagen.assets.LightLanguageProvider;
import com.mrbysco.headlight.datagen.data.LightBlockTagsProvider;
import com.mrbysco.headlight.datagen.data.LightItemTagsProvider;
import com.mrbysco.headlight.datagen.data.LightRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public class LightDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		if (event.includeServer()) {
			generator.addProvider(event.includeServer(), new LightRecipeProvider(packOutput, lookupProvider));
			BlockTagsProvider provider;
			generator.addProvider(event.includeServer(), provider = new LightBlockTagsProvider(packOutput, lookupProvider, existingFileHelper));
			generator.addProvider(event.includeServer(), new LightItemTagsProvider(packOutput, lookupProvider, provider, existingFileHelper));
		}
		if (event.includeClient()) {
			generator.addProvider(event.includeClient(), new LightLanguageProvider(packOutput));
			generator.addProvider(event.includeClient(), new LightItemModelsProvider(packOutput, existingFileHelper));
		}
	}
}