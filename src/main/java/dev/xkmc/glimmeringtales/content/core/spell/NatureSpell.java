package dev.xkmc.glimmeringtales.content.core.spell;

import com.mojang.serialization.Codec;
import dev.xkmc.glimmeringtales.content.core.description.SpellTooltip;
import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.item.rune.DefaultAffinity;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2serial.serialization.codec.CodecAdaptor;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public record NatureSpell(
		Holder<SpellAction> spell,
		SpellElement elem,
		int cost, int maxConsumeTick,
		SpellTooltipData tooltip
) {

	private static final int MIN_MANA_COST = 1, CAST_COOLDOWN = 10, BREAK_COOLDOWN = 20;
	private static final double MIN_AFFINITY = 0.2;

	public static final Codec<NatureSpell> CODEC = new CodecAdaptor<>(NatureSpell.class);

	public MutableComponent lang() {
		return Component.translatable(SpellAction.lang(spell().unwrapKey().orElseThrow().location()));
	}

	public int blockRuneDesc(Level level, List<Component> list, double affinity) {
		if (affinity < MIN_AFFINITY) affinity = MIN_AFFINITY;
		int consume = Math.max(MIN_MANA_COST, (int) Math.round(cost / affinity));
		list.add(GTLang.TOOLTIP_SPELL.get(lang().withStyle(ChatFormatting.GOLD),
				elem.coloredDesc()).withStyle(ChatFormatting.GRAY));
		Component val = Component.literal(consume + "").withStyle(ChatFormatting.BLUE);
		list.add(GTLang.TOOLTIP_COST.get(val).withStyle(ChatFormatting.GRAY));
		return consume;
	}

	public int spellRuneDesc(Level level, List<Component> list, double affinity) {
		if (affinity < MIN_AFFINITY) affinity = MIN_AFFINITY;
		int consume = Math.max(MIN_MANA_COST, (int) Math.round(cost / affinity));
		list.add(GTLang.TOOLTIP_SPELL.get(lang().withStyle(ChatFormatting.GOLD),
				elem.coloredDesc()).withStyle(ChatFormatting.GRAY));
		list.add(spell().value().castType().desc());
		list.add(spell().value().triggerType().desc());
		Component val = Component.literal(consume + "").withStyle(ChatFormatting.BLUE);
		if (spell().value().castType() != SpellCastType.INSTANT) {
			if (maxConsumeTick <= 0) {
				val = GTLang.TOOLTIP_COST_CONT.get(val).withStyle(ChatFormatting.GRAY);
			} else {
				Component max = Component.literal(consume * maxConsumeTick + "").withStyle(ChatFormatting.BLUE);
				val = GTLang.TOOLTIP_COST_CAPPED.get(val, max).withStyle(ChatFormatting.GRAY);
			}
		}
		list.add(GTLang.TOOLTIP_COST.get(val).withStyle(ChatFormatting.YELLOW));
		return consume;
	}

	public boolean consumeMana(LivingEntity user, ItemStack stack, double affinity, int useTick, boolean charging, boolean simulate) {
		if (affinity < MIN_AFFINITY) affinity = MIN_AFFINITY;
		double consume = Math.max(MIN_MANA_COST, cost / affinity);
		var cast = spell().value().castType();
		if (maxConsumeTick > 0) {
			if (cast == SpellCastType.CONTINUOUS && useTick > maxConsumeTick) consume = 0;
			if (cast == SpellCastType.CHARGE && (!charging || useTick > maxConsumeTick)) consume = 0;
		}
		if (user instanceof Player player) {
			var mana = GTRegistries.MANA.type().getOrCreate(player);
			if (simulate) return mana.getMana() >= cost;
			if (!mana.consume(player, consume)) {
				if (!user.level().isClientSide() && (
						spell.value().castType() == SpellCastType.CONTINUOUS ||
								spell.value().castType() == SpellCastType.CHARGE && charging
				)) player.getCooldowns().addCooldown(stack.getItem(), BREAK_COOLDOWN);
				return false;
			}
			if (!user.level().isClientSide() && (
					spell.value().castType() == SpellCastType.INSTANT ||
							spell.value().castType() == SpellCastType.CHARGE && !charging
			)) player.getCooldowns().addCooldown(stack.getItem(), CAST_COOLDOWN);
		}
		return true;
	}

	private void addMana(List<Component> list, Player player, int cost) {
		var mana = GTRegistries.MANA.type().getExisting(player).orElse(null);
		if (mana == null) return;
		int max = (int) player.getAttributeValue(GTRegistries.MAX_MANA);
		int val = (int) mana.getMana();
		var cval = Component.literal("" + val).withStyle(cost > val ? ChatFormatting.RED :
				val < max ? ChatFormatting.GREEN : ChatFormatting.AQUA);
		var cmax = Component.literal("" + max).withStyle(ChatFormatting.AQUA);
		list.add(GTLang.OVERLAY_MANA.get(cval, cmax).withStyle(ChatFormatting.GRAY));
	}

	public static void runeItemBlockDesc(Holder<NatureSpell> spell, Level level, List<Component> list) {
		spell.value().blockRuneDesc(level, list, 1);
		var desc = SpellTooltip.get(level, spell);
		list.add(desc.format(spell.unwrapKey().orElseThrow()));
	}

	public static void runeItemSpellDesc(Holder<NatureSpell> spell, Level level, List<Component> list) {
		spell.value().spellRuneDesc(level, list, 1);
		var desc = SpellTooltip.get(level, spell);
		list.add(desc.format(spell.unwrapKey().orElseThrow()));
	}

	public List<Component> getSpellCastTooltip(Player player, ItemStack wand) {
		List<Component> list = new ArrayList<>();
		int cost = spellRuneDesc(player.level(), list, DefaultAffinity.INS.getFinalAffinity(elem(), player, wand));
		addMana(list, player, cost);
		return list;
	}

	public List<Component> getBlockCastTooltip(Player player, ItemStack wand, IAffinityProvider aff) {
		List<Component> list = new ArrayList<>();
		int cost = blockRuneDesc(player.level(), list, aff.getFinalAffinity(elem(), player, wand));
		addMana(list, player, cost);
		return list;
	}

}
