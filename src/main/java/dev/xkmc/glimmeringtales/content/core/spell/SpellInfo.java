package dev.xkmc.glimmeringtales.content.core.spell;

import dev.xkmc.glimmeringtales.content.core.description.SpellTooltip;
import dev.xkmc.glimmeringtales.content.item.rune.DefaultAffinity;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public interface SpellInfo {

	static SpellInfo empty() {
		return new Empty();
	}

	static SpellInfo ofSpell(@Nullable Holder<NatureSpell> spell) {
		if (spell == null) return empty();
		return new Spell(spell, DefaultAffinity.INS, true);
	}

	static SpellInfo ofRune(@Nullable RuneBlock rune) {
		if (rune == null) return empty();
		return new Spell(rune.spell(), DefaultAffinity.INS, false);
	}

	static SpellInfo ofBlock(BlockSpell spell, ElementAffinity aff) {
		return new Block(spell, aff);
	}

	List<Component> getCastTooltip(Player player, ItemStack wand, ItemStack core);

	void runeItemDesc(Level level, List<Component> list);

	SpellCost getCost(Player player, ItemStack wand);

	record Empty() implements SpellInfo {

		@Override
		public List<Component> getCastTooltip(Player player, ItemStack wand, ItemStack core) {
			return List.of();
		}

		@Override
		public SpellCost getCost(Player player, ItemStack wand) {
			return SpellCost.ZERO;
		}

		@Override
		public void runeItemDesc(Level level, List<Component> list) {

		}

	}

	interface Present extends SpellInfo {

		IAffinityProvider affinity();

		Holder<NatureSpell> spell();

		boolean advanced();

		default SpellCost getCost(Player player, ItemStack wand) {
			return spell().value().manaCost(affinity().getFinalAffinity(spell().value().elem(), player, wand));
		}

		default List<Component> getCastTooltip(Player player, ItemStack wand, ItemStack core) {
			var ns = spell().value();
			List<Component> list = new ArrayList<>();
			var cost = getCost(player, wand);
			ns.runeDesc(player.level(), list, cost, advanced());
			SpellTooltip.get(player.level(), ns).brief(spell().unwrapKey().orElseThrow(), list);
			NatureSpell.addMana(list, player, cost);
			return list;
		}

		default void runeItemDesc(Level level, List<Component> list) {
			spell().value().runeDesc(level, list, spell().value().manaCost(1), advanced());
			var desc = SpellTooltip.get(level, spell().value());
			list.add(desc.format(spell().unwrapKey().orElseThrow()));
		}

	}

	record Spell(Holder<NatureSpell> spell, IAffinityProvider affinity, boolean advanced) implements Present {

	}

	record Block(BlockSpell block, IAffinityProvider affinity) implements Present {

		@Override
		public Holder<NatureSpell> spell() {
			return block.spell();
		}

		@Override
		public boolean advanced() {
			return false;
		}

		@Override
		public List<Component> getCastTooltip(Player player, ItemStack wand, ItemStack core) {
			var ans = Present.super.getCastTooltip(player, wand, core);
			if (block.breakBlock()) {
				ans.add(GTLang.OVERLAY_DESTROY.get().withStyle(ChatFormatting.RED));
			}
			return ans;
		}

	}

}
