package dev.xkmc.glimmeringtales.content.item.rune;

import dev.xkmc.glimmeringtales.content.item.wand.ISpellHolder;
import dev.xkmc.glimmeringtales.content.item.wand.IWandCoreItem;
import dev.xkmc.glimmeringtales.content.item.wand.SpellCastContext;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IBlockSpellItem extends IWandCoreItem, ISpellHolder {

	boolean castSpell(SpellCastContext user);

	@Override
	default SpellCastType castType() {
		return SpellCastType.INSTANT;
	}

	@Override
	default ISpellHolder getSpell(ItemStack sel, Level level) {
		return this;
	}

	@Override
	default boolean cast(SpellCastContext user, int useTick, boolean charging) {
		return castSpell(user);
	}

}
