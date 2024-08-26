package dev.xkmc.glimmeringtales.content.block.altar;

import dev.xkmc.l2modularblock.mult.OnPlaceBlockMethod;
import dev.xkmc.l2modularblock.mult.OnReplacedBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class RitualLinkBlockMethod implements OnPlaceBlockMethod, OnReplacedBlockMethod {

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState old, boolean moving) {
		if (level.getBlockEntity(pos) instanceof BaseRitualBlockEntity be) {
			be.onPlaced();
		}
	}

	@Override
	public void onReplaced(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (state.getBlock() == newState.getBlock()) return;
		if (level.getBlockEntity(pos) instanceof BaseRitualBlockEntity be) {
			be.onReplaced();
		}
	}

}
