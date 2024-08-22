package dev.xkmc.glimmeringtales.content.recipe.thunder;

import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class StrikeBlockRecipe extends BaseRecipe<StrikeBlockRecipe, StrikeBlockRecipe, StrikeBlockRecipe.Inv> {

	@SerialField
	public Ingredient ingredient;
	@SerialField
	public Block transformTo;

	@SerialField
	public ItemStack result;

	public StrikeBlockRecipe() {
		super(GTRecipes.RS_STRIKE_BLOCK.get());
	}

	@Override
	public boolean matches(Inv inv, Level level) {
		return ingredient.test(inv.state.getBlock().asItem().getDefaultInstance());
	}

	@Override
	public ItemStack assemble(Inv inv, HolderLookup.Provider access) {
		inv.setBlock(transformTo.defaultBlockState(), result.copy());
		return ItemStack.EMPTY;
	}

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return false;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider access) {
		return result;
	}

	public record Inv(Level level, BlockState state, BlockPos pos) implements RecipeInput {

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public ItemStack getItem(int i) {
			return ItemStack.EMPTY;
		}

		@Override
		public int size() {
			return 1;
		}

		public void setBlock(BlockState ans, ItemStack stack) {
			if (ans.isAir()) {
				level.removeBlock(pos, false);
				Block.popResource(level, pos, stack);
			} else {
				level.setBlockAndUpdate(pos, ans);
				Block.popResource(level, pos.above(), stack);
			}
		}

	}

}
