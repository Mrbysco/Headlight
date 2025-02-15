package com.mrbysco.headlight.menu;

import com.mrbysco.headlight.items.HeadlightHelmetItem;
import com.mrbysco.headlight.registry.LightMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class HeadlightMenu extends AbstractContainerMenu {
	private ItemStack heldStack;

	public HeadlightMenu(int id, @NotNull Inventory playerInventory) {
		this(id, playerInventory, getHelmetInventory(playerInventory));
	}

	public static ItemStack getHelmetInventory(@NotNull Inventory playerInventory) {
		Player player = playerInventory.player;
		if (player.getMainHandItem().getItem() instanceof HeadlightHelmetItem) {
			return player.getMainHandItem();
		} else if (player.getOffhandItem().getItem() instanceof HeadlightHelmetItem) {
			return player.getOffhandItem();
		}
		return ItemStack.EMPTY;
	}

	public HeadlightMenu(int id, @NotNull Inventory playerInventory, @NotNull ItemStack helmetStack) {
		super(LightMenus.HEADLIGHT.get(), id);
		if (helmetStack.isEmpty()) {
			playerInventory.player.closeContainer();
			return;
		}

		this.heldStack = helmetStack;

		IItemHandler itemHandler = heldStack.getCapability(Capabilities.ItemHandler.ITEM);
		if (itemHandler != null) {
			this.addSlot(new SlotItemHandler(itemHandler, 0, 80, 20));

			//Player Inventory
			int xPos = 8;
			int yPos = 54;

			for (int y = 0; y < 3; ++y) {
				for (int x = 0; x < 9; ++x) {
					this.addSlot(new Slot(playerInventory, x + y * 9 + 9, xPos + x * 18, yPos + y * 18));
				}
			}

			for (int x = 0; x < 9; ++x) {
				this.addSlot(new Slot(playerInventory, x, xPos + x * 18, yPos + 58));
			}
		} else {
			playerInventory.player.closeContainer();
		}
	}

	@Override
	public void removed(@NotNull Player player) {
		super.removed(player);
	}

	@Override
	public boolean stillValid(@NotNull Player player) {
		return !heldStack.isEmpty() && !player.isSpectator();
	}

	@NotNull
	@Override
	public ItemStack quickMoveStack(@NotNull Player player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;

		if (index <= 1) {
			Slot slot = slots.get(index);

			if (slot != null && slot.hasItem()) {
				ItemStack itemstack1 = slot.getItem();
				itemstack = itemstack1.copy();

				if (itemstack.getItem() instanceof HeadlightHelmetItem)
					return ItemStack.EMPTY;

				int containerSlots = slots.size() - player.getInventory().items.size();

				if (index < containerSlots) {
					if (!this.moveItemStackTo(itemstack1, containerSlots, slots.size(), true)) {
						return ItemStack.EMPTY;
					}
				} else if (!this.moveItemStackTo(itemstack1, 0, containerSlots, false)) {
					return ItemStack.EMPTY;
				}

				if (itemstack1.getCount() == 0) {
					slot.set(ItemStack.EMPTY);
				} else {
					slot.setChanged();
				}

				if (itemstack1.getCount() == itemstack.getCount()) {
					return ItemStack.EMPTY;
				}

				slot.onTake(player, itemstack1);
			}
		}

		return itemstack;
	}
}
