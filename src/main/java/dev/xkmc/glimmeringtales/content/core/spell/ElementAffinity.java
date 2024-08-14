package dev.xkmc.glimmeringtales.content.core.spell;

import java.util.LinkedHashMap;
import java.util.Map;

public record ElementAffinity(LinkedHashMap<SpellElement, Double> affinity) implements IAffinityProvider {

	public static ElementAffinity of(Map<SpellElement, Double> map) {
		return new ElementAffinity(new LinkedHashMap<>(map));
	}

	@Deprecated
	public ElementAffinity {

	}

	@Override
	public double get(SpellElement elem) {
		return affinity.getOrDefault(elem, 1d);
	}

}
