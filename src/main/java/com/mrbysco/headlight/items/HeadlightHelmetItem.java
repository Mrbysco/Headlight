package com.mrbysco.headlight.items;

import com.mrbysco.headlight.HeadlightMod;
import com.mrbysco.headlight.client.ClientHandler;
import com.mrbysco.headlight.client.model.HeadlightModel;
import com.mrbysco.headlight.client.renderer.HeadlightRenderer;
import com.mrbysco.headlight.light.LightManager;
import com.mrbysco.headlight.menu.HeadlightMenu;
import com.mrbysco.headlight.registry.LightRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
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
import net.minecraft.world.item.ArmorMaterial.Layer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.items.ComponentItemHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@SuppressWarnings("deprecation")
public class HeadlightHelmetItem extends ArmorItem {

	public HeadlightHelmetItem(Holder<ArmorMaterial> materialHolder, Properties properties) {
		super(materialHolder, Type.HELMET, properties.durability(ArmorItem.Type.HELMET.getDurability(15)));
	}

	@NotNull
	@Override
	public InteractionResultHolder<ItemStack> use(@NotNull Level level, Player playerIn, @NotNull InteractionHand handIn) {
		ItemStack stack = playerIn.getItemInHand(handIn);
		IItemHandler handler = stack.getCapability(Capabilities.ItemHandler.ITEM);
		if (handler != null && !playerIn.isShiftKeyDown()) {
			playerIn.openMenu(this.getContainer(stack));
			return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
		} else {
			return super.use(level, playerIn, handIn);
		}
	}

	@Nullable
	public MenuProvider getContainer(ItemStack stack) {
		return new SimpleMenuProvider((id, inventory, player) ->
				new HeadlightMenu(id, inventory, stack), stack.has(DataComponents.CUSTOM_NAME) ?
				((MutableComponent) stack.getHoverName()).withStyle(ChatFormatting.BLACK) :
				Component.translatable(HeadlightMod.MOD_ID + ".container.headlight"));
	}

	public static class LightInventory extends ComponentItemHandler {
		public LightInventory(ItemStack stack) {
			super(stack, LightRegistry.HEADLIGHT_CONTENTS.get(), 1);
		}

		@Override
		protected void onContentsChanged(int slot, ItemStack oldStack, ItemStack newStack) {
			super.onContentsChanged(slot, oldStack, newStack);
			ItemStack lightStack = getStackInSlot(slot);
			if (lightStack.isEmpty()) {
				parent.remove(LightRegistry.LIGHT_SOURCE);
				parent.remove(LightRegistry.LIGHT_LEVEL);
			} else {
				int lightLevel = LightManager.getValue(lightStack.getItem());
				if (lightLevel > 0)
					parent.set(LightRegistry.LIGHT_LEVEL, lightLevel);
				else
					parent.remove(LightRegistry.LIGHT_LEVEL);

				ResourceLocation itemID = BuiltInRegistries.ITEM.getKey(lightStack.getItem());
				if (itemID != null)
					parent.set(LightRegistry.LIGHT_SOURCE, itemID);
				else
					parent.remove(LightRegistry.LIGHT_SOURCE);
			}
		}
	}

	@SuppressWarnings("removal")
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			private final LazyLoadedValue<HeadlightModel<?>> model = new LazyLoadedValue<>(this::provideHeadlightModel);

			public HeadlightModel<?> provideHeadlightModel() {
				return new HeadlightModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(ClientHandler.HEADLIGHT));
			}

			@NotNull
			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
				ItemStack source = HeadlightRenderer.INSTANCE.getSourceItem(itemStack);
				var helmetModel = model.get();
				helmetModel.setSourceStack(source);
				return helmetModel;
			}
		});
	}

	@Override
	public @Nullable ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, Layer layer, boolean innerModel) {
		return HeadlightMod.modLoc("textures/models/armor/headlight.png");
	}
}
