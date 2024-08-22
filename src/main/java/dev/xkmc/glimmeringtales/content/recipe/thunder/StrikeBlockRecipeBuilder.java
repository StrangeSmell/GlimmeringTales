package dev.xkmc.glimmeringtales.content.recipe.thunder;

import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import dev.xkmc.l2core.serial.recipe.BaseRecipeBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public class StrikeBlockRecipeBuilder extends BaseRecipeBuilder<StrikeBlockRecipeBuilder, StrikeBlockRecipe, StrikeBlockRecipe, StrikeBlockRecipe.Inv> {

	public StrikeBlockRecipeBuilder(Block in, Block out, ItemStack result) {
		super(GTRecipes.RS_STRIKE_BLOCK.get(), result.getItem());
		this.recipe.ingredient = Ingredient.of(in);
		this.recipe.transformTo = out;
		this.recipe.result = result;
	}

	public StrikeBlockRecipeBuilder(TagKey<Item> in, Block out, ItemStack result) {
		super(GTRecipes.RS_STRIKE_BLOCK.get(), result.getItem());
		this.recipe.ingredient = Ingredient.of(in);
		this.recipe.transformTo = out;
		this.recipe.result = result;
	}

	@Override
	public void save(RecipeOutput output) {
		if (getResult() != Items.AIR) super.save(output);
		else super.save(output, BuiltInRegistries.BLOCK.getKey(recipe.transformTo));
	}

}
