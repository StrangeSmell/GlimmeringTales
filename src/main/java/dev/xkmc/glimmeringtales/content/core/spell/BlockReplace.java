package dev.xkmc.glimmeringtales.content.core.spell;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public record BlockReplace(Block block) {

	public static BlockReplace of(Block block) {
		return new BlockReplace(block);
	}

	public BlockState state(BlockState state) {
		return block.defaultBlockState();
	}
}
