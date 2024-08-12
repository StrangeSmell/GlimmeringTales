package dev.xkmc.glimmeringtales.content.block.api;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public interface CropGrowListener {

	void onNeighborGrow(ServerLevel level, BlockState state, BlockPos pos, BlockState source);

}
