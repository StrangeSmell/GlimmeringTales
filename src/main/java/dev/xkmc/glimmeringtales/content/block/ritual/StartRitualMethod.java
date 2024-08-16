package dev.xkmc.glimmeringtales.content.block.ritual;

import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.l2modularblock.mult.UseItemOnBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class StartRitualMethod implements UseItemOnBlockMethod {

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (stack.is(GTItems.WAND)) {
			if (level.getBlockEntity(pos) instanceof NatureCoreBlockEntity be) {
				be.triggerCraft();
				return ItemInteractionResult.SUCCESS;
			}
		}
		return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
	}

}
