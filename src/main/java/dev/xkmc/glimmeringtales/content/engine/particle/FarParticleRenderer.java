package dev.xkmc.glimmeringtales.content.engine.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.xkmc.l2magic.content.particle.core.LMGenericParticle;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import dev.xkmc.l2magic.content.particle.render.ParticleRenderer;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.LivingEntity;

public record FarParticleRenderer(
		ParticleRenderer inner, LivingEntity user, double minDist
) implements ParticleRenderer {

	@Override
	public void onParticleInit(LMGenericParticle e) {
		inner.onParticleInit(e);
	}

	@Override
	public void onPostTick(LMGenericParticle e) {
		inner.onPostTick(e);
	}

	@Override
	public RenderTypePreset renderType() {
		return inner.renderType();
	}

	@Override
	public boolean specialRender(LMGenericParticle e, VertexConsumer vc, Camera camera, float v) {
		var dist = camera.getEntity().getEyePosition().distanceTo(e.getPos());
		if (dist < minDist && camera.getEntity() == user) return true;
		return inner.specialRender(e, vc, camera, v);
	}
}
