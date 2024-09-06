package dev.xkmc.glimmeringtales.content.block.ritual;

import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.l2modularblock.mult.ToolTipBlockMethod;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class GuideTextMethod implements ToolTipBlockMethod {

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext level, List<Component> list, TooltipFlag flag) {
		list.add(GTLang.TOOLTIP_RITUAL_FORM.get(GTItems.RITUAL_ALTAR.asStack().getHoverName()).withStyle(ChatFormatting.GRAY));
		list.add(GTLang.TOOLTIP_RITUAL_START.get().withStyle(ChatFormatting.DARK_AQUA));
	}

}
