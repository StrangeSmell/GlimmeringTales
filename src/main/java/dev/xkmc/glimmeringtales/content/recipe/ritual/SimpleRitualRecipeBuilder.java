package dev.xkmc.glimmeringtales.content.recipe.ritual;

import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import dev.xkmc.l2core.serial.recipe.BaseRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class SimpleRitualRecipeBuilder extends BaseRecipeBuilder<
		SimpleRitualRecipeBuilder,
		SimpleRitualRecipe,
		RitualRecipe<?>,
		RitualInput> {

	public SimpleRitualRecipeBuilder(ItemLike core, ItemLike result) {
		this(Ingredient.of(core), result.asItem().getDefaultInstance());
	}

	public SimpleRitualRecipeBuilder(Ingredient core, ItemStack result) {
		super(GTRecipes.RSR_SIMPLE.get(), result.getItem());
		recipe.core = new RitualRecipe.Entry(core, result);
	}

	public SimpleRitualRecipeBuilder side(Ingredient in, int count) {
		return side(in, ItemStack.EMPTY, count);
	}

	public SimpleRitualRecipeBuilder side(Ingredient in, ItemStack out, int count) {
		for (int i = 0; i < count; i++)
			recipe.list.add(new RitualRecipe.Entry(in, out));
		return this;
	}

}
