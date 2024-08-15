package dev.xkmc.glimmeringtales.content.recipe.ritual;

import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2core.serial.recipe.BaseRecipeBuilder;
import net.minecraft.world.item.Item;

public class SimpleRitualRecipeBuilder extends BaseRecipeBuilder<
		SimpleRitualRecipeBuilder,
		SimpleRitualRecipe,
		RitualRecipe<?>,
		RitualInput> {

	public SimpleRitualRecipeBuilder(BaseRecipe.RecType<SimpleRitualRecipe, RitualRecipe<?>, RitualInput> type, Item result) {
		super(GTRecipes.RSR_SIMPLE.get(), result);
	}

}
