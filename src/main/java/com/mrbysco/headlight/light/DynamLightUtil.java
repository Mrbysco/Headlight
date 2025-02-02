package com.mrbysco.headlight.light;

import com.mrbysco.headlight.registry.LightRegistry;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;

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
			if (headStack.is(LightRegistry.HEADLIGHT.get())) {
				var itemHandler = headStack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
				ItemStack lightStack = itemHandler.getStackInSlot(0);
				if (itemHandler != null && !lightStack.isEmpty()) {
					return LightManager.getLightRegistry().containsKey(lightStack.getItem());
				}
			}
		}
		return false;
	}

	public static int lightForEntity(Entity entity) {
		if (entity instanceof LivingEntity livingEntity) {
			ItemStack headStack = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
			if (headStack.is(LightRegistry.HEADLIGHT.get())) {
				var itemHandler = headStack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
				if (itemHandler != null) {
					ItemStack lightStack = itemHandler.getStackInSlot(0);
					if (!lightStack.isEmpty()) {
						return LightManager.getValue(lightStack.getItem());
					}
				}
			}
		}
		return 0;
	}
}
