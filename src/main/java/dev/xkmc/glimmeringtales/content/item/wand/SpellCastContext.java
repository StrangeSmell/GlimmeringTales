package dev.xkmc.glimmeringtales.content.item.wand;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public record SpellCastContext(Level level, Player user, ItemStack wand) {

	public static SpellCastContext of(Level level, Player player, ItemStack stack) {
		return new SpellCastContext(level, player, stack);
	}

}
