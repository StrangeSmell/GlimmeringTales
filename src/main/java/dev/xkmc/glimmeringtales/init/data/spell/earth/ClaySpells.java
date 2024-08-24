package dev.xkmc.glimmeringtales.init.data.spell.earth;

import dev.xkmc.glimmeringtales.content.core.analysis.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellEntry;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.block.ScheduleTick;
import dev.xkmc.l2magic.content.engine.block.SetBlock;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.predicate.BlockTestCondition;
import dev.xkmc.l2magic.content.engine.predicate.SurfaceBelowCondition;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.world.level.block.Blocks;

import java.util.List;

public class ClaySpells {

	public static final NatureSpellBuilder BUILDER = GTRegistries.EARTH.get()
			.build(GlimmeringTales.loc("clay")).cost(20)
			.spell(ctx -> NatureSpellEntry.ofBlock(gen(ctx), GTItems.RUNE_CLAY, 1020))
			.block((b, e) -> b.add(Blocks.CLAY, new BlockSpell(e, false, 0)))
			.block((b, e) -> b.add(GTItems.CLAY_CARPET, new BlockSpell(e, false, 0)))
			.lang("Clay Overflow").desc(
					"[Block] Form a circular carpet to trap entities",
					"Create a circular field of clay carpet lasting 5 seconds to immobilize entities",
					SpellTooltipData.of()
			);

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SetBlock(GTItems.CLAY_CARPET.getDefaultState()),
				new ScheduleTick(IntVariable.of("rand(80,120)"), GTItems.CLAY_CARPET.get())
		)).circular(
				DoubleVariable.of("4"),
				DoubleVariable.of("2"),
				false, null,
				SurfaceBelowCondition.full(),
				BlockTestCondition.Type.REPLACEABLE.get()
		);
	}

}
