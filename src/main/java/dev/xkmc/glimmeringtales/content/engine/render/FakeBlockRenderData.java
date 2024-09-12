package dev.xkmc.glimmeringtales.content.engine.render;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderData;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderType;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderer;
import net.minecraft.world.level.block.state.BlockState;

public record FakeBlockRenderData(
		BlockState model, DoubleVariable scale
) implements ProjectileRenderData<FakeBlockRenderData> {

	public static final MapCodec<FakeBlockRenderData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			BlockState.CODEC.fieldOf("model").forGetter(FakeBlockRenderData::model),
			DoubleVariable.codec("initial", FakeBlockRenderData::scale)
	).apply(i, FakeBlockRenderData::new));

	@Override
	public ProjectileRenderType<FakeBlockRenderData> type() {
		return GTEngine.PR_BLOCK.get();
	}

	@Override
	public ProjectileRenderer resolve(EngineContext ctx) {
		return new FakeBlockRenderer(model, (float) scale.eval(ctx));
	}

}
