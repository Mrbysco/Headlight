package com.mrbysco.headlight.light;

import com.mrbysco.headlight.HeadlightMod;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * This code is taken from the 1.20 branch of Ars Nouveau, an LGPG 3 licensed mod: <a href="https://github.com/baileyholl/Ars-Nouveau/tree/1.20">Github Link</a>
 * Which in turn have taken the code from LambDynamicLights, an MIT fabric mod: <a href="https://github.com/LambdAurora/LambDynamicLights">Github Link</a>
 */
public class LightManager {

	private final static Set<LambDynamicLight> dynamicLightSources = new HashSet<>();
	private final static ReentrantReadWriteLock lightSourcesLock = new ReentrantReadWriteLock();

	public static long lastUpdate = System.currentTimeMillis();

	public static int lastUpdateCount = 0;
	private static final Map<Item, Integer> LIGHT_REGISTRY = new HashMap<>();

	@SuppressWarnings("deprecation")
	public static void init() {
		BuiltInRegistries.ITEM.get(HeadlightMod.LIGHTS).ifPresent(tag -> tag.forEach(item -> {
			if (item.value() instanceof BlockItem blockItem) {
				register(blockItem, blockItem.getBlock().defaultBlockState().getLightEmission());
			}
		}));
	}

	public static <T extends Item> void register(Item item, Integer luminance) {
		if (!LIGHT_REGISTRY.containsKey(item)) {
			LIGHT_REGISTRY.put(item, luminance);
		}
	}


	public static Map<Item, Integer> getLightRegistry() {
		return LIGHT_REGISTRY;
	}

	public static <T extends Item> int getValue(T item) {
		int val = 0;
		if (!LIGHT_REGISTRY.containsKey(item))
			return val;

		return LIGHT_REGISTRY.get(item);
	}

	/**
	 * Adds the light source to the tracked light sources.
	 *
	 * @param lightSource the light source to add
	 */
	public static void addLightSource(LambDynamicLight lightSource) {
		if (!lightSource.headlight$getDynamicLightWorld().isClientSide())
			return;
		if (!shouldUpdateDynamicLight())
			return;
		if (containsLightSource(lightSource))
			return;
		lightSourcesLock.writeLock().lock();
		dynamicLightSources.add(lightSource);
		lightSourcesLock.writeLock().unlock();
	}

	/**
	 * Returns whether the light source is tracked or not.
	 *
	 * @param lightSource the light source to check
	 * @return {@code true} if the light source is tracked, else {@code false}
	 */
	public static boolean containsLightSource(@NotNull LambDynamicLight lightSource) {
		if (!lightSource.headlight$getDynamicLightWorld().isClientSide())
			return false;

		boolean result;
		lightSourcesLock.readLock().lock();
		result = dynamicLightSources.contains(lightSource);
		lightSourcesLock.readLock().unlock();
		return result;
	}

	/**
	 * Returns the number of dynamic light sources that currently emit lights.
	 *
	 * @return the number of dynamic light sources emitting light
	 */
	public int getLightSourcesCount() {
		int result;

		lightSourcesLock.readLock().lock();
		result = dynamicLightSources.size();
		lightSourcesLock.readLock().unlock();

		return result;
	}

	/**
	 * Removes the light source from the tracked light sources.
	 *
	 * @param lightSource the light source to remove
	 */
	public static void removeLightSource(LambDynamicLight lightSource) {
		lightSourcesLock.writeLock().lock();

		var sourceIterator = dynamicLightSources.iterator();
		LambDynamicLight it;
		while (sourceIterator.hasNext()) {
			it = sourceIterator.next();
			if (it.equals(lightSource)) {
				sourceIterator.remove();
				if (Minecraft.getInstance().level != null)
					lightSource.headlight$scheduleTrackedChunksRebuild(Minecraft.getInstance().levelRenderer);
				break;
			}
		}

		lightSourcesLock.writeLock().unlock();
	}

	/**
	 * Clears light sources.
	 */
	public static void clearLightSources() {
		lightSourcesLock.writeLock().lock();

		var sourceIterator = dynamicLightSources.iterator();
		LambDynamicLight it;
		while (sourceIterator.hasNext()) {
			it = sourceIterator.next();
			sourceIterator.remove();
			if (Minecraft.getInstance().levelRenderer != null) {
				if (it.headlight$getLuminance() > 0)
					it.headlight$resetDynamicLight();
				it.headlight$scheduleTrackedChunksRebuild(Minecraft.getInstance().levelRenderer);
			}
		}

		lightSourcesLock.writeLock().unlock();
	}


	/**
	 * Schedules a chunk rebuild at the specified chunk position.
	 *
	 * @param renderer the renderer
	 * @param chunkPos the chunk position
	 */
	public static void scheduleChunkRebuild(@NotNull LevelRenderer renderer, @NotNull BlockPos chunkPos) {
		scheduleChunkRebuild(renderer, chunkPos.getX(), chunkPos.getY(), chunkPos.getZ());
	}

	/**
	 * Schedules a chunk rebuild at the specified chunk position.
	 *
	 * @param renderer the renderer
	 * @param chunkPos the packed chunk position
	 */
	public static void scheduleChunkRebuild(@NotNull LevelRenderer renderer, long chunkPos) {
		scheduleChunkRebuild(renderer, BlockPos.getX(chunkPos), BlockPos.getY(chunkPos), BlockPos.getZ(chunkPos));
	}

