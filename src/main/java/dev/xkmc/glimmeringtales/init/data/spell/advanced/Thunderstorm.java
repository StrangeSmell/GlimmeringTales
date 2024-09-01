package dev.xkmc.glimmeringtales.init.data.spell.advanced;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.engine.processor.LightningInstance;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.processor.CastAtProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxCylinderSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.sound.SoundInstance;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import net.minecraft.sounds.SoundEvents;

import java.util.List;

public class Thunderstorm {

	public static final NatureSpellBuilder BUILDER = GTRegistries.THUNDER.get()
			.build(GlimmeringTales.loc("thunderstorm")).cost(100)
			.spell(ctx -> new SpellAction(gen(ctx), GTItems.THUNDERSTORM.get(), 2002,
					SpellCastType.INSTANT, SpellTriggerType.TARGET_POS)
			).lang("Thunderstorm").desc(
					"[Ranged] Create a thunderstorm striking entities",
					"Create a lightning strike on all entities in target area, inflicting %s multiple times",
					SpellTooltipData.of(GTEngine.THUNDER)
			);

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SoundInstance(
						SoundEvents.TRIDENT_THUNDER.value(),
						DoubleVariable.of("1"),
						DoubleVariable.of("1+rand(-0.1,0.1)+rand(-0.1,0.1)")
				),
				new ProcessorEngine(
						SelectionType.ENEMY_NO_FAMILY,
						new ApproxCylinderSelector(DoubleVariable.of("6"), DoubleVariable.of("3")),
						List.of(new CastAtProcessor(
								CastAtProcessor.PosType.BOTTOM, CastAtProcessor.DirType.UP,
								new LightningInstance(DoubleVariable.of("10"))
						))
				)));

	}


}
