package dev.xkmc.glimmeringtales.content.core.spell;

import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class SpellElement extends NamedEntry<SpellElement> {

	private final ChatFormatting color;

	public SpellElement(ChatFormatting color) {
		super(GTRegistries.ELEMENT);
		this.color = color;
	}

	public Component coloredDesc(){
		return getDesc().withStyle(color);
	}

}
