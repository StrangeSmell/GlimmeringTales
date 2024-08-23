package dev.xkmc.glimmeringtales.content.core.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
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

import java.util.List;

public record NatureSpell(
		Holder<SpellAction> spell,
		SpellElement elem,
		int cost, int maxConsumeTick
) {

	private static final int MIN_MANA_COST = 1, COOLDOWN = 10;
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
		list.add(GTLang.TOOLTIP_SPELL.get(lang().withStyle(ChatFormatting.GOLD),
				elem.coloredDesc()).withStyle(ChatFormatting.GRAY));
		Component val = Component.literal(cost + "").withStyle(ChatFormatting.BLUE);
		list.add(GTLang.TOOLTIP_COST.get(val).withStyle(ChatFormatting.GRAY));
	}

	public void spellRuneDesc(List<Component> list) {
		list.add(GTLang.TOOLTIP_SPELL.get(lang().withStyle(ChatFormatting.GOLD),
				elem.coloredDesc()).withStyle(ChatFormatting.GRAY));
		list.add(spell().value().castType().desc());
		list.add(spell().value().triggerType().desc());
		Component val = Component.literal(cost + "").withStyle(ChatFormatting.BLUE);
		if (spell().value().castType() == SpellCastType.INSTANT) {
			list.add(GTLang.TOOLTIP_COST.get(val).withStyle(ChatFormatting.GRAY));
		} else if (maxConsumeTick <= 0) {
			list.add(GTLang.TOOLTIP_COST_CONT.get(val).withStyle(ChatFormatting.GRAY));
		} else {
			Component max = Component.literal(cost * maxConsumeTick + "").withStyle(ChatFormatting.BLUE);
			list.add(GTLang.TOOLTIP_COST_CAPPED.get(val, max).withStyle(ChatFormatting.GRAY));
		}
	}

	public boolean consumeMana(LivingEntity user, ItemStack stack, double affinity, int useTick, boolean charging) {
		if (affinity < MIN_AFFINITY) affinity = MIN_AFFINITY;
		int consume = Math.max(MIN_MANA_COST, (int) Math.round(cost / affinity));
		var cast = spell().value().castType();
		if (maxConsumeTick > 0) {
			if (cast == SpellCastType.CONTINUOUS && useTick > maxConsumeTick) consume = 0;
			if (cast == SpellCastType.CHARGE && (!charging || useTick > maxConsumeTick)) consume = 0;
		}
		if (user instanceof Player player) {
			var mana = GTRegistries.MANA.type().getOrCreate(player);
			if (!mana.consume(player, consume)) {
				return false;
			}
			if (!user.level().isClientSide() && (
					spell.value().castType() == SpellCastType.INSTANT ||
							spell.value().castType() == SpellCastType.CHARGE && !charging
			)) player.getCooldowns().addCooldown(stack.getItem(), COOLDOWN);
		}
		return true;
	}

}
