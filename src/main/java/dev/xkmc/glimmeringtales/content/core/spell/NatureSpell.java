package dev.xkmc.glimmeringtales.content.core.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.glimmeringtales.content.item.rune.DefaultAffinity;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public record NatureSpell(
		Holder<SpellAction> spell,
		SpellElement elem,
		int cost, int maxConsumeTick
) {

	private static final int MIN_MANA_COST = 1, CAST_COOLDOWN = 10, BREAK_COOLDOWN = 20;
	private static final double MIN_AFFINITY = 0.2;

	public NatureSpell(Holder<SpellAction> spell, SpellElement elem, int cost) {
		this(spell, elem, cost, 0);
	}

	public static final Codec<NatureSpell> CODEC = RecordCodecBuilder.create(i -> i.group(
			SpellAction.HOLDER.fieldOf("spell").forGetter(NatureSpell::spell),
			GTRegistries.ELEMENT.reg().byNameCodec().fieldOf("elem").forGetter(NatureSpell::elem),
			Codec.INT.fieldOf("cost").forGetter(NatureSpell::cost),
			Codec.INT.fieldOf("maxConsumeTick").forGetter(NatureSpell::maxConsumeTick)
	).apply(i, NatureSpell::new));

	public MutableComponent lang() {
		return Component.translatable(SpellAction.lang(spell().unwrapKey().orElseThrow().location()));
	}

	public void blockRuneDesc(List<Component> list) {
		blockRuneDesc(list, 1);
	}

	public int blockRuneDesc(List<Component> list, double affinity) {
		if (affinity < MIN_AFFINITY) affinity = MIN_AFFINITY;
		int consume = Math.max(MIN_MANA_COST, (int) Math.round(cost / affinity));
		list.add(GTLang.TOOLTIP_SPELL.get(lang().withStyle(ChatFormatting.GOLD),
				elem.coloredDesc()).withStyle(ChatFormatting.GRAY));
		Component val = Component.literal(consume + "").withStyle(ChatFormatting.BLUE);
		list.add(GTLang.TOOLTIP_COST.get(val).withStyle(ChatFormatting.GRAY));
		return consume;
	}

	public void spellRuneDesc(List<Component> list) {
		spellRuneDesc(list, 1);
	}

	public int spellRuneDesc(List<Component> list, double affinity) {
		if (affinity < MIN_AFFINITY) affinity = MIN_AFFINITY;
		int consume = Math.max(MIN_MANA_COST, (int) Math.round(cost / affinity));
		list.add(GTLang.TOOLTIP_SPELL.get(lang().withStyle(ChatFormatting.GOLD),
				elem.coloredDesc()).withStyle(ChatFormatting.GRAY));
		list.add(spell().value().castType().desc());
		list.add(spell().value().triggerType().desc());
		Component val = Component.literal(consume + "").withStyle(ChatFormatting.BLUE);
		if (spell().value().castType() == SpellCastType.INSTANT) {
			list.add(GTLang.TOOLTIP_COST.get(val).withStyle(ChatFormatting.GRAY));
		} else if (maxConsumeTick <= 0) {
			list.add(GTLang.TOOLTIP_COST_CONT.get(val).withStyle(ChatFormatting.GRAY));
		} else {
			Component max = Component.literal(cost * maxConsumeTick + "").withStyle(ChatFormatting.BLUE);
			list.add(GTLang.TOOLTIP_COST_CAPPED.get(val, max).withStyle(ChatFormatting.GRAY));
		}
		return consume;
	}

	public boolean consumeMana(LivingEntity user, ItemStack stack, double affinity, int useTick, boolean charging, boolean simulate) {
		if (affinity < MIN_AFFINITY) affinity = MIN_AFFINITY;
		int consume = Math.max(MIN_MANA_COST, (int) Math.round(cost / affinity));
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

	public List<Component> getSpellCastTooltip(Player player, ItemStack wand) {
		List<Component> list = new ArrayList<>();
		int cost = spellRuneDesc(list, DefaultAffinity.INS.getFinalAffinity(elem(), player, wand));
		addMana(list, player, cost);
		return list;
	}

	private void addMana(List<Component> list, Player player, int cost) {
		var mana = GTRegistries.MANA.type().getExisting(player).orElse(null);
		if (mana == null) return;
		int max = (int) player.getAttributeValue(GTRegistries.MAX_MANA);
		int val = mana.getMana();
		var cval = Component.literal("" + val).withStyle(cost > val ? ChatFormatting.RED :
				val < max ? ChatFormatting.GREEN : ChatFormatting.AQUA);
		var cmax = Component.literal("" + max).withStyle(ChatFormatting.AQUA);
		list.add(GTLang.OVERLAY_MANA.get(cval, cmax).withStyle(ChatFormatting.GRAY));
	}

	public List<Component> getBlockCastTooltip(Player player, ItemStack wand, IAffinityProvider aff) {
		List<Component> list = new ArrayList<>();
		int cost = blockRuneDesc(list, aff.getFinalAffinity(elem(), player, wand));
		addMana(list, player, cost);
		return list;
	}
}
