package dev.xkmc.glimmeringtales.content.engine.filter;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityFilter;
import dev.xkmc.l2magic.content.engine.core.FilterType;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.world.entity.LivingEntity;

public record InvulFrameFilter(
		IntVariable frame
) implements EntityFilter<InvulFrameFilter> {

	public static final MapCodec<InvulFrameFilter> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			IntVariable.codec("frame", InvulFrameFilter::frame)
	).apply(i, InvulFrameFilter::new));

	@Override
	public FilterType<InvulFrameFilter> type() {
		return GTEngine.INVUL.get();
	}

	@Override
	public boolean test(LivingEntity le, EngineContext ctx) {
		return le.hurtTime <= frame.eval(ctx);
	}

}
