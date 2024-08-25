package dev.xkmc.glimmeringtales.init.data.spell.thunder;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.engine.processor.LightningInstance;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellEntry;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import net.minecraft.world.level.block.Blocks;

public class ThunderSpells {

	public static final NatureSpellBuilder BUILDER = GTRegistries.THUNDER.get()
			.build(GlimmeringTales.loc("thunder")).cost(100)
			.spell(ctx -> NatureSpellEntry.ofBlock(gen(ctx), GTItems.RUNE_THUNDER, 1040))
			.block((b, e) -> b.add(GTItems.STRUCK_LOG, BlockSpell.offset(e)))
			.block((b, e) -> b.add(Blocks.LIGHTNING_ROD, BlockSpell.offset(e)))
			.lang("Thunder").desc(
					"[Block] Create a lightning strike",
					"Create a lightning strike in target position, inflicting %s multiple times",
					SpellTooltipData.of(GTEngine.THUNDER)
			);

	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		return new LightningInstance(DoubleVariable.of("10"));
	}


}
