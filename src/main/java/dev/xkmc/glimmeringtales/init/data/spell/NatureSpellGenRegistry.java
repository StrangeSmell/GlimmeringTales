package dev.xkmc.glimmeringtales.init.data.spell;

import dev.xkmc.glimmeringtales.init.data.spell.earth.*;
import dev.xkmc.glimmeringtales.init.data.spell.life.*;

import java.util.List;

public class NatureSpellGenRegistry {

	public static final List<NatureSpellEntry> LIST = List.of(
			new DripstoneSpells(), // 1010
			new ClaySpells(), // 1020
			new AmethystSpells(), // 1030
			new SandSpells(), // 1040
			new QuartzSpells(), // 1050
			new VinesSpell(), // 1050
			new HaySpell(),
			new CactusSpell(),
			new BambooSpell(),
			new FlowerSpell(),
			new GravelSpells() // 1060
	);

}
