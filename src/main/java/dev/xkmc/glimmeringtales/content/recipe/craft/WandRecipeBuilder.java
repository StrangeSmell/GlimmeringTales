package dev.xkmc.glimmeringtales.content.recipe.craft;

import dev.xkmc.l2core.serial.recipe.CustomShapedBuilder;
import net.minecraft.world.level.ItemLike;

public class WandRecipeBuilder extends CustomShapedBuilder<WandCraftRecipe> {

	public WandRecipeBuilder(ItemLike result, int count) {
		super(WandCraftRecipe::new, result, count);
	}

}
