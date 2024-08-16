package dev.xkmc.glimmeringtales.content.item.wand;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2backpack.content.quickswap.common.EntryRenderContext;
import dev.xkmc.l2backpack.content.quickswap.entry.ISwapEntry;
import dev.xkmc.l2backpack.content.quickswap.type.MatcherSwapType;
import dev.xkmc.l2backpack.init.data.LBConfig;
import dev.xkmc.l2itemselector.overlay.SelectionSideBar;
import net.minecraft.client.gui.Font;
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

	@Override
	public void renderSelected(SelectionSideBar.Context ctx, Player player, ISwapEntry<?> token, EntryRenderContext entry) {
		int w = ctx.g().guiWidth();
		int h = ctx.g().guiHeight();
		int index = entry.i();
		float easing = entry.easing();
		boolean selected = entry.selected();
		ItemStack stack = token.getStack();
		Font font = ctx.font();
		//TODO
		super.renderSelected(ctx, player, token, entry);
	}

}
