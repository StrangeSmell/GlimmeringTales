package dev.xkmc.glimmeringtales.content.item.wand;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.xkmc.glimmeringtales.content.core.spell.SpellCost;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

public class SpellCastingOverlay implements LayeredDraw.Layer {

	private static final ResourceLocation FRAME = GlimmeringTales.loc("mana_frame");
	private static final ResourceLocation FOCUS = GlimmeringTales.loc("mana_bar");
	private static final ResourceLocation BAR = GlimmeringTales.loc("focus_bar");
	private static final ResourceLocation RED = GlimmeringTales.loc("insufficient");
	private static final int W = 64, FW = 76, FH = 8;

	@Override
	public void render(GuiGraphics g, DeltaTracker delta) {
		float pTick = delta.getGameTimeDeltaPartialTick(true);
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var mana = GTRegistries.MANA.type().getExisting(player).orElse(null);
		if (mana == null) return;
		double maxMana = player.getAttributeValue(GTRegistries.MAX_MANA);
		double valMana = mana.getMana();
		double maxFocus = player.getAttributeValue(GTRegistries.MAX_FOCUS);
		double valFocus = mana.getFocus();
		ItemStack wand = player.getItemInHand(InteractionHand.MAIN_HAND);
		if (!wand.is(GTItems.WAND)) wand = player.getItemInHand(InteractionHand.OFF_HAND);
		if (wand.is(GTItems.WAND)) {
			int w = g.guiWidth();
			int h = g.guiHeight();
			int x0 = w / 2, y0 = (int) (h * 0.625) - FH / 2;
			g.blitSprite(FRAME, x0 - FW / 2, y0, FW, FH);
			var core = RuneWandItem.getCore(wand);
			var cost = SpellCost.ZERO;
			if (core.getItem() instanceof IWandCoreItem item) {
				cost = item.getSpellInfo(player).getCost(player, wand);
			}
			blitSprite(g, cost.focus() > valFocus ? RED : FOCUS, x0 - W / 2, y0 + 2, (float) (valFocus / maxFocus), 1);
			blitSprite(g, cost.mana() > valMana ? RED : BAR, x0 - W / 2, y0 + 4, (float) (valMana / maxMana), 2);
		}
	}

	private static void blitSprite(
			GuiGraphics g, ResourceLocation id, int x, int y, float w, float h
	) {
		TextureAtlasSprite sp = Minecraft.getInstance().getGuiSprites().getSprite(id);
		innerBlit(g, sp.atlasLocation(),
				x, x + w * W, y, y + h,
				sp.getU(0), sp.getU(w),
				sp.getV(0), sp.getV(1)
		);
	}

	private static void innerBlit(
			GuiGraphics g, ResourceLocation tex,
			float x1, float x2, float y1, float y2,
			float u0, float u1, float v0, float v1
	) {
		RenderSystem.setShaderTexture(0, tex);
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		Matrix4f m = g.pose().last().pose();
		BufferBuilder b = Tesselator.getInstance().begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
		b.addVertex(m, x1, y1, 0).setUv(u0, v0);
		b.addVertex(m, x1, y2, 0).setUv(u0, v1);
		b.addVertex(m, x2, y2, 0).setUv(u1, v1);
		b.addVertex(m, x2, y1, 0).setUv(u1, v0);
		BufferUploader.drawWithShader(b.buildOrThrow());
	}

}
