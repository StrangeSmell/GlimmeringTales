package dev.xkmc.glimmeringtales.init.data.spell.earth;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellEntry;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.logic.RandomVariableLogic;
import dev.xkmc.l2magic.content.engine.modifier.*;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.content.engine.processor.PushProcessor;
import dev.xkmc.l2magic.content.engine.processor.SetDeltaProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxCylinderSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.motion.MovePosMotion;
import dev.xkmc.l2magic.content.particle.engine.CustomParticleInstance;
import dev.xkmc.l2magic.content.particle.engine.DustParticleData;
import dev.xkmc.l2magic.content.particle.engine.RenderTypePreset;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.List;

public class SandSpells extends NatureSpellEntry {

	public static final ResourceLocation ID = GlimmeringTales.loc("sand");
	public static final DataGenCachedHolder<SpellAction> SPELL = spell(ID);
	public static final DataGenCachedHolder<NatureSpell> NATURE = nature(ID);

	@Override
	public void regNature(BootstrapContext<NatureSpell> ctx) {
		NATURE.gen(ctx, new NatureSpell(SPELL, GTRegistries.EARTH.get(), 20));
	}

	@Override
	public void regBlock(DataMapProvider.Builder<BlockSpell, Block> builder) {
		builder.add(BlockTags.SAND, new BlockSpell(NATURE), false);
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		new SpellAction(
				gen(new DataGenContext(ctx)),
				GTItems.RUNE_SAND.asItem(),
				1040,
				SpellCastType.INSTANT,
				SpellTriggerType.TARGET_POS
		).verifyOnBuild(ctx, SPELL);
	}

	@Override
	public void genLang(RegistrateLangProvider ctx) {
		ctx.add(SpellAction.lang(ID), "Sandstorm");
	}

	private static ConfiguredEngine<?> gen(DataGenContext ctx) {
		double vsp = 0.5;
		int life = 20;
		double rate = Math.tan(10 * Mth.DEG_TO_RAD);
		double w = vsp * 180 / Math.PI / 2;
		double ir = 0.7;
		String radius = ir + "+TickCount*" + vsp * rate;
		String angle = w / (vsp * rate) + "*(log(" + radius + ")+log(" + ir + "))";

		var damage = new ProcessorEngine(SelectionType.ENEMY,
				new ApproxCylinderSelector(
						DoubleVariable.of("1"),
						DoubleVariable.of("6")
				),
				List.of(
						new DamageProcessor(
								ctx.damage(DamageTypes.ARROW),
								DoubleVariable.of("4"),
								true, true
						),
						SetDeltaProcessor.ZERO,
						new PushProcessor(
								DoubleVariable.of("-0.05"),
								DoubleVariable.ZERO,
								DoubleVariable.ZERO,
								PushProcessor.Type.HORIZONTAL
						),
						new EffectProcessor(
								MobEffects.MOVEMENT_SLOWDOWN,
								IntVariable.of("100"),
								IntVariable.of("2"),
								false, false
						)
				));

		var particle = new CustomParticleInstance(
				DoubleVariable.of("0"),
				DoubleVariable.of("0.15"),
				IntVariable.of("" + life),
				true,
				new MovePosMotion(List.of(
						ForwardOffsetModifier.of("-" + ir),
						RotationModifier.of(angle),
						ForwardOffsetModifier.of(radius),
						new Normal2DirModifier(),
						ForwardOffsetModifier.of("TickCount*" + vsp)
				)),
				new DustParticleData(
						RenderTypePreset.NORMAL,
						ColorVariable.Static.of(14406560)
				)
		).move(NormalOffsetModifier.of("rand(" + (-vsp) + "," + vsp + ")"));

		var tick = new ListLogic(List.of(damage,
				new DelayedIterator(
						IntVariable.of("10"),
						IntVariable.of("1"),
						new RandomVariableLogic("r", 1,
								new RingRandomIterator(
										DoubleVariable.of(ir + ""),
										DoubleVariable.of(ir + ""),
										DoubleVariable.of("-180"),
										DoubleVariable.of("180"),
										IntVariable.of("3"),
										particle, null
								).move(new Dir2NormalModifier())
						), null
				)
		));

		return new DelayedIterator(IntVariable.of("80"), IntVariable.of("1"), tick, null)
				.move(OffsetModifier.ABOVE);
	}


}
