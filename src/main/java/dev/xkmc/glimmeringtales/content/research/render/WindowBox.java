package dev.xkmc.glimmeringtales.content.research.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

public class WindowBox {

	private Screen parent;
	public int x, y, w, h, margin;

	public void setSize(Screen parent, int x, int y, int w, int h, int sh) {
		this.x = x + sh;
		this.y = y + sh;
		this.w = w - sh * 2;
		this.h = h - sh * 2;
		this.margin = sh;
		this.parent = parent;
	}

	public boolean isMouseIn(double mx, double my, int sh) {
		return mx > x - sh && mx < x + w + sh && my > y - sh && my < y + h + sh;
	}

	public void render(GuiGraphics g, int sh, int color, RenderType type) {
		if (type == RenderType.FILL) {
			g.fill(x - sh, y - sh, x + w + sh, y + h + sh, color);
		} else if (type == RenderType.MARGIN) {
			g.fill(x - sh, y - sh, x + w + sh, y, color);
			g.fill(x - sh, y + h, x + w + sh, y + h + sh, color);
			g.fill(x - sh, y, x, y + h, color);
			g.fill(x + w, y, x + w + sh, y + h, color);
		} else {
			g.fill(0, 0, parent.width, y, color);
			g.fill(0, y + h, parent.width, parent.height, color);
			g.fill(0, y, x, y + h, color);
			g.fill(x + w, y, parent.width, y + h, color);
		}
	}

	public void startClip(GuiGraphics g) {
		g.pose().pushPose();
		RenderSystem.enableDepthTest();
		g.pose().translate(0.0F, 0.0F, 950.0F);
		RenderSystem.colorMask(false, false, false, false);
		g.fill(4680, 2260, -4680, -2260, -16777216);
		RenderSystem.colorMask(true, true, true, true);
		g.pose().translate(0.0F, 0.0F, -950.0F);
		RenderSystem.depthFunc(518);
		g.fill(x, y, x + w, y + h, -16777216);
		RenderSystem.depthFunc(515);
	}

	public void endClip(GuiGraphics g) {
		RenderSystem.depthFunc(518);
		g.pose().translate(0.0F, 0.0F, -950.0F);
		RenderSystem.colorMask(false, false, false, false);
		g.fill(4680, 2260, -4680, -2260, -16777216);
		RenderSystem.colorMask(true, true, true, true);
		g.pose().translate(0.0F, 0.0F, 950.0F);
		RenderSystem.depthFunc(515);
		g.pose().popPose();
	}

	public enum RenderType {
		MARGIN, FILL, MARGIN_ALL
	}

}
