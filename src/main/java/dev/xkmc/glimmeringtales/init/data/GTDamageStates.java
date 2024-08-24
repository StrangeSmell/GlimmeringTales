package dev.xkmc.glimmeringtales.init.data;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2damagetracker.contents.damage.DamageState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.Tags;

import java.util.Locale;
import java.util.function.Consumer;

public enum GTDamageStates implements DamageState {
	MAGIC;

	private final ResourceLocation id;

	GTDamageStates() {
		this.id = GlimmeringTales.loc(name().toLowerCase(Locale.ROOT));
	}

	@Override
	public void gatherTags(Consumer<TagKey<DamageType>> consumer) {
		consumer.accept(Tags.DamageTypes.IS_MAGIC);
		consumer.accept(DamageTypeTags.BYPASSES_ARMOR);
	}

	@Override
	public void removeTags(Consumer<TagKey<DamageType>> consumer) {
		consumer.accept(DamageTypeTags.IS_PROJECTILE);
		consumer.accept(DamageTypeTags.IS_FREEZING);
		consumer.accept(DamageTypeTags.IS_FIRE);
		consumer.accept(DamageTypeTags.IS_EXPLOSION);
	}

	@Override
	public ResourceLocation getId() {
		return id;
	}
}
