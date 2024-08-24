package dev.xkmc.glimmeringtales.init.data.spell.earth;

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
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.neoforged.neoforge.common.Tags;

import java.util.List;

public class StoneSpells {

	public static final NatureSpellBuilder BUILDER = GTRegistries.EARTH.get()
			.build(GlimmeringTales.loc("stone")).cost(20)
			.spell(ctx -> NatureSpellEntry.ofBlock(gen(ctx), GTItems.RUNE_STONE, 1040))
			.block((b, e) -> b.add(Tags.Blocks.STONES, new BlockSpell(e, false, 0)))
			.block((b, e) -> b.add(GTItems.FAKE_STONE, new BlockSpell(e, false, 0)))
			.lang("Stone Cliff");//TODO desc

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new ListLogic(List.of(
				new SetBlock(GTItems.FAKE_STONE.getDefaultState()),
				new ScheduleTick(IntVariable.of("rand(180,220)"), GTItems.FAKE_STONE.get())
		)).circular(
				DoubleVariable.of("3"), DoubleVariable.ZERO, true, null,
				BlockTestCondition.Type.REPLACEABLE.get());
	}


}
