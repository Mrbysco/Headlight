package com.mrbysco.headlight.light;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.Level;

/**
 * Represents a dynamic light source.
 * This code is taken from the 1.20 branch of Ars Nouveau, an LGPG 3 licensed mod: <a href="https://github.com/baileyholl/Ars-Nouveau/tree/1.20">Github Link</a>
 * Which in turn is a straight implementation from <a href="https://github.com/LambdAurora/LambDynamicLights">LambDynamicLights</a>, a super awesome Fabric mod!
 * The methods in this interface that are used by mixins have been prefixed with {@code headlight$} to avoid conflicts with other mods that may implement the same interface from LambDynamicLights.
 *
 * @author LambdAurora
 * @version 1.3.3
 * @since 1.0.0
 */
public interface LambDynamicLight {
	/**
	 * Returns the dynamic light source X coordinate.
	 *
	 * @return the X coordinate
	 */
	double headlight$getDynamicLightX();

	/**
	 * Returns the dynamic light source Y coordinate.
	 *
	 * @return the Y coordinate
	 */
	double headlight$getDynamicLightY();

	/**
	 * Returns the dynamic light source Z coordinate.
	 *
	 * @return the Z coordinate
	 */
	double headlight$getDynamicLightZ();

	/**
	 * Returns the dynamic light source world.
	 *
	 * @return the world instance
	 */
	Level headlight$getDynamicLightWorld();

	/**
	 * Returns whether the dynamic light is enabled or not.
	 *
	 * @return {@code true} if the dynamic light is enabled, else {@code false}
	 */
	default boolean isDynamicLightEnabled() {
		return LightManager.containsLightSource(this);
	}

	void headlight$resetDynamicLight();

	default void headlight$setDynamicLightEnabled(boolean enabled) {
		this.headlight$resetDynamicLight();
		if (enabled)
			LightManager.addLightSource(this);
		else
			LightManager.removeLightSource(this);
	}

	/**
	 * Returns the luminance of the light source.
	 * The maximum is 15, below 1 values are ignored.
	 *
	 * @return the luminance of the light source
	 */
	int headlight$getLuminance();

	/**
	 * Executed at each tick.
	 */
	void headlight$dynamicLightTick();

	/**
	 * Returns whether this dynamic light source should update.
	 *
	 * @return {@code true} if this dynamic light source should update, else {@code false}
	 */
	boolean headlight$shouldUpdateDynamicLight();

	boolean headlight$updateDynamicLight(LevelRenderer renderer);

	void headlight$scheduleTrackedChunksRebuild(LevelRenderer renderer);
}
