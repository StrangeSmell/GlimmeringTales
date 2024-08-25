package dev.xkmc.glimmeringtales.init.data.spell.life;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTTagGen;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellEntry;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.iterator.SphereRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.particle.BlockParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.PushProcessor;
import dev.xkmc.l2magic.content.engine.selector.BoxSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class VinesSpell {

	public static final NatureSpellBuilder BUILDER = GTRegistries.LIFE.get()
			.build(GlimmeringTales.loc("vine")).cost(40)
			.damageCustom(msg -> new DamageType(msg, 1f),
					"%s is choked vines",
					"%s is choked by %s with vines",
					DamageTypeTags.PANIC_ENVIRONMENTAL_CAUSES)
			.spell(ctx -> NatureSpellEntry.ofBlock(vine(ctx, 4, 0.3), Items.VINE, 1030))
			.block((b, e) -> b.add(GTTagGen.VINE, new BlockSpell(e, true, 1)))
			.lang("Vine").desc(
					"[Block] Pull enemies to center",
					"Pull surrounding enemies toward target position, dealing %s",
					SpellTooltipData.damage()
			);

	private static ConfiguredEngine<?> vine(NatureSpellBuilder ctx, double radius, double step) {
		return new ListLogic(List.of(
				new ProcessorEngine(
						SelectionType.ENEMY,
						new BoxSelector(
								DoubleVariable.of("16"),
								DoubleVariable.of("16"),
								true
						),
						List.of(
								new DamageProcessor(
										ctx.damage(),
										DoubleVariable.of("1"),
										true, false
								),
								new PushProcessor(
										DoubleVariable.of("-.5"),
										DoubleVariable.ZERO,
										DoubleVariable.ZERO,
										PushProcessor.Type.TO_CENTER
								)
						)
				),
				new LoopIterator(
						IntVariable.of("1"),
						new SphereRandomIterator(
								DoubleVariable.of("12"),
								IntVariable.of("300"),
								new BlockParticleInstance(
										Blocks.VINE,
										DoubleVariable.of("-.8"),
										DoubleVariable.of("1"),
										IntVariable.of("40"),
										true
								),
								null
						)
						,
						null

				)

		));
	}


}