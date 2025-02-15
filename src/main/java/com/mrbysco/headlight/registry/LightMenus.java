package com.mrbysco.headlight.registry;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.menu.HeadlightMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class LightMenus {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, HeadlightMod.MOD_ID);

	public static final Supplier<MenuType<HeadlightMenu>> HEADLIGHT = MENU_TYPES.register("headlight", () ->
			IMenuTypeExtension.create((windowId, inv, data) -> new HeadlightMenu(windowId, inv)));
}
