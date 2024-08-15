package dev.xkmc.glimmeringtales.content.block.ritual;

import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2modularblock.tile_api.BlockContainer;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@SerialClass
public class BaseRitualBlockEntity extends BaseBlockEntity implements BlockContainer {

	@SerialField
	private ItemStack stack = ItemStack.EMPTY;

	private final RitualItemHandler handler = new RitualItemHandler(this);

	public BaseRitualBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void setItem(ItemStack stack) {
		if (locked()) return;
		this.stack = stack;
	}

	public ItemStack getItem() {
		return stack;
	}

	public IItemHandler getItemHandler(@Nullable Direction direction) {
		return handler;
	}

	@Override
	public List<Container> getContainers() {
		return List.of(new SimpleContainer(stack));
	}

	public boolean locked() {
		return false;
	}

	public void onReplaced() {

	}

	public void onPlaced() {

	}

}
