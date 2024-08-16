package dev.xkmc.glimmeringtales.content.block.ritual;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;

public class RitualRenderer<T extends BaseRitualBlockEntity> implements BlockEntityRenderer<T> {

	@Override
	public void render(T blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {

	}

}
