package com.mrbysco.headlight.client.extension;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.client.model.HeadlightModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

public class HeadlightExtension implements IClientItemExtensions {
	private static final HeadlightModel<?> HEADLIGHT_MODEL = new HeadlightModel<>(HeadlightModel.createHeadlightLayer().bakeRoot());

	@Override
	public Identifier getArmorTexture(ItemStack stack, EquipmentClientInfo.LayerType type, EquipmentClientInfo.Layer layer, Identifier _default) {
		return HeadlightMod.modLoc("textures/models/armor/headlight.png");
	}

	@Override
	public Model getHumanoidArmorModel(ItemStack itemStack, EquipmentClientInfo.LayerType layerType, Model original) {
		Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
		EquipmentSlot slot = equippable != null ? equippable.slot() : null;

		if (slot == EquipmentSlot.HEAD) {
			return HEADLIGHT_MODEL;
		}

		return original;
	}
}
