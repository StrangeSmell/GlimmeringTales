package dev.xkmc.glimmeringtales.content.entity.hostile;

import dev.xkmc.glimmeringtales.content.item.rune.BaseRuneItem;
import dev.xkmc.glimmeringtales.content.item.wand.RuneWandItem;
import dev.xkmc.glimmeringtales.content.item.wand.SpellCastContext;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public class SpellCastGoal extends StrafingRangedAttackGoal {

	private int useTick = 0;

	public SpellCastGoal(Mob mob, MobCastingConfig config) {
		super(mob, config);
	}

	@Override
	protected double getAttackRangeSqr(LivingEntity target) {
		var spell = getSpell();
		if (spell == null) return 256;
		return spell.mob().idealRange() * spell.mob().idealRange();
	}

	@Override
	public void start() {
		super.start();
		useTick = 0;
	}

	@Override
	public void stop() {
		if (useTick > 0) {
			var spell = getSpell();
			if (spell != null) {
				nextAttackTimestamp = mob.level().getGameTime() + spell.getCooldown(useTick);
			}
		}
		useTick = 0;
		if (mob.isUsingItem()) {
			mob.stopUsingItem();
		}
		super.stop();
	}

	@Override
	protected int attack(LivingEntity target, int seeTime) {
		var spell = getSpell();
		if (spell == null) return 20;
		var action = spell.spell().spell().value();
		var ctx = SpellCastContext.of(mob.level(), mob, spell.wand());
		int cost;
		if (action.castType() == SpellCastType.INSTANT) {
			spell.holder().cast(ctx, 0, false);
			cost = 1;
		} else {
			if (useTick == 0) {
				mob.startUsingItem(InteractionHand.MAIN_HAND);
			}
			if (useTick < spell.mob().maxUseTick()) {
				spell.holder().cast(ctx, useTick, action.castType() == SpellCastType.CHARGE);
				useTick++;
				return 0;
			} else {
				if (action.castType() == SpellCastType.CHARGE) {
					spell.holder().cast(ctx, useTick, false);
				}
				int maxTick = spell.spell().maxConsumeTick();
				cost = useTick;
				mob.stopUsingItem();
				useTick = 0;
			}
		}
		return spell.getCooldown(cost);
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
			var holder = item.getSpell(core, mob.level());
			var info = item.getSpellInfo(mob.level().registryAccess());
			if (holder != null && info.spell() != null) {
				var ns = info.spell().value();
				if (ns.mob() != null) {
					var ins = mob.getAttribute(GTRegistries.MANA_REGEN);
					double regen = ins == null ?
							GTRegistries.MANA_REGEN.get().getDefaultValue() :
							ins.getValue();
					return new MobSpellData(wand, ns, holder, ns.mob(), info.getCost(mob, wand), regen);
				}
			}
		}
		return null;
	}

}
