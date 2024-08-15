package dev.xkmc.glimmeringtales.content.recipe.thunder;

import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

@SerialClass
public class StrikeItemRecipe extends BaseRecipe<StrikeItemRecipe, StrikeItemRecipe, SingleRecipeInput> {

	@SerialField
	public Ingredient ingredient;

	@SerialField
	public ItemStack result;

	public StrikeItemRecipe() {
		super(GTRecipes.RS_STRIKE_ITEM.get());
	}

	@Override
	public boolean matches(SingleRecipeInput inv, Level level) {
		return ingredient.test(inv.getItem(0));
	}

	@Override
	public ItemStack assemble(SingleRecipeInput inv, HolderLookup.Provider access) {
		return result.copyWithCount(inv.getItem(0).getCount());
	}

	@Override
	public boolean canCraftInDimensions(int i, int i1) {
		return false;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider access) {
		return result;
	}

}
