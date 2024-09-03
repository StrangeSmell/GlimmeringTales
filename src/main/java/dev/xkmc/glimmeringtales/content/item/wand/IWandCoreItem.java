package dev.xkmc.glimmeringtales.content.item.wand;

import dev.xkmc.glimmeringtales.content.core.spell.SpellCost;
import dev.xkmc.glimmeringtales.content.core.spell.SpellInfo;
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

	SpellInfo getSpellInfo(Player player);

	default List<Component> getCastTooltip(Player player, ItemStack wand, ItemStack core) {
		return getSpellInfo(player).getCastTooltip(player, wand, core);
	}

	default SpellCost getCost(Player player, ItemStack wand, ItemStack core) {
		return getSpellInfo(player).getCost(player, wand);
	}

}
