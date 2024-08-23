package dev.xkmc.glimmeringtales.content.item.wand;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record SpellCastContext(Level level, LivingEntity user, ItemStack wand) {

	public static SpellCastContext of(Level level, LivingEntity player, ItemStack stack) {
		return new SpellCastContext(level, player, stack);
	}

}
