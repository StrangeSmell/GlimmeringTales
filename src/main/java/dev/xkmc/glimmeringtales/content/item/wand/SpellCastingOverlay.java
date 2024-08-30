package dev.xkmc.glimmeringtales.content.item.wand;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;

public class SpellCastingOverlay implements LayeredDraw.Layer {

	private static final ResourceLocation FRAME = GlimmeringTales.loc("mana_frame");
	private static final ResourceLocation BAR = GlimmeringTales.loc("mana_bar");
	private static final int W = 48, FW = W + 6, H = 2, FH = H + 6;

	@Override
	public void render(GuiGraphics g, DeltaTracker delta) {
		float pTick = delta.getGameTimeDeltaPartialTick(true);
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var mana = GTRegistries.MANA.type().getExisting(player).orElse(null);
		if (mana == null) return;
		int max = (int) player.getAttributeValue(GTRegistries.MAX_MANA);
		int val = (int) mana.getMana();
		if (player.getItemInHand(InteractionHand.MAIN_HAND).is(GTItems.WAND) ||
				player.getItemInHand(InteractionHand.OFF_HAND).is(GTItems.WAND) ||
				val < max) {
			int w = g.guiWidth();
			int h = g.guiHeight();
			g.blitSprite(FRAME, (w - FW) / 2, (int) (h * 0.625) - FH / 2, FW, FH);
			TextureAtlasSprite bar = Minecraft.getInstance().getGuiSprites().getSprite(BAR);
			g.blitSprite(BAR, W, H, 0, 0, (w - W) / 2, (int) (h * 0.625) - H / 2, 0, W * val / max, H);


		}
	}

}
