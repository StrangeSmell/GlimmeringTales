package dev.xkmc.glimmeringtales.content.block.ritual;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;

public record RitualItemHandler(BaseRitualBlockEntity be) implements IItemHandlerModifiable {

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		be.setItem(stack);
	}

	@Override
	public int getSlots() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return be.getItem();
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		ItemStack current = be.getItem();
		if (!current.isEmpty()) return stack;
		ItemStack copy = stack.copy();
		ItemStack result = copy.split(1);
		if (!simulate) be.setItem(result);
		return copy;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		ItemStack current = be.getItem();
		if (amount == 0 || current.isEmpty()) return ItemStack.EMPTY;
		if (!simulate) be.setItem(ItemStack.EMPTY);
		return current.copy();
	}

	@Override
	public int getSlotLimit(int slot) {
		return 1;
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return true;
	}

}