	public static void scheduleChunkRebuild(@NotNull LevelRenderer renderer, int x, int y, int z) {
		if (Minecraft.getInstance().level != null)
			renderer.setSectionDirty(x, y, z);
	}

	/**
	 * Updates all light sources.
	 *
	 * @param renderer the renderer
	 */
	public static void updateAll(LevelRenderer renderer) {

		lastUpdate = System.currentTimeMillis();
		lastUpdateCount = 0;

		lightSourcesLock.readLock().lock();
		for (var lightSource : dynamicLightSources) {
			if (lightSource.headlight$updateDynamicLight(renderer)) {
				lastUpdateCount++;
			}
		}
		lightSourcesLock.readLock().unlock();

	}

	/**
	 * Updates the tracked chunk sets.
	 *
	 * @param chunkPos the packed chunk position
	 * @param old      the set of old chunk coordinates to remove this chunk from it
	 * @param newPos   the set of new chunk coordinates to add this chunk to it
	 */
	public static void updateTrackedChunks(@NotNull BlockPos chunkPos, @Nullable LongOpenHashSet old, @Nullable LongOpenHashSet newPos) {
		if (old != null || newPos != null) {
			long pos = chunkPos.asLong();
			if (old != null)
				old.remove(pos);
			if (newPos != null)
				newPos.add(pos);
		}
	}

	public static int getLightmapWithDynamicLight(@NotNull BlockPos pos, int lightmap) {
		return getLightmapWithDynamicLight(getDynamicLightLevel(pos), lightmap);
	}

	/**
	 * Returns the lightmap with combined light levels.
	 *
	 * @param dynamicLightLevel the dynamic light level
	 * @param lightmap          the vanilla lightmap coordinates
	 * @return the modified lightmap coordinates
	 */
	public static int getLightmapWithDynamicLight(double dynamicLightLevel, int lightmap) {
		if (dynamicLightLevel > 0) {
			// lightmap is (skyLevel << 20 | blockLevel << 4)

			// Get vanilla block light level.
			int blockLevel = getBlockLightNoPatch(lightmap);
			if (dynamicLightLevel > blockLevel) {
				// Equivalent to a << 4 bitshift with a little quirk: this one ensure more precision (more decimals are saved).
				int luminance = (int) (dynamicLightLevel * 16.0);
				lightmap &= 0xfff00000;
				lightmap |= luminance & 0x000fffff;
			}
		}

		return lightmap;
	}

	public static int getBlockLightNoPatch(int light) { // Reverts the forge patch to LightTexture.block
		return light >> 4 & '\uffff';
	}

	/**
	 * Returns the dynamic light level at the specified position.
	 *
	 * @param pos the position
	 * @return the dynamic light level at the specified position
	 */
	public static double getDynamicLightLevel(@NotNull BlockPos pos) {
		double result = 0;
		lightSourcesLock.readLock().lock();
		for (var lightSource : dynamicLightSources) {
			result = maxDynamicLightLevel(pos, lightSource, result);
		}
		lightSourcesLock.readLock().unlock();

		return Mth.clamp(result, 0, 15);
	}

	private static final double MAX_RADIUS = 7.75;
	private static final double MAX_RADIUS_SQUARED = MAX_RADIUS * MAX_RADIUS;

	/**
	 * Returns the dynamic light level generated by the light source at the specified position.
	 *
	 * @param pos               the position
	 * @param lightSource       the light source
	 * @param currentLightLevel the current surrounding dynamic light level
	 * @return the dynamic light level at the specified position
	 */
	public static double maxDynamicLightLevel(@NotNull BlockPos pos, @NotNull LambDynamicLight lightSource, double currentLightLevel) {
		int luminance = lightSource.headlight$getLuminance();
		if (luminance > 0) {
			// Can't use Entity#squaredDistanceTo because of eye Y coordinate.
			double dx = pos.getX() - lightSource.headlight$getDynamicLightX() + 0.5;
			double dy = pos.getY() - lightSource.headlight$getDynamicLightY() + 0.5;
			double dz = pos.getZ() - lightSource.headlight$getDynamicLightZ() + 0.5;

			double distanceSquared = dx * dx + dy * dy + dz * dz;
			// 7.75 because else we would have to update more chunks and that's not a good idea.
			// 15 (max range for blocks) would be too much and a bit cheaty.
			if (distanceSquared <= MAX_RADIUS_SQUARED) {
				double multiplier = 1.0 - Math.sqrt(distanceSquared) / MAX_RADIUS;
				double lightLevel = multiplier * luminance;
				if (lightLevel > currentLightLevel) {
					return lightLevel;
				}
			}
		}
		return currentLightLevel;
	}

	/**
	 * Updates the dynamic lights tracking.
	 *
	 * @param lightSource the light source
	 */
	public static void updateTracking(@NotNull LambDynamicLight lightSource) {
		boolean enabled = lightSource.headlight$isDynamicLightEnabled();
		int luminance = lightSource.headlight$getLuminance();
		if (!enabled && luminance > 0) {
			lightSource.headlight$setDynamicLightEnabled(true);
		} else if (enabled && luminance < 1) {
			lightSource.headlight$setDynamicLightEnabled(false);
		}
	}

	public static boolean shouldUpdateDynamicLight() {
		return true;
	}
}
