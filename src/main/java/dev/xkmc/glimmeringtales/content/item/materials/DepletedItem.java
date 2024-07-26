package dev.xkmc.glimmeringtales.content.item.materials;

import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

public class DepletedItem extends LightningImmuneItem {

	private final Supplier<Block> pred;
	private final IntSupplier count;
	private final Supplier<Item> next;
	private final Supplier<SoundEvent> sound;

	public DepletedItem(Properties properties, Supplier<Block> pred,
						IntSupplier count, Supplier<Item> next,
						Supplier<SoundEvent> sound) {
		super(properties.stacksTo(1));
		this.pred = pred;
		this.count = count;
		this.next = next;
		this.sound = sound;
	}

	@Override
	public InteractionResult useOn(UseOnContext context) {
		var level = context.getLevel();
		var pos = context.getClickedPos();
		var state = level.getBlockState(pos);
		var stack = context.getItemInHand();
		var player = context.getPlayer();
		if (player != null && state.is(pred.get())) {
			if (!level.isClientSide()) {
				level.removeBlock(pos, false);
				int use = GTItems.PROGRESS.getOrDefault(stack, 0) + 1;
				if (use >= count.getAsInt()) {
					player.setItemInHand(context.getHand(), next.get().getDefaultInstance());
				} else GTItems.PROGRESS.set(stack, use + 1);
				level.playSound(player, pos, sound.get(), SoundSource.PLAYERS, 1, 1);
			}
			return InteractionResult.SUCCESS;
		}
		return super.useOn(context);
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return 0xffffffff;
	}

	@Override
	public int getBarWidth(ItemStack stack) {
		int use = GTItems.PROGRESS.getOrDefault(stack, 0);
		float prog = 13f * Math.clamp(use / count.getAsInt(), 0, 1);
		return Math.round(prog);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> list, TooltipFlag flag) {
		list.add(GTLang.TOOLTIP_BLOCK.get(
				Component.translatable(pred.get().getDescriptionId())
						.withStyle(ChatFormatting.YELLOW)
		).withStyle(ChatFormatting.GRAY));
		int use = GTItems.PROGRESS.getOrDefault(stack, 0);
		list.add(GTLang.TOOLTIP_FILL.get(
				Component.literal(use + "").withStyle(ChatFormatting.DARK_AQUA),
				Component.literal(count.getAsInt() + "").withStyle(ChatFormatting.DARK_AQUA)
		).withStyle(ChatFormatting.GRAY));
	}

}
