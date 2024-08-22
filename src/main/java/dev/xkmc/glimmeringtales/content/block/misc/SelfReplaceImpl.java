package dev.xkmc.glimmeringtales.content.block.misc;

import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2modularblock.mult.ScheduleTickBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public record SelfReplaceImpl() implements ScheduleTickBlockMethod {

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource randomSource) {
		var e = GTRegistries.REPLACE.get(level.registryAccess(), state.getBlockHolder());
		if (e == null) level.removeBlock(pos, false);
		else level.setBlockAndUpdate(pos, e.state(state));
	}

}
