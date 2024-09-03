package dev.xkmc.glimmeringtales.content.core.spell;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public record SpellCost(double focus, double mana) {

	public static final SpellCost ZERO = new SpellCost(0, 0);

	public MutableComponent manaText() {
		return Component.literal(Math.round(mana()) + "");
	}

	public MutableComponent manaText(double factor) {
		return Component.literal(Math.round(mana() * factor) + "");
	}

	public MutableComponent focusText() {
		return Component.literal(Math.round(focus()) + "");
	}

}
