package dev.xkmc.glimmeringtales.init.data.spell;

import dev.xkmc.glimmeringtales.init.data.spell.advanced.FlameSpells;
import dev.xkmc.glimmeringtales.init.data.spell.advanced.SnowSpells;
import dev.xkmc.glimmeringtales.init.data.spell.earth.*;
import dev.xkmc.glimmeringtales.init.data.spell.flame.MagmaSpells;
import dev.xkmc.glimmeringtales.init.data.spell.life.*;
import dev.xkmc.glimmeringtales.init.data.spell.thunder.ThunderSpells;

import java.util.ArrayList;
import java.util.List;

public class NatureSpellGenRegistry {

	public static final List<NatureSpellEntry> LIST = new ArrayList<>();

	static {

		// earth
		LIST.addAll(List.of(
				DripstoneSpells.BUILDER, // 1010
				ClaySpells.BUILDER, // 1020
				AmethystSpells.BUILDER, // 1030
				SandSpells.BUILDER, // 1040
				QuartzSpells.BUILDER, // 1050
				GravelSpells.BUILDER, // 1060
				StoneSpells.BUILDER
		));

		// vine
		LIST.addAll(List.of(
				VinesSpell.BUILDER, // 1050
				HaySpell.BUILDER,
				CactusSpell.BUILDER,
				BambooSpell.BUILDER,
				FlowerSpell.BUILDER
		));

		// others
		LIST.addAll(List.of(
				MagmaSpells.BUILDER,
				ThunderSpells.BUILDER
		));

		// advanced
		LIST.addAll(List.of(
				FlameSpells.HM, FlameSpells.LB,
				SnowSpells.WS, SnowSpells.ST
		));
	}

}
