package dev.xkmc.glimmeringtales.content.item.rune;

import dev.xkmc.glimmeringtales.content.core.spell.IAffinityProvider;
import dev.xkmc.glimmeringtales.content.core.spell.SpellElement;

public record DefaultAffinity() implements IAffinityProvider {

	public static final IAffinityProvider INS = new DefaultAffinity();

	@Override
	public double get(SpellElement elem) {
		return 1;
	}

}
