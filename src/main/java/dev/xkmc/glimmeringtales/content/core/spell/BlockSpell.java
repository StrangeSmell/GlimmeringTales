package dev.xkmc.glimmeringtales.content.core.spell;

import net.minecraft.core.Holder;

public record BlockSpell(
		Holder<NatureSpell> spell,
		boolean breakBlock
) {

	public static BlockSpell of(Holder<NatureSpell> spell) {
		return new BlockSpell(spell, false);
	}


	public static BlockSpell cost(Holder<NatureSpell> spell) {
		return new BlockSpell(spell, true);
	}

}
