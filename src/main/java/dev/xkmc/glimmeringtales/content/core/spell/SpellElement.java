package dev.xkmc.glimmeringtales.content.core.spell;

import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2core.init.reg.registrate.NamedEntry;
import dev.xkmc.l2core.util.DataGenOnly;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
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

	public TagKey<DamageType> damgeTag() {
		return TagKey.create(Registries.DAMAGE_TYPE, getRegistryName());
	}

	@DataGenOnly
	public NatureSpellBuilder build(ResourceLocation id) {
		return new NatureSpellBuilder(id, this);
	}

	public int getColor() {
		var ans = color.getColor();
		return ans == null ? -1 : ans;
	}

}
