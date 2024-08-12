package dev.xkmc.glimmeringtales.init.reg;

import dev.xkmc.glimmeringtales.content.engine.render.VerticalRenderData;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderType;
import dev.xkmc.l2magic.init.registrate.EngineReg;

public class GTEngine {

	private static final EngineReg REG = new EngineReg(GlimmeringTales.REGISTRATE);

	public static final Val<ProjectileRenderType<VerticalRenderData>> PR_VERTICAL = REG.reg("vertical", () -> VerticalRenderData.CODEC);

	public static void register() {

	}

}
