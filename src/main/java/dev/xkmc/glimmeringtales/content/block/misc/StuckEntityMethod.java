package dev.xkmc.glimmeringtales.content.block.misc;

import dev.xkmc.l2modularblock.one.EntityInsideBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public record StuckEntityMethod(Vec3 vec3) implements EntityInsideBlockMethod {

	@Override
	public void entityInside(BlockState state, Level level, BlockPos pos, Entity e) {
		e.makeStuckInBlock(state, vec3);
	}

}
