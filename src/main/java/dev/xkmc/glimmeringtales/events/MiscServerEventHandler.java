package dev.xkmc.glimmeringtales.events;

import dev.xkmc.glimmeringtales.content.recipe.StrikeBlockRecipe;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import dev.xkmc.l2library.init.L2Library;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightningRodBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;

@EventBusSubscriber(modid = GlimmeringTales.MODID, bus = EventBusSubscriber.Bus.GAME)
public class MiscServerEventHandler {

	@SubscribeEvent(priority = EventPriority.HIGH)
	public static void onEntityStruck(EntityStruckByLightningEvent event) {
		if (event.getEntity() instanceof ItemEntity ie) {
			var level = ie.level();
			var cont = new SingleRecipeInput(ie.getItem());
			var ans = level.getRecipeManager()
					.getRecipeFor(GTRecipes.RT_STRIKE_ITEM.get(), cont, level);
			ans.ifPresent(holder -> ie.setItem(holder.value().assemble(cont, level.registryAccess())));
		}
	}

	@SubscribeEvent(priority = EventPriority.LOW)
	public static void onEntityAdded(EntityJoinLevelEvent event) {
		var level = event.getLevel();
		if (!level.isClientSide()) return;
		if (!(event.getEntity() instanceof LightningBolt ie)) return;
		BlockPos pos = BlockPos.containing(ie.position().add(0, -1e-6, 0));
		BlockState state = level.getBlockState(pos);
		BlockPos hitPos;
		BlockState hitState;
		if (state.is(Blocks.LIGHTNING_ROD)) {
			hitPos = pos.relative(state.getValue(LightningRodBlock.FACING).getOpposite());
			hitState = level.getBlockState(hitPos);
		} else {
			hitPos = pos;
			hitState = state;
		}
		var cont = new StrikeBlockRecipe.Inv(level, hitState, hitPos);
		var ans = level.getRecipeManager().getRecipeFor(GTRecipes.RT_STRIKE_BLOCK.get(), cont, level);
		if (ans.isEmpty()) return;
		ans.get().value().assemble(cont, level.registryAccess());
	}

}
