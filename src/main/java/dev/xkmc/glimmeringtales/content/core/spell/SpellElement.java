package dev.xkmc.glimmeringtales.content.core.spell;

import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;

public class SpellElement extends NamedEntry<SpellElement> {

	private final ChatFormatting color;
	private final Holder<Attribute> affinity;

	public SpellElement(ChatFormatting color, Holder<Attribute> affinity) {
		super(GTRegistries.ELEMENT);
		this.color = color;
		this.affinity = affinity;
	}

	public Holder<Attribute> getAffinity() {
		return affinity;
	}

	public Component coloredDesc() {
		return getDesc().withStyle(color);
	}

}
