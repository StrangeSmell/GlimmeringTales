package dev.xkmc.glimmeringtales.content.engine.instance;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.glimmeringtales.content.entity.GTFallingBlockEntity;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.l2magic.content.engine.block.IBlockProcessor;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public record GTKnockBlock(
		DoubleVariable speed,
		DoubleVariable damagePerBlock,
		DoubleVariable maxDamage
) implements IBlockProcessor<GTKnockBlock> {

	public static final MapCodec<GTKnockBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			DoubleVariable.codec("speed", GTKnockBlock::speed),
			DoubleVariable.optionalCodec("damagePerBlock", GTKnockBlock::damagePerBlock),
			DoubleVariable.optionalCodec("maxDamage", GTKnockBlock::maxDamage)
	).apply(i, (a, b, c) -> new GTKnockBlock(a, b.orElse(DoubleVariable.ZERO), c.orElse(b.orElse(DoubleVariable.ZERO)))));

	@Override
	public EngineType<GTKnockBlock> type() {
		return GTEngine.KNOCK.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		var level = ctx.user().level();
		if (level.isClientSide()) return;
		var pos = BlockPos.containing(ctx.loc().pos());
		if (level.getBlockEntity(pos) != null) return;
		var state = level.getBlockState(pos);
		if (state.canBeReplaced()) return;
		level.setBlockAndUpdate(pos, state.getFluidState().createLegacyBlock());
		GTFallingBlockEntity e = fall(level, pos, state);
		e.setDeltaMovement(0, speed.eval(ctx), 0);
		e.setOwner(ctx.user().user());
		e.setHurtsEntities((float) damagePerBlock().eval(ctx), (int) maxDamage().eval(ctx));
		level.addFreshEntity(e);
	}


	public static GTFallingBlockEntity fall(Level level, BlockPos pos, BlockState state) {
		var w = BlockStateProperties.WATERLOGGED;
		return new GTFallingBlockEntity(level,
				pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
				state.hasProperty(w) ? state.setValue(w, false) : state);
	}

}
