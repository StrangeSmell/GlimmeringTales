package dev.xkmc.glimmeringtales.init.data.spell.flame;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.data.spell.advanced.UnrealHelper;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.SphereRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.PredicateLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.modifier.ToCurrentCasterDirModifier;
import dev.xkmc.l2magic.content.engine.modifier.ToCurrentCasterPosModifier;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.KnockBackProcessor;
import dev.xkmc.l2magic.content.engine.processor.PushProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxCylinderSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.selector.SelfSelector;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;

import java.util.List;

public class FlameDash {

	public static final NatureSpellBuilder BUILDER = GTRegistries.FLAME
			.build(GlimmeringTales.loc("flame_dash")).focusAndCost(2, 5)
			.damageFire()
			.spell(e -> new SpellAction(flameCharge(e), GTItems.FLAME_DASH.get(), 2010,
					SpellCastType.CHARGE, SpellTriggerType.FACING_FRONT))
			.lang("Flame Dash").desc(
					"[Charge] Charge and launch yourself as a rolling flame ball",
					"Charge, and then enter flame dashing mode for the same duration as you charged, move forward and knock off enemies, dealing %s",
					SpellTooltipData.damage()
			);

	private static ConfiguredEngine<?> flameCharge(NatureSpellBuilder ctx) {
		return new PredicateLogic(BooleanVariable.of("Power==0"),
				new SphereRandomIterator(
						DoubleVariable.of("2"),
						IntVariable.of("50"),
						new DustParticleInstance(
								ColorVariable.Static.of(0xFF0000),
								DoubleVariable.of("1"),
								DoubleVariable.of("-.2"),
								IntVariable.of("10")
						),
						null
				),
				new DelayedIterator(
						IntVariable.of("min(2*TickUsing,80)"),
						IntVariable.of("1"),
						new ListLogic(List.of(
								new ProcessorEngine(  // Push
										SelectionType.ALL,
										new SelfSelector(),
										List.of(new PushProcessor(
												DoubleVariable.of(".2"),
												DoubleVariable.ZERO,
												DoubleVariable.ZERO,
												PushProcessor.Type.UNIFORM
										))
								),
								new ProcessorEngine(  // Damage
										SelectionType.ENEMY,
										new ApproxCylinderSelector(
												DoubleVariable.of("2"),
												DoubleVariable.of("2")
										),
										List.of(
												new DamageProcessor(ctx.damage(), DoubleVariable.of("4"), true, true),
												new KnockBackProcessor(
														DoubleVariable.of("0.2"),
														DoubleVariable.ZERO,
														DoubleVariable.ZERO
												)
										)
								),
								UnrealHelper.cone(1.5, 3, 30, 30,
										new DustParticleInstance(
												ColorVariable.Static.of(0xFF0000),
												DoubleVariable.of(".5"),
												DoubleVariable.of("1"),
												IntVariable.of("1")
										).move(new ToCurrentCasterDirModifier())
								).move(new ForwardOffsetModifier(DoubleVariable.of("-1")))
						)).move(
								new ToCurrentCasterPosModifier(),
								SetDirectionModifier.UP,
								ForwardOffsetModifier.of("1"),
								new ToCurrentCasterDirModifier()
						), null
				)
		);
	}

}
