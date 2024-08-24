package dev.xkmc.glimmeringtales.content.core.analysis;

import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public record SpellTooltipData(ArrayList<Entry> list) {

	public static SpellTooltipData of(Entry... entries) {
		return new SpellTooltipData(new ArrayList<>(List.of(entries)));
	}

	public static String brief(ResourceLocation rl) {
		return "nature_spell." + rl.toLanguageKey() + ".brief";
	}

	public static String detail(ResourceLocation rl) {
		return "nature_spell." + rl.toLanguageKey() + ".detail";
	}

	public Component format(SpellTooltip data) {
		return Component.empty();
	}

	public record Entry(ProcessorType<?> type) {

	}

}
