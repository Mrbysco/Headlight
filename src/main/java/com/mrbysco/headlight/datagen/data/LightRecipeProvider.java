package com.mrbysco.headlight.datagen.data;

import com.mrbysco.headlight.registry.LightRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.Tags;

import java.util.function.Consumer;


public class LightRecipeProvider extends RecipeProvider {
	public LightRecipeProvider(PackOutput packOutput) {
		super(packOutput);
	}

	@Override
	protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
		ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, LightRegistry.HEADLIGHT.get())
				.pattern(" F ")
				.pattern("LIL")
				.pattern("I I")
				.define('F', Items.ITEM_FRAME)
				.define('L', Tags.Items.LEATHER)
				.define('I', Tags.Items.INGOTS_IRON)
				.unlockedBy("has_item", has(Items.ITEM_FRAME))
				.unlockedBy("has_leather", has(Tags.Items.LEATHER))
				.unlockedBy("has_iron", has(Tags.Items.INGOTS_IRON))
				.save(consumer);
	}
}