package dev.xkmc.glimmeringtales.content.item.wand;

import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapToken;
import dev.xkmc.l2backpack.content.quickswap.common.SingleSwapItem;
import dev.xkmc.l2backpack.content.quickswap.common.SingleSwapToken;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapType;
import dev.xkmc.l2library.content.raytrace.FastItem;
import dev.xkmc.l2library.content.raytrace.IGlowingTarget;
import dev.xkmc.l2library.content.raytrace.RayTraceUtil;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class RuneWandItem extends SingleSwapItem implements IGlowingTarget, FastItem {

	public RuneWandItem(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public IQuickSwapToken<?> getTokenOfType(ItemStack stack, LivingEntity le, QuickSwapType type) {
		return type == GTRegistries.SWAP ? new SingleSwapToken(this, stack, type) : null;
	}

	@Override
	public boolean isValidContent(ItemStack stack) {
		return stack.getItem() instanceof IWandCoreItem;
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (isSelected && level.isClientSide() && entity instanceof Player player) {
			RayTraceUtil.clientUpdateTarget(player, getDistance(stack));
		}
	}

	@Override
	public int getDistance(ItemStack stack) {
		return getCore(stack).getItem() instanceof IWandCoreItem item ? item.entityTrace() : 0;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity e) {
		return 72000;
	}

	@Override
	public boolean isFast(ItemStack itemStack) {
		return true;
	}

	private ItemStack getCore(ItemStack stack) {
		return getItems(stack).get(getSelected(stack));
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		var spell = getSpell(stack, level);
		if (spell != null) {
			if (spell.castType() == SpellCastType.INSTANT) {
				if (castSpell(stack, level, player, spell, 0, false)) {
					return InteractionResultHolder.consume(stack);
				} else {
					return InteractionResultHolder.fail(stack);
				}
			} else {
				return ItemUtils.startUsingInstantly(level, player, hand);
			}
		}
		return super.use(level, player, hand);
	}

	@Override
	public void onUseTick(Level level, LivingEntity user, ItemStack stack, int remain) {
		super.onUseTick(level, user, stack, remain);
		var spell = getSpell(stack, level);
		if (spell != null) {
			if (spell.castType() == SpellCastType.CONTINUOUS) {
				castSpell(stack, level, user, spell, getUseDuration(stack, user) - remain, false);
			}
			if (spell.castType() == SpellCastType.CHARGE) {
				castSpell(stack, level, user, spell, getUseDuration(stack, user) - remain, true);
			}
		}
	}

	@Override
	public void releaseUsing(ItemStack stack, Level level, LivingEntity user, int remain) {
		var spell = getSpell(stack, level);
		if (spell != null) {
			if (spell.castType() == SpellCastType.CHARGE) {
				castSpell(stack, level, user, spell, getUseDuration(stack, user) - remain, false);
			}
		}
	}

	@Nullable
	private ISpellHolder getSpell(ItemStack stack, Level level) {
		var sel = getCore(stack);
		return sel.getItem() instanceof IWandCoreItem item ? item.getSpell(sel, level) : null;
	}

	private boolean castSpell(ItemStack stack, Level level, LivingEntity user, ISpellHolder spell, int useTick, boolean charging) {
		return user instanceof Player player && spell.cast(SpellCastContext.of(level, player, stack), useTick, charging);
	}

}
