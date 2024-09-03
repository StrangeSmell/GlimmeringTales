package dev.xkmc.glimmeringtales.content.core.description;

import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2magic.content.engine.extension.ExtensionHolder;
import dev.xkmc.l2magic.content.engine.extension.IExtended;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public record SpellTooltipData(ArrayList<Entry> list) {

	public static SpellTooltipData damage() {
		return of(EngineRegistry.DAMAGE);
	}

	public static SpellTooltipData damageAndFalling() {
		return of(EngineRegistry.DAMAGE, EngineRegistry.KNOCK_BLOCK);
	}

	public static SpellTooltipData damageAndEffect() {
		return of(EngineRegistry.DAMAGE, EngineRegistry.EFFECT);
	}

	public static SpellTooltipData of() {
		return new SpellTooltipData(new ArrayList<>());
	}

	@SafeVarargs
	public static SpellTooltipData of(Val<? extends ExtensionHolder<?>>... entries) {
		return new SpellTooltipData(new ArrayList<>(Stream.of(entries).map(Entry::of).toList()));
	}

	public static String brief(ResourceLocation rl) {
		return "nature_spell." + rl.toLanguageKey() + ".brief";
	}

	public static String detail(ResourceLocation rl) {
		return "nature_spell." + rl.toLanguageKey() + ".detail";
	}

	public Component format(ResourceLocation rl, SpellTooltip data) {
		SpellDataStack stack = data.asStack();
		Object[] objs = new Object[list.size()];
		for (int i = 0; i < list.size(); i++) {
			objs[i] = format(list.get(i).type(), stack);
		}
		return Component.translatable(detail(rl), objs).withStyle(ChatFormatting.GRAY);
	}

	public void brief(ResourceLocation rl, List<Component> ans, SpellTooltip data) {
		SpellDataStack stack = data.asStack();
		ans.add(Component.empty());
		ans.add(Component.translatable(brief(rl)).withStyle(ChatFormatting.GRAY));
		for (var e : list()) {
			ans.add(Component.literal("=> ").append(format(e.type(), stack)).withStyle(ChatFormatting.GRAY));
		}
	}

	public static <T extends IExtended<T>> Component format(ExtensionHolder<T> type, SpellDataStack data) {
		var ext = type.get(Component.class);
		return ext == null ? Component.literal("???") : ext.process(data.get(type));
	}

	public record Entry(ResourceLocation reg, ResourceLocation id) {

		public static Entry of(Val<? extends ExtensionHolder<?>> val) {
			return new Entry(val.key().registry(), val.key().location());
		}

		public ExtensionHolder<?> type() {
			return Wrappers.cast(BuiltInRegistries.REGISTRY.get(reg).get(id));
		}
	}

}
