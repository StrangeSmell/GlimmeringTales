package dev.xkmc.glimmeringtales.content.engine.render;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.l2magic.content.entity.core.LMProjectile;
import dev.xkmc.l2magic.content.entity.renderer.LMProjectileRenderer;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.RenderTypeHelper;
import net.neoforged.neoforge.client.model.data.ModelData;

public record FakeBlockRenderer(BlockState state, float scale) implements ProjectileRenderer {

	@Override
	public ResourceLocation getTexture() {
		return TextureAtlas.LOCATION_BLOCKS;
	}

	@Override
	public void render(LMProjectile entity, LMProjectileRenderer<?> r, float v, PoseStack pose, MultiBufferSource buffer, int packedLight) {
		if (state.getRenderShape() != RenderShape.MODEL) return;
		var dispatcher = Minecraft.getInstance().getBlockRenderer();
		Level level = entity.level();
		pose.pushPose();
		BlockPos pos = BlockPos.containing(entity.getX(), entity.getBoundingBox().maxY, entity.getZ());
		pose.scale(scale, scale, scale);
		pose.translate(-0.5, 0.0, -0.5);
		var model = dispatcher.getBlockModel(state);
		int seed = entity.getId();
		for (var rt : model.getRenderTypes(state, RandomSource.create(seed), ModelData.EMPTY))
			dispatcher.getModelRenderer().tesselateBlock(
					level, dispatcher.getBlockModel(state), state, pos, pose,
					buffer.getBuffer(RenderTypeHelper.getMovingBlockRenderType(rt)), false,
					RandomSource.create(), seed, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, rt
			);
		pose.popPose();

	}

}
