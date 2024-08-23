package dev.xkmc.glimmeringtales.content.item.wand;

import dev.xkmc.glimmeringtales.content.core.spell.IAffinityProvider;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.l2magic.content.engine.context.SpellContext;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;

public interface ISpellHolder {

	SpellCastType castType();

	boolean cast(SpellCastContext user, int useTick, boolean charging);

	default void execute(NatureSpell spell, SpellContext ctx, SpellCastContext user, IAffinityProvider aff, int useTick, boolean charging) {
		var val = aff.get(spell.elem());
		var wandAff = RuneWandItem.getHandle(user.wand()).getAffinity(user.level());
		if (wandAff != null) {
			val += aff.get(spell.elem());
		}
		if (spell.consumeMana(user.user(), user.wand(), val, useTick, charging))
			spell.spell().value().execute(ctx);
	}

}
