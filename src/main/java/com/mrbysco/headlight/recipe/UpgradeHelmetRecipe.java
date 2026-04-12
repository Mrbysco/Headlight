package com.mrbysco.headlight.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mrbysco.headlight.registry.LightRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

public class UpgradeHelmetRecipe extends CustomRecipe {
	public final static Predicate<ItemStack> IS_HELMET = stack ->
			stack.has(DataComponents.EQUIPPABLE) &&
					Objects.requireNonNull(stack.get(DataComponents.EQUIPPABLE)).slot() == EquipmentSlot.HEAD;

	public static final MapCodec<UpgradeHelmetRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
			i -> i.group(
							Ingredient.CODEC.fieldOf("material").forGetter(o -> o.material)
					)
					.apply(i, UpgradeHelmetRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, UpgradeHelmetRecipe> STREAM_CODEC = StreamCodec.composite(
			Ingredient.CONTENTS_STREAM_CODEC,
			o -> o.material,
			UpgradeHelmetRecipe::new
	);
	public static final RecipeSerializer<UpgradeHelmetRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	private final Ingredient material;

	public UpgradeHelmetRecipe(Ingredient material) {
		this.material = material;
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		if (input.ingredientCount() != 2) {
			return false;
		} else {
			// Get list of non-empty stacks in the crafting grid
			ArrayList<ItemStack> list = new ArrayList<>();
			for (int i = 0; i < input.size(); ++i) {
				ItemStack stack = input.getItem(i);
				if (!stack.isEmpty()) {
					list.add(stack);
				}
			}
			// Check if first item is helmet and other the material
			return list.size() == 2 && RecipeMatcher.findMatches(list, List.of(material, IS_HELMET)) != null;
		}
	}

	@Override
	public ItemStack assemble(CraftingInput input) {
		// Get helmet ItemStack
		for (int slot = 0; slot < input.size(); slot++) {
			ItemStack itemStack = input.getItem(slot).copy();
			if (IS_HELMET.test(itemStack)) {
				itemStack.set(LightRegistry.HEADLIGHT_CONTENTS, ItemContainerContents.EMPTY);
				return itemStack;
			}
		}
		return null;
	}


	@Override
	public RecipeSerializer<? extends CustomRecipe> getSerializer() {
		return LightRegistry.UPGRADE_HELMET.get();
	}
}
