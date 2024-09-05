package dev.xkmc.glimmeringtales.content.entity.hostile;

import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.content.core.spell.SpellCost;
import dev.xkmc.glimmeringtales.content.item.wand.ISpellHolder;
import net.minecraft.world.item.ItemStack;

public record MobSpellData(ItemStack wand, NatureSpell spell, ISpellHolder holder, MobCastingData mob, SpellCost cost, double regen) {
}
