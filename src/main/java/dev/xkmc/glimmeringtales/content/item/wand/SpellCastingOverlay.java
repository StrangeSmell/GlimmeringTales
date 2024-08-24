package dev.xkmc.glimmeringtales.content.item.wand;

import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;

public class SpellCastingOverlay implements LayeredDraw.Layer {

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
			Font font = Minecraft.getInstance().font;
			int w = g.guiWidth();
			int h = g.guiHeight();
			var cval = Component.literal("" + val).withStyle(val < max * 0.25 ? ChatFormatting.RED :
					val < max * 0.75 ? ChatFormatting.YELLOW : ChatFormatting.AQUA);
			var cmax = Component.literal("" + max).withStyle(ChatFormatting.AQUA);
			g.drawCenteredString(font, GTLang.OVERLAY_MANA.get(cval, cmax), w / 2, (int) (h * 0.625), -1);
		}
	}

}
