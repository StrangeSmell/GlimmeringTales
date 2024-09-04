package dev.xkmc.glimmeringtales.content.engine.render;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderData;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderType;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderer;
import net.minecraft.resources.ResourceLocation;

public record AnimatedRenderData(
		ResourceLocation texture, double initial, double rate
) implements ProjectileRenderData<AnimatedRenderData> {

	public static final MapCodec<AnimatedRenderData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			ResourceLocation.CODEC.fieldOf("texture").forGetter(AnimatedRenderData::texture),
			Codec.DOUBLE.fieldOf("initial").forGetter(AnimatedRenderData::initial),
			Codec.DOUBLE.fieldOf("rate").forGetter(AnimatedRenderData::rate)
	).apply(i, AnimatedRenderData::new));

	@Override
	public ProjectileRenderType<AnimatedRenderData> type() {
		return GTEngine.PR_ANIM.get();
	}

	@Override
	public ProjectileRenderer resolve(EngineContext ctx) {
		return new AnimatedTextureRenderer(texture, initial, rate);
	}

}
