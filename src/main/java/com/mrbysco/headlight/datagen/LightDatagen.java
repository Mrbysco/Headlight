package com.mrbysco.headlight.datagen;

import com.mrbysco.headlight.datagen.assets.LightLanguageProvider;
import com.mrbysco.headlight.datagen.assets.LightModelProvider;
import com.mrbysco.headlight.datagen.data.LightBlockTagsProvider;
import com.mrbysco.headlight.datagen.data.LightItemTagsProvider;
import com.mrbysco.headlight.datagen.data.LightRecipeProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class LightDatagen {
	@SubscribeEvent
	public static void gatherData(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput packOutput = generator.getPackOutput();
		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		generator.addProvider(true, new LightRecipeProvider.Runner(packOutput, lookupProvider));
		generator.addProvider(true, new LightBlockTagsProvider(packOutput, lookupProvider));
		generator.addProvider(true, new LightItemTagsProvider(packOutput, lookupProvider));

		generator.addProvider(true, new LightLanguageProvider(packOutput));
		generator.addProvider(true, new LightModelProvider(packOutput));

	}
}