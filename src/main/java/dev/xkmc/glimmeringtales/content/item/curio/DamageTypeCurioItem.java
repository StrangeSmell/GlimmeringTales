package dev.xkmc.glimmeringtales.content.item.curio;

import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.l2damagetracker.contents.damage.DamageState;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class DamageTypeCurioItem extends AttributeCurioItem {

	private final DamageState state;
	private final GTLang lang;

	public DamageTypeCurioItem(Properties properties, DamageState state, GTLang lang) {
		super(properties);
		this.state = state;
		this.lang = lang;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag tooltipFlag) {
		list.add(lang.get().withStyle(ChatFormatting.GOLD));
	}

	public DamageState getState() {
		return state;
	}
}
