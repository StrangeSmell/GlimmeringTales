package dev.xkmc.glimmeringtales.init.reg;

import dev.xkmc.glimmeringtales.content.engine.processor.*;
import dev.xkmc.glimmeringtales.content.engine.render.CrossRenderData;
import dev.xkmc.glimmeringtales.content.engine.render.VerticalRenderData;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.entity.renderer.ProjectileRenderType;
import dev.xkmc.l2magic.init.registrate.EngineReg;

public class GTEngine {

	private static final EngineReg REG = new EngineReg(GlimmeringTales.REGISTRATE);


	public static final Val<ProcessorType<StackingEffectProcessor>> EP_STACK = REG.reg("stacking", () -> StackingEffectProcessor.CODEC);
	public static final Val<EngineType<EffectCloudInstance>> EFFECT_CLOUD = REG.reg("effect_cloud", () -> EffectCloudInstance.CODEC);
	public static final Val<EngineType<MeltBlockInstance>> MELT_BLOCK = REG.reg("melt_block", () -> MeltBlockInstance.CODEC);
	public static final Val<EngineType<LightningInstance>> THUNDER = REG.reg("thunder", () -> LightningInstance.CODEC);
	public static final Val<ProcessorType<ProcreationProcessor>> PROCREATION = REG.reg("procreation", () -> ProcreationProcessor.CODEC);
	public static final Val<ProjectileRenderType<VerticalRenderData>> PR_VERTICAL = REG.reg("vertical", () -> VerticalRenderData.CODEC);
	public static final Val<ProjectileRenderType<CrossRenderData>> PR_CROSS = REG.reg("cross", () -> CrossRenderData.CODEC);

	public static void register() {

	}

}
