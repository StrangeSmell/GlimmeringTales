package dev.xkmc.glimmeringtales.content.core.spell;

import net.minecraft.core.Holder;

public record RuneBlock(
		Holder<NatureSpell> spell,
		int noBlockOffset,
		boolean targetLiquid,
		boolean allowSelf
) {

	public static RuneBlock of(Holder<NatureSpell> spell) {
		return new RuneBlock(spell, 0, false, false);
	}

	public static RuneBlock offset(Holder<NatureSpell> spell) {
		return new RuneBlock(spell, 1, false, false);
	}

	public static RuneBlock liquid(Holder<NatureSpell> spell) {
		return new RuneBlock(spell, 0, true, false);
	}

	public static RuneBlock self(Holder<NatureSpell> spell) {
		return new RuneBlock(spell, 0, false, true);
	}

}
