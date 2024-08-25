package dev.xkmc.glimmeringtales.content.core.spell;

import net.minecraft.core.Holder;

public record BlockSpell(
		Holder<NatureSpell> spell,
		boolean breakBlock,
		int noBlockOffset,
		boolean targetLiquid,
		boolean allowSelf
) {

	public static BlockSpell of(Holder<NatureSpell> spell) {
		return new BlockSpell(spell, false, 0, false, false);
	}

	public static BlockSpell offset(Holder<NatureSpell> spell) {
		return new BlockSpell(spell, false, 1, false, false);
	}

	public static BlockSpell cost(Holder<NatureSpell> spell) {
		return new BlockSpell(spell, true, 0, false, false);
	}

	public static BlockSpell costOff(Holder<NatureSpell> spell) {
		return new BlockSpell(spell, true, 1, false, false);
	}

	public static BlockSpell liquid(Holder<NatureSpell> spell) {
		return new BlockSpell(spell, false, 0, true, false);
	}

	public static BlockSpell self(Holder<NatureSpell> spell) {
		return new BlockSpell(spell, false, 0, false, true);
	}

}
