package dev.xkmc.glimmeringtales.content.research.core;

import dev.xkmc.glimmeringtales.init.data.GTLang;

import java.util.Locale;

public enum ResearchState implements GTLang.EnumLang {
	LOCKED, UNLOCKED, COMPLETED;

	public int getIndex() {
		return ordinal();
	}

	public String toString() {
		return name().toLowerCase();
	}

	@Override
	public String defText() {
		return name().toLowerCase(Locale.ROOT);
	}

}
