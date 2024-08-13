package dev.xkmc.glimmeringtales.content.item.wand;

import dev.xkmc.glimmeringtales.content.item.rune.IWandCoreItem;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2backpack.content.quickswap.common.IQuickSwapToken;
import dev.xkmc.l2backpack.content.quickswap.common.SingleSwapItem;
import dev.xkmc.l2backpack.content.quickswap.common.SingleSwapToken;
import dev.xkmc.l2backpack.content.quickswap.type.QuickSwapType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class RuneWandItem extends SingleSwapItem {

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
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		var sel = getItems(stack).get(getSelected(stack));
		if (sel.getItem() instanceof IWandCoreItem rune) {
			return rune.onUse(level, player, stack);
		}
		return super.use(level, player, hand);
	}
}
