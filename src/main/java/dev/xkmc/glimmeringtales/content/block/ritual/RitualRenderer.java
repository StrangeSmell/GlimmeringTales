package dev.xkmc.glimmeringtales.content.block.ritual;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.xkmc.glimmeringtales.content.block.altar.BaseRitualBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class RitualRenderer<T extends BaseRitualBlockEntity> implements BlockEntityRenderer<T> {

	public RitualRenderer(BlockEntityRendererProvider.Context context) {

	}

	@Override
	public void render(T be, float pTick, PoseStack pose, MultiBufferSource buffer, int light, int overlay) {
		light = LightTexture.FULL_BRIGHT;
		ItemStack stack = be.getItem();
		var level = be.getLevel();
		if (stack.isEmpty() || level == null) return;
		float time = Math.floorMod(level.getGameTime(), 80L) + pTick;
		pose.pushPose();
		double offset = (Math.sin(time * 2 * Math.PI / 40.0) - 3) / 16;
		pose.translate(0.5, 1.5 + offset, 0.5);
		pose.mulPose(Axis.YP.rotationDegrees(time * 4.5f));
		float scale = be.getStackScale(pTick);
		pose.scale(scale, scale, scale);
		Minecraft.getInstance().getItemRenderer().renderStatic(stack, ItemDisplayContext.GROUND, light,
				overlay, pose, buffer, level, 0);
		pose.popPose();
	}

}
