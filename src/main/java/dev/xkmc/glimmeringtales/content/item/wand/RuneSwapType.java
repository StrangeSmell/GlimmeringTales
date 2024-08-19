package dev.xkmc.glimmeringtales.content.item.wand;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2backpack.content.quickswap.common.EntryRenderContext;
import dev.xkmc.l2backpack.content.quickswap.common.QuickSwapOverlay;
import dev.xkmc.l2backpack.content.quickswap.entry.ISwapEntry;
import dev.xkmc.l2backpack.content.quickswap.type.MatcherSwapType;
import dev.xkmc.l2backpack.init.data.LBConfig;
import dev.xkmc.l2itemselector.overlay.SelectionSideBar;
import dev.xkmc.l2itemselector.overlay.TextBox;
import net.minecraft.client.gui.Font;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.List;

public class RuneSwapType extends MatcherSwapType {
	private ResourceLocation COPPER = ResourceLocation.fromNamespaceAndPath(GlimmeringTales.MODID,"textures/hud/copper.png");
	private ResourceLocation SELECT2 = ResourceLocation.fromNamespaceAndPath(GlimmeringTales.MODID,"textures/hud/select2.png");
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
		boolean selected = entry.selected();
		ItemStack stack = token.getStack();
		int i = entry.i();
		List<ItemStack> list = token.asList();
		int size = 9;
		int r = ctx.g().guiHeight()/4;

		if (selected && list.size() == 1) {

			if (!stack.isEmpty()) {
				ctx.g().renderTooltip(ctx.font(), stack.getHoverName(), 0, 0);
				TextBox box = new TextBox(ctx.g(), entry.center() ? 0 : 2, 1,
						(int)(ctx.g().guiWidth()/2-8+Math.sin(2*Math.PI*i/size)*r) + (entry.center() ? 22 : -6), (int)(ctx.g().guiHeight()/2-8-Math.cos(2*Math.PI*i/size)*r), -1);
				box.renderLongText(ctx.font(), List.of(stack.getHoverName()));
			}
		}

		ctx.renderItem(stack, (int)(ctx.g().guiWidth()/2-8+Math.sin(2*Math.PI*i/size)*r), (int)(ctx.g().guiHeight()/2-8-Math.cos(2*Math.PI*i/size)*r));
		ctx.g().blit(COPPER,(int)(ctx.g().guiWidth()/2-8+Math.sin(2*Math.PI*i/size)*r)-4,(int)(ctx.g().guiHeight()/2-8-Math.cos(2*Math.PI*i/size)*r)-4,24,24,0,0,24,24,24,24);

		if(selected) ctx.g().blit(SELECT2,(int)(ctx.g().guiWidth()/2-8+Math.sin(2*Math.PI*i/size)*r)-4,(int)(ctx.g().guiHeight()/2-8-Math.cos(2*Math.PI*i/size)*r)-4,24,24,0,0,24,24,24,24);

	}

}
