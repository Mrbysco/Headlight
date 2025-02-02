package com.mrbysco.headlight.registry;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.menu.HeadlightMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class LightMenus {
	public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, HeadlightMod.MOD_ID);

	public static final RegistryObject<MenuType<HeadlightMenu>> HEADLIGHT = MENU_TYPES.register("headlight", () ->
			IForgeMenuType.create((windowId, inv, data) -> new HeadlightMenu(windowId, inv)));
}
