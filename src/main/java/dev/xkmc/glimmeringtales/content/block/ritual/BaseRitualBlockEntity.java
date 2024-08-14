package dev.xkmc.glimmeringtales.content.block.ritual;

import dev.xkmc.l2core.base.tile.BaseBlockEntity;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

@SerialClass
public class BaseRitualBlockEntity extends BaseBlockEntity {

	@SerialField
	private ItemStack stack = ItemStack.EMPTY;

	private final RitualItemHandler handler = new RitualItemHandler(this);

	public BaseRitualBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void setItem(ItemStack stack) {
		this.stack = stack;
		sync();
		setChanged();
	}

	public ItemStack getItem() {
		return stack;
	}

	public IItemHandler getItemHandler(@Nullable Direction direction) {
		return handler;
	}

}
