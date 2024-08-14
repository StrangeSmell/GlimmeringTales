package dev.xkmc.glimmeringtales.content.item.wand;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface IWandCoreItem {

	int entityTrace();

	@Nullable
	ISpellHolder getSpell(ItemStack sel, Level level);

}
