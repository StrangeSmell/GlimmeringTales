package dev.xkmc.glimmeringtales.content.block.ritual;

import com.mojang.blaze3d.vertex.PoseStack;
import dev.xkmc.glimmeringtales.content.block.altar.BaseRitualBlockEntity;
import dev.xkmc.l2library.util.RenderUtils;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemStack;

public class RitualRenderer<T extends BaseRitualBlockEntity> implements BlockEntityRenderer<T> {

	public RitualRenderer(BlockEntityRendererProvider.Context context) {

	}

	@Override
	public void render(T be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		ItemStack stack = be.getItem();
		if (stack.isEmpty() || be.getLevel() == null) return;
		RenderUtils.renderItemAbove(stack, 1.5, be.getLevel(), pTick, pose, buffer, LightTexture.FULL_BRIGHT, overlay);
	}

}
