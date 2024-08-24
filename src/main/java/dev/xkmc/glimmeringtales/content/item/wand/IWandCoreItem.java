package dev.xkmc.glimmeringtales.content.item.wand;

import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface IWandCoreItem {

	int entityTrace();

	@Nullable
	ISpellHolder getSpell(ItemStack sel, Level level);

	ModelResourceLocation model();

	List<Component> getCastTooltip(Player player, ItemStack wand, ItemStack core);

}
