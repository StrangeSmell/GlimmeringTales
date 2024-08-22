package dev.xkmc.glimmeringtales.content.block.misc;

import dev.xkmc.l2modularblock.mult.StepOnBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class MagmaImpl implements StepOnBlockMethod {

	@Override
	public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
		if (!entity.isSteppingCarefully() && entity instanceof LivingEntity) {
			entity.hurt(level.damageSources().hotFloor(), 1.0F);
		}
	}

}
