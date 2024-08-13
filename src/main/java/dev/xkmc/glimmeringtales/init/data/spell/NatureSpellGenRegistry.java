package dev.xkmc.glimmeringtales.init.data.spell;

import dev.xkmc.glimmeringtales.init.data.spell.earth.AmethystSpells;
import dev.xkmc.glimmeringtales.init.data.spell.earth.ClaySpells;
import dev.xkmc.glimmeringtales.init.data.spell.earth.DripstoneSpells;

import java.util.List;

public class NatureSpellGenRegistry {

	public static final List<NatureSpellEntry> LIST = List.of(
			new ClaySpells(),
			new DripstoneSpells(),
			new AmethystSpells()
	);

}
