package dev.xkmc.glimmeringtales.content.core.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record NatureSpell(
		Holder<SpellAction> spell,
		SpellElement elem,
		int cost
) {

	public static final Codec<NatureSpell> CODEC = RecordCodecBuilder.create(i -> i.group(
			SpellAction.HOLDER.fieldOf("spell").forGetter(NatureSpell::spell),
			GTRegistries.ELEMENT.reg().byNameCodec().fieldOf("elem").forGetter(NatureSpell::elem),
			Codec.INT.fieldOf("cost").forGetter(NatureSpell::cost)
	).apply(i, NatureSpell::new));

	public MutableComponent lang() {
		return Component.translatable(SpellAction.lang(spell().unwrapKey().orElseThrow().location()));
	}

	public void runeDesc(List<Component> list) {
		list.add(lang().withStyle(ChatFormatting.GRAY));
		Component val = Component.literal(cost + "").withStyle(ChatFormatting.BLUE);
		list.add(GTLang.TOOLTIP_COST.get(elem.coloredDesc(), val).withStyle(ChatFormatting.GRAY));
	}

	public void cooldown(Player player, ItemStack stack, double affinity) {
		if (affinity < 0.2) affinity = 0.2;
		int cooldown = Math.max(10, (int) Math.round(cost / affinity));
		player.getCooldowns().addCooldown(stack.getItem(), cooldown);

	}

}
