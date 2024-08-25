package dev.xkmc.glimmeringtales.init.data.spell.flame;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.engine.processor.MeltBlockInstance;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTTagGen;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellEntry;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.predicate.BlockTestCondition;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.world.level.block.Blocks;

public class MagmaSpells {

	public static final NatureSpellBuilder BUILDER = GTRegistries.FLAME.get()
			.build(GlimmeringTales.loc("magma")).cost(20)
			.spell(ctx -> NatureSpellEntry.ofBlock(gen(ctx), GTItems.RUNE_MAGMA, 1040))
			.block((b, e) -> b.add(Blocks.MAGMA_BLOCK, BlockSpell.of(e)))
			.block((b, e) -> b.add(GTTagGen.FAKE_MAGMA, BlockSpell.of(e)))
			.lang("Meltdown").desc(
					"[Block] Melts stones into magma temporarily",
					"Melts stone, deep slate, and netherrack in a circular area for 5 seconds",
					SpellTooltipData.of()
			);

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new MeltBlockInstance(IntVariable.of("rand(180,220)")).circular(
				DoubleVariable.of("6"), DoubleVariable.of("2"), false, null,
				BlockTestCondition.Type.REPLACEABLE.get().move(OffsetModifier.ABOVE));
	}


}
