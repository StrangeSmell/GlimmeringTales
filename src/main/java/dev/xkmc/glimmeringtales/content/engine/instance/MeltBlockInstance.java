package dev.xkmc.glimmeringtales.content.engine.instance;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.block.BlockUtils;
import dev.xkmc.l2magic.content.engine.block.IBlockProcessor;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

public record MeltBlockInstance(
		IntVariable tick
) implements IBlockProcessor<MeltBlockInstance> {

	public static final MapCodec<MeltBlockInstance> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			IntVariable.codec("tick", e -> e.tick)

	).apply(i, MeltBlockInstance::new));

	@Override
	public EngineType<MeltBlockInstance> type() {
		return GTEngine.MELT_BLOCK.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		if (!(ctx.user().level() instanceof ServerLevel)) return;
		var level = ctx.user().level();
		var pos = BlockPos.containing(ctx.loc().pos());
		var state = level.getBlockState(pos);
		var e = GTRegistries.MELT.get(level.registryAccess(), state.getBlockHolder());
		if (e == null) return;
		var next = e.state(state);
		BlockUtils.set(ctx, next);
		level.scheduleTick(pos, next.getBlock(), tick.eval(ctx));
	}

}