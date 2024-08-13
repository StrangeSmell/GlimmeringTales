package dev.xkmc.glimmeringtales.content.block.misc;

import dev.xkmc.l2modularblock.mult.ScheduleTickBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public record SelfDestroyImpl() implements ScheduleTickBlockMethod {

	@Override
	public void tick(BlockState blockState, ServerLevel level, BlockPos pos, RandomSource randomSource) {
		level.removeBlock(pos, false);
	}

}
