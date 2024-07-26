package dev.xkmc.glimmeringtales.content.item.materials;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class LightningImmuneItem extends Item {

	public LightningImmuneItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean canBeHurtBy(ItemStack stack, DamageSource source) {
		if (source.is(DamageTypeTags.IS_LIGHTNING)) return false;
		if (source.is(DamageTypeTags.IS_FIRE)) return false;
		return super.canBeHurtBy(stack, source);
	}
}
