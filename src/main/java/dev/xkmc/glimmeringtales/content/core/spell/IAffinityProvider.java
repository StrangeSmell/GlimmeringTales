package dev.xkmc.glimmeringtales.content.core.spell;

import dev.xkmc.glimmeringtales.content.item.wand.RuneWandItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

public interface IAffinityProvider {

	double get(SpellElement elem);

	default double getFinalAffinity(SpellElement elem, LivingEntity user, ItemStack wand) {
		var val = get(elem);
		var wandAff = RuneWandItem.getHandle(wand).getAffinity(user.level());
		if (wandAff != null) {
			val += wandAff.get(elem);
		}
		var ins = user.getAttribute(elem.getAffinity());
		if (ins != null) {
			val += ins.getValue();
		}
		return val;
	}
}
