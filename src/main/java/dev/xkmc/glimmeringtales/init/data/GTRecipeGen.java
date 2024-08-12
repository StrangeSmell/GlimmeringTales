package dev.xkmc.glimmeringtales.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.glimmeringtales.content.recipe.StrikeBlockRecipeBuilder;
import dev.xkmc.glimmeringtales.content.recipe.StrikeItemRecipeBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.function.BiFunction;

public class GTRecipeGen {

	public static void onRecipeGen(RegistrateRecipeProvider pvd) {

		unlock(pvd, new StrikeItemRecipeBuilder(Ingredient.of(Items.LAPIS_LAZULI),
				GTItems.CRYSTAL_NATURE.asStack())::unlockedBy, Items.LAPIS_LAZULI)
				.save(pvd);

		unlock(pvd, new StrikeBlockRecipeBuilder(Blocks.BUDDING_AMETHYST, Blocks.AIR,
				GTItems.CRYSTAL_EARTH.asStack())::unlockedBy, Items.AMETHYST_SHARD)
				.save(pvd);

		unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GTItems.DEPLETED_FLAME.get())::unlockedBy, GTItems.CRYSTAL_NATURE.get())
				.pattern("AAA").pattern("ABA").pattern("AAA")
				.define('A', Items.MAGMA_BLOCK)
				.define('B', GTItems.CRYSTAL_NATURE)
				.save(pvd);

		unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GTItems.DEPLETED_WINTERSTORM.get())::unlockedBy, GTItems.CRYSTAL_NATURE.get())
				.pattern("AAA").pattern("ABA").pattern("AAA")
				.define('A', Items.BLUE_ICE)
				.define('B', GTItems.CRYSTAL_NATURE)
				.save(pvd);

		unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GTItems.CRYSTAL_VINE.get())::unlockedBy, GTItems.CRYSTAL_NATURE.get())
				.pattern("AAA").pattern("ABA").pattern("AAA")
				.define('A', Tags.Items.SEEDS)
				.define('B', GTItems.CRYSTAL_NATURE)
				.save(pvd);


	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, Criterion<InventoryChangeTrigger.TriggerInstance>, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCriterion(pvd));
	}


}
