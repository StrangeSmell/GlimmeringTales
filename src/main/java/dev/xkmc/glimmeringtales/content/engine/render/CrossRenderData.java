package dev.xkmc.glimmeringtales.content.engine.render;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderData;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderType;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderer;
import net.minecraft.resources.ResourceLocation;

public record CrossRenderData(
		ResourceLocation texture
) implements ProjectileRenderData<CrossRenderData> {

	public static final MapCodec<CrossRenderData> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			ResourceLocation.CODEC.fieldOf("texture").forGetter(CrossRenderData::texture)
	).apply(i, CrossRenderData::new));

	@Override
	public ProjectileRenderType<CrossRenderData> type() {
		return GTEngine.PR_CROSS.get();
	}

	@Override
	public ProjectileRenderer resolve(EngineContext ctx) {
		return new CrossTextureRenderer(texture);
	}

}
