package dev.xkmc.glimmeringtales.content.item.rune;

import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IWandCoreItem {

	InteractionResultHolder<ItemStack> onUse(Level level, Player player, ItemStack stack);

}
