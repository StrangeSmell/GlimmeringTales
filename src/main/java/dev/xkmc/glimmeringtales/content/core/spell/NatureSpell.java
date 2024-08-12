package dev.xkmc.glimmeringtales.content.core.spell;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import net.minecraft.core.Holder;

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



}
