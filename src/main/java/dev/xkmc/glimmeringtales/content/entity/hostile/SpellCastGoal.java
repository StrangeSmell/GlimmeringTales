package dev.xkmc.glimmeringtales.content.entity.hostile;

import dev.xkmc.glimmeringtales.content.item.rune.BaseRuneItem;
import dev.xkmc.glimmeringtales.content.item.wand.RuneWandItem;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class SpellCastGoal extends StrafingRangedAttackGoal {

	public SpellCastGoal(Mob mob, MobCastingConfig config) {
		super(mob, config);
	}

	@Override
	protected double getAttackRangeSqr(LivingEntity target) {
		var spell = getSpell();
		if (spell == null) return 16;
		return spell.mob().idealRange();
	}

	@Override
	protected int attack(LivingEntity target, int seeTime) {
		var spell = getSpell();
		if (spell == null) return 20;

		return 20;
	}

	@Override
	protected boolean canCastSpell() {
		var spell = getSpell();
		return spell != null;
	}

	@Nullable
	public MobSpellData getSpell() {
		ItemStack wand = mob.getItemBySlot(EquipmentSlot.MAINHAND);
		if (!wand.is(GTItems.WAND)) return null;
		ItemStack core = RuneWandItem.getCore(wand);
		if (core.getItem() instanceof BaseRuneItem item) {
			var info = item.getSpellInfo(mob.level().registryAccess());
			if (info.spell() != null) {
				var ns = info.spell().value();
				if (ns.mob() != null) {
					var ins = mob.getAttribute(GTRegistries.MANA_REGEN);
					double regen = ins == null ?
							GTRegistries.MANA_REGEN.get().getDefaultValue() :
							ins.getValue();
					return new MobSpellData(ns, ns.mob(), info.getCost(mob, wand), regen);
				}
			}
		}
		return null;
	}

}
