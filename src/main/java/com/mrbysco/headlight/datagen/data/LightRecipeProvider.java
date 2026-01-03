package com.mrbysco.headlight.datagen.data;

import com.mrbysco.headlight.registry.LightRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;


public class LightRecipeProvider extends RecipeProvider {
	public LightRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
		super(provider, recipeOutput);
	}

	@Override
	protected void buildRecipes() {
		shaped(RecipeCategory.COMBAT, LightRegistry.HEADLIGHT.get())
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

	public static class Runner extends RecipeProvider.Runner {
		public Runner(PackOutput output, CompletableFuture<Provider> completableFuture) {
			super(output, completableFuture);
		}

		@Override
		protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
			return new LightRecipeProvider(provider, recipeOutput);
		}

		@Override
		public String getName() {
			return "Headlight Recipes";
		}
	}
}