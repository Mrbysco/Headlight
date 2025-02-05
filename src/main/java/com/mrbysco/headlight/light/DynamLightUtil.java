package com.mrbysco.headlight.light;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.Reference;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public class DynamLightUtil {

	public static int getSectionCoord(double coord) {
		return getSectionCoord(Mth.floor(coord));
	}

	public static int getSectionCoord(int coord) {
		return coord >> 4;
	}

	public static boolean couldGiveLight(Entity entity) {
		if (entity instanceof LivingEntity livingEntity) {
			ItemStack headStack = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
			CompoundTag nbt = headStack.getTag();
			if (nbt != null && headStack.is(HeadlightMod.HEADLIGHT_HELMETS)) {
				return nbt.getInt(Reference.LEVEL_TAG) > 0;
			}
		}
		return false;
	}

	public static int lightForEntity(Entity entity) {
		if (entity instanceof LivingEntity livingEntity) {
			ItemStack headStack = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
			CompoundTag nbt = headStack.getTag();
			if (nbt != null && headStack.is(HeadlightMod.HEADLIGHT_HELMETS)) {
				return nbt.getInt(Reference.LEVEL_TAG);
			}
		}
		return 0;
	}
}
