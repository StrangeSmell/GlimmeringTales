package dev.xkmc.glimmeringtales.content.engine.particle;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.particle.engine.ParticleRenderData;
import dev.xkmc.l2magic.content.particle.engine.ParticleRenderType;
import dev.xkmc.l2magic.content.particle.render.ParticleRenderer;

public record FarParticleRenderData(
		ParticleRenderData<?> inner,
		DoubleVariable hiddenRange
) implements ParticleRenderData<FarParticleRenderData> {


	public static final MapCodec<FarParticleRenderData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			ParticleRenderData.CODEC.fieldOf("inner").forGetter(FarParticleRenderData::inner),
			DoubleVariable.codec("hiddenRange", FarParticleRenderData::hiddenRange)
	).apply(i, FarParticleRenderData::new));

	@Override
	public ParticleRenderType<FarParticleRenderData> type() {
		return GTEngine.PR_FAR.get();
	}

	@Override
	public ParticleRenderer resolve(EngineContext ctx) {
		return new FarParticleRenderer(inner.resolve(ctx), ctx.user().user(), hiddenRange.eval(ctx));
	}
}
