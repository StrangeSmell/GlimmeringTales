package dev.xkmc.glimmeringtales.content.entity.hostile;

import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.content.core.spell.SpellCost;

public record MobSpellData(NatureSpell spell, MobCastingData mob, SpellCost cost, double regen) {
}
