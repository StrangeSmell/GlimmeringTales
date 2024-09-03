package dev.xkmc.glimmeringtales.content.core.spell;

import com.mojang.serialization.Codec;
import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
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

import java.util.List;

public record NatureSpell(
		Holder<SpellAction> spell,
		SpellElement elem,
		int focus, int cost, int maxConsumeTick,
		SpellTooltipData tooltip
) {

	private static final int MIN_MANA_COST = 1, CAST_COOLDOWN = 10, BREAK_COOLDOWN = 20;
	private static final double MIN_AFFINITY = 0.2;

	public static final Codec<NatureSpell> CODEC = new CodecAdaptor<>(NatureSpell.class);

	public boolean consumeMana(LivingEntity user, ItemStack stack, double affinity, int useTick, boolean charging, boolean simulate) {
		var consume = manaCost(affinity);
		var cast = spell().value().castType();
		if (maxConsumeTick > 0) {
			if (cast == SpellCastType.CONTINUOUS && useTick > maxConsumeTick) consume = SpellCost.ZERO;
			if (cast == SpellCastType.CHARGE && (!charging || useTick > maxConsumeTick)) consume = SpellCost.ZERO;
		}
		if (user instanceof Player player) {
			var mana = GTRegistries.MANA.type().getOrCreate(player);
			if (simulate) return mana.getMana() >= cost && mana.getFocus() >= focus;
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

	// ------ Tooltip ------

	private MutableComponent lang() {
		return Component.translatable(SpellAction.lang(spell().unwrapKey().orElseThrow().location()));
	}

	public SpellCost manaCost(double affinity) {
		if (affinity < MIN_AFFINITY) affinity = MIN_AFFINITY;
		return new SpellCost(focus, Math.max(MIN_MANA_COST, Math.round(cost / affinity)));
	}

	void runeDesc(Level level, List<Component> list, SpellCost consume, boolean advanced) {
		list.add(GTLang.TOOLTIP_SPELL.get(lang().withStyle(ChatFormatting.GOLD),
				elem.coloredDesc()).withStyle(ChatFormatting.GRAY));
		if (advanced) {
			list.add(spell().value().castType().desc());
			list.add(spell().value().triggerType().desc());
		}
		Component val = consume.manaText().withStyle(ChatFormatting.BLUE);
		if (spell().value().castType() != SpellCastType.INSTANT) {
			if (maxConsumeTick <= 0) {
				val = GTLang.TOOLTIP_COST_CONT.get(val).withStyle(ChatFormatting.GRAY);
			} else {
				Component max = consume.manaText(maxConsumeTick).withStyle(ChatFormatting.BLUE);
				val = GTLang.TOOLTIP_COST_CAPPED.get(val, max).withStyle(ChatFormatting.GRAY);
			}
		}
		list.add(GTLang.TOOLTIP_COST.get(val).withStyle(ChatFormatting.YELLOW));
		Component fval = consume.focusText().withStyle(ChatFormatting.BLUE);
		list.add(GTLang.TOOLTIP_FOCUS.get(fval).withStyle(ChatFormatting.YELLOW));
	}

	static void addMana(List<Component> list, Player player, SpellCost cost) {
		var mana = GTRegistries.MANA.type().getExisting(player).orElse(null);
		if (mana == null) return;
		int mmax = (int) player.getAttributeValue(GTRegistries.MAX_MANA);
		int mval = (int) mana.getMana();
		int fmax = (int) player.getAttributeValue(GTRegistries.MAX_FOCUS);
		int fval = (int) mana.getFocus();
		var cmval = Component.literal("" + mval).withStyle(cost.mana() > mval ? ChatFormatting.RED :
				mval < mmax ? ChatFormatting.GREEN : ChatFormatting.AQUA);
		var cmmax = Component.literal("" + mmax).withStyle(ChatFormatting.AQUA);
		var cfval = Component.literal("" + fval).withStyle(cost.focus() > fval ? ChatFormatting.RED :
				fval < fmax ? ChatFormatting.GREEN : ChatFormatting.AQUA);
		var cfmax = Component.literal("" + fmax).withStyle(ChatFormatting.AQUA);
		list.add(GTLang.OVERLAY_MANA.get(cmval, cmmax).withStyle(ChatFormatting.LIGHT_PURPLE));
		list.add(GTLang.OVERLAY_FOCUS.get(cfval, cfmax).withStyle(ChatFormatting.LIGHT_PURPLE));
	}

}
