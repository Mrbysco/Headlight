package com.mrbysco.headlight.light;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.registry.LightRegistry;
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
			int level = headStack.getOrDefault(LightRegistry.LIGHT_LEVEL, 0);
			if (headStack.is(HeadlightMod.HEADLIGHT_HELMETS)) {
				return level > 0;
			}
		}
		return false;
	}

	public static int lightForEntity(Entity entity) {
		if (entity instanceof LivingEntity livingEntity) {
			ItemStack headStack = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
			int level = headStack.getOrDefault(LightRegistry.LIGHT_LEVEL, 0);
			if (headStack.is(HeadlightMod.HEADLIGHT_HELMETS)) {
				return level;
			}
		}
		return 0;
	}
}
