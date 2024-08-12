package dev.xkmc.glimmeringtales.content.item.wand;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2backpack.content.quickswap.entry.ISwapEntry;
import dev.xkmc.l2backpack.content.quickswap.type.MatcherSwapType;
import dev.xkmc.l2backpack.init.data.LBConfig;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class RuneSwapType extends MatcherSwapType {

	public RuneSwapType() {
		super(GlimmeringTales.MODID + "_rune", true);
	}

	@Override
	public boolean activePopup() {
		return LBConfig.CLIENT.popupArrowOnSwitch.get();
	}

	@Override
	public boolean match(ItemStack stack) {
		return stack.getItem() instanceof RuneWandItem;
	}

	@Override
	public boolean isAvailable(Player player, ISwapEntry<?> token) {
		return !token.getStack().isEmpty();
	}

}
