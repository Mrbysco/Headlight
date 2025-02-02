package com.mrbysco.headlight.datagen.assets;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.registry.LightRegistry;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class LightLanguageProvider extends LanguageProvider {
	public LightLanguageProvider(PackOutput packOutput) {
		super(packOutput, HeadlightMod.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		add("itemGroup.headlight", "Headlight");

		addItem(LightRegistry.HEADLIGHT, "Headlight Helmet");

		add("headlight.container.headlight", "Headlight Helmet");
	}
}
