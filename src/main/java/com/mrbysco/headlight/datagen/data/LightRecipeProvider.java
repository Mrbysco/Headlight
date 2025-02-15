package com.mrbysco.headlight.datagen.data;

import com.mrbysco.headlight.registry.LightRegistry;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;


public class LightRecipeProvider extends RecipeProvider {
	public LightRecipeProvider(PackOutput packOutput, CompletableFuture<Provider> lookupProvider) {
		super(packOutput, lookupProvider);
	}

	@Override
	protected void buildRecipes(@NotNull RecipeOutput output) {
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, LightRegistry.HEADLIGHT.get())
				.pattern(" F ")
				.pattern("LIL")
				.pattern("I I")
				.define('F', Items.ITEM_FRAME)
				.define('L', Tags.Items.LEATHERS)
				.define('I', Tags.Items.INGOTS_IRON)
				.unlockedBy("has_item", has(Items.ITEM_FRAME))
				.unlockedBy("has_leather", has(Tags.Items.LEATHERS))
				.unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
				.save(output);
	}
}