package com.mrbysco.headlight.items;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.client.ClientHandler;
import com.mrbysco.headlight.client.model.HeadlightModel;
import com.mrbysco.headlight.light.LightManager;
import com.mrbysco.headlight.menu.HeadlightMenu;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class HeadlightHelmetItem extends ArmorItem {

	public HeadlightHelmetItem(ArmorMaterial material, Properties properties) {
		super(material, Type.HELMET, properties);
	}

	@NotNull
	@Override
	public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player playerIn, @NotNull InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		IItemHandler handler = stack.getCapability(ForgeCapabilities.ITEM_HANDLER).orElse(null);
		if (handler != null && !playerIn.isShiftKeyDown()) {
			playerIn.openMenu(this.getContainer(stack));
			return new InteractionResultHolder<>(InteractionResult.PASS, stack);
		} else {
			return super.use(level, playerIn, handIn);
		}
	}

	@Nullable
	public MenuProvider getContainer(ItemStack stack) {
		return new SimpleMenuProvider((id, inventory, player) ->
				new HeadlightMenu(id, inventory, stack), stack.hasCustomHoverName() ?
				((MutableComponent) stack.getHoverName()).withStyle(ChatFormatting.BLACK) :
				Component.translatable(HeadlightMod.MOD_ID + ".container.headlight"));
	}

	@Nullable
	@Override
	public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
		return new HeadlightHelmetItem.InventoryProvider(stack);
	}

	private static class InventoryProvider implements ICapabilitySerializable<CompoundTag> {
		private final LazyOptional<ItemStackHandler> inventory = LazyOptional.of(() -> new ItemStackHandler(1) {
			@Override
			public boolean isItemValid(int slot, @NotNull ItemStack stack) {
				return stack.is(HeadlightMod.LIGHTS) && super.isItemValid(slot, stack);
			}

			@Override
			public int getSlotLimit(int slot) {
				return 1;
			}

			@Override
			protected void onContentsChanged(int slot) {
				super.onContentsChanged(slot);
				ItemStack lightStack = getStackInSlot(slot);
				CompoundTag tag = lightStack.getOrCreateTag();
				if (lightStack.isEmpty()) {
					tag.remove("headlight_level");
				} else {
					int lightLevel = LightManager.getValue(lightStack.getItem());
					if (lightLevel > 0)
						tag.putInt("headlight_level", lightLevel);
					else
						tag.remove("headlight_level");
				}
				if(tag.isEmpty())
					headlight.setTag(null);
				else
					headlight.setTag(tag);
			}
		});

		private final ItemStack headlight;

		public InventoryProvider(ItemStack stack) {
			this.headlight = stack;
		}

		@NotNull
		@Override
		public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
			if (cap == ForgeCapabilities.ITEM_HANDLER)
				return inventory.cast();
			else return LazyOptional.empty();
		}

		@Override
		public CompoundTag serializeNBT() {
			if (inventory.isPresent()) {
				return inventory.resolve().get().serializeNBT();
			}
			return new CompoundTag();
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			inventory.ifPresent(h -> h.deserializeNBT(nbt));
		}
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			private final LazyLoadedValue<HeadlightModel> model = new LazyLoadedValue<>(this::provideHeadlightModel);

			public HeadlightModel provideHeadlightModel() {
				return new HeadlightModel(Minecraft.getInstance().getEntityModels().bakeLayer(ClientHandler.HEADLIGHT));
			}

			@NotNull
			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
				return model.get();
			}
		});
	}

	@Override
	public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
		return HeadlightMod.modLoc("textures/models/armor/headlight.png").toString();
	}
}
