package dev.xkmc.glimmeringtales.content.recipe;

import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import dev.xkmc.l2core.serial.recipe.BaseRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.SingleRecipeInput;

public class StrikeItemRecipeBuilder extends BaseRecipeBuilder<StrikeItemRecipeBuilder, StrikeItemRecipe, StrikeItemRecipe, SingleRecipeInput> {

	public StrikeItemRecipeBuilder(Ingredient ing, ItemStack result) {
		super(GTRecipes.RS_STRIKE_ITEM.get(), result.getItem());
		this.recipe.ingredient = ing;
		this.recipe.result = result;
	}

}
