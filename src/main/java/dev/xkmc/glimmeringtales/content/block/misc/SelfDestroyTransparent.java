package dev.xkmc.glimmeringtales.content.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.TransparentBlock;
import net.minecraft.world.level.block.state.BlockState;

public class SelfDestroyTransparent extends TransparentBlock {

	public SelfDestroyTransparent(Properties p_309186_) {
		super(p_309186_);
	}

	@Override
	public void tick(BlockState blockState, ServerLevel level, BlockPos pos, RandomSource randomSource) {
		level.removeBlock(pos, false);
	}

}
