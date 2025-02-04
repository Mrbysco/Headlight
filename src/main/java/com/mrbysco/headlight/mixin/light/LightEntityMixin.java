package com.mrbysco.headlight.mixin.light;

import com.mrbysco.headlight.light.DynamLightUtil;
import com.mrbysco.headlight.light.LambDynamicLight;
import com.mrbysco.headlight.light.LightManager;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class LightEntityMixin implements LambDynamicLight {
	@Shadow
	private Level level;

	@Shadow
	public abstract double getX();

	@Shadow
	public abstract double getEyeY();

	@Shadow
	public abstract double getZ();

	@Shadow
	public abstract double getY();


	@Shadow
	public abstract BlockPos blockPosition();

	@Shadow
	public abstract boolean isRemoved();

	@Shadow
	private ChunkPos chunkPosition;

	@Unique
	protected int lambdynlights$luminance = 0;
	@Unique
	private int lambdynlights$lastLuminance = 0;
	@Unique
	private long lambdynlights$lastUpdate = 0;
	@Unique
	private double lambdynlights$prevX;
	@Unique
	private double lambdynlights$prevY;
	@Unique
	private double lambdynlights$prevZ;
	@Unique
	private LongOpenHashSet lambdynlights$trackedLitChunkPos = new LongOpenHashSet();

	@Inject(method = "tick", at = @At("TAIL"))
	public void onTick(CallbackInfo ci) {
		// We do not want to update the entity on the server.
		if (level.isClientSide && !LightManager.shouldUpdateDynamicLight()) {
			lambdynlights$luminance = 0;
		}
		if (this.level.isClientSide() && LightManager.shouldUpdateDynamicLight()) {
			if (this.isRemoved()) {
				this.headlight$setDynamicLightEnabled(false);
			} else {
				this.headlight$dynamicLightTick();
				LightManager.updateTracking(this);
			}
		}
	}

	@Inject(method = "remove", at = @At("TAIL"))
	public void onRemove(CallbackInfo ci) {
		if (this.level.isClientSide()) {
			this.headlight$setDynamicLightEnabled(false);
		}
	}

	@Inject(method = "onClientRemoval", at = @At("TAIL"))
	public void removed(CallbackInfo ci) {
		if (this.level.isClientSide()) {
			this.headlight$setDynamicLightEnabled(false);
		}
	}

	@Override
	public double headlight$getDynamicLightX() {
		return this.getX();
	}

	@Override
	public double headlight$getDynamicLightY() {
		return this.getEyeY();
	}

	@Override
	public double headlight$getDynamicLightZ() {
		return this.getZ();
	}

	@Override
	public Level headlight$getDynamicLightWorld() {
		return this.level;
	}

	@Override
	public void headlight$resetDynamicLight() {
		this.lambdynlights$lastLuminance = 0;
	}

	@SuppressWarnings("ConstantValue")
	@Override
	public boolean headlight$shouldUpdateDynamicLight() {
		return LightManager.shouldUpdateDynamicLight() && DynamLightUtil.couldGiveLight((Entity) (Object) this);
	}

	@Override
	public void headlight$dynamicLightTick() {
		lambdynlights$luminance = 0;
		int luminance = DynamLightUtil.lightForEntity((Entity) (Object) this);
		if (luminance > this.lambdynlights$luminance)
			this.lambdynlights$luminance = luminance;
	}

	@Override
	public int headlight$getLuminance() {
		return this.lambdynlights$luminance;
	}

	@Override
	public boolean headlight$updateDynamicLight(LevelRenderer renderer) {
		if (!this.headlight$shouldUpdateDynamicLight())
			return false;
		double deltaX = this.getX() - this.lambdynlights$prevX;
		double deltaY = this.getY() - this.lambdynlights$prevY;
		double deltaZ = this.getZ() - this.lambdynlights$prevZ;

		int luminance = this.headlight$getLuminance();

		if (Math.abs(deltaX) > 0.1D || Math.abs(deltaY) > 0.1D || Math.abs(deltaZ) > 0.1D || luminance != this.lambdynlights$lastLuminance) {
			this.lambdynlights$prevX = this.getX();
			this.lambdynlights$prevY = this.getY();
			this.lambdynlights$prevZ = this.getZ();
			this.lambdynlights$lastLuminance = luminance;

			var newPos = new LongOpenHashSet();

			if (luminance > 0) {
				var entityChunkPos = this.chunkPosition;
				var chunkPos = new BlockPos.MutableBlockPos(entityChunkPos.x, DynamLightUtil.getSectionCoord(this.getEyeY()), entityChunkPos.z);

				LightManager.scheduleChunkRebuild(renderer, chunkPos);
				LightManager.updateTrackedChunks(chunkPos, this.lambdynlights$trackedLitChunkPos, newPos);

				var directionX = (this.blockPosition().getX() & 15) >= 8 ? Direction.EAST : Direction.WEST;
				var directionY = (Mth.floor(this.getEyeY()) & 15) >= 8 ? Direction.UP : Direction.DOWN;
				var directionZ = (this.blockPosition().getZ() & 15) >= 8 ? Direction.SOUTH : Direction.NORTH;

				for (int i = 0; i < 7; i++) {
					if (i % 4 == 0) {
						chunkPos.move(directionX); // X
					} else if (i % 4 == 1) {
						chunkPos.move(directionZ); // XZ
					} else if (i % 4 == 2) {
						chunkPos.move(directionX.getOpposite()); // Z
					} else {
						chunkPos.move(directionZ.getOpposite()); // origin
						chunkPos.move(directionY); // Y
					}
					LightManager.scheduleChunkRebuild(renderer, chunkPos);
					LightManager.updateTrackedChunks(chunkPos, this.lambdynlights$trackedLitChunkPos, newPos);
				}
			}
			// Schedules the rebuild of removed chunks.
			this.headlight$scheduleTrackedChunksRebuild(renderer);
			// Update tracked lit chunks.
			this.lambdynlights$trackedLitChunkPos = newPos;
			return true;
		}
		return false;
	}

	@Override
	public void headlight$scheduleTrackedChunksRebuild(LevelRenderer renderer) {
		if (Minecraft.getInstance().level == this.level)
			for (long pos : this.lambdynlights$trackedLitChunkPos) {
				LightManager.scheduleChunkRebuild(renderer, pos);
			}
	}
}
