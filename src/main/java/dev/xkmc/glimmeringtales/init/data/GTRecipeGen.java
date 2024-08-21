package dev.xkmc.glimmeringtales.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.glimmeringtales.content.recipe.ritual.SimpleRitualRecipeBuilder;
import dev.xkmc.glimmeringtales.content.recipe.thunder.StrikeBlockRecipeBuilder;
import dev.xkmc.glimmeringtales.content.recipe.thunder.StrikeItemRecipeBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;

import java.util.function.BiFunction;

public class GTRecipeGen {

	public static void onRecipeGen(RegistrateRecipeProvider pvd) {
		{

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

		// life
		{

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_LIFE, GTItems.RUNE_BAMBOO)::unlockedBy, GTItems.CRYSTAL_LIFE.get())
					.side(Ingredient.of(Items.BAMBOO), 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_LIFE, GTItems.RUNE_CACTUS)::unlockedBy, GTItems.CRYSTAL_LIFE.get())
					.side(Ingredient.of(Items.CACTUS), 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_LIFE, GTItems.RUNE_FLOWER)::unlockedBy, GTItems.CRYSTAL_LIFE.get())
					.side(Ingredient.of(ItemTags.FLOWERS), 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_LIFE, GTItems.RUNE_VINE)::unlockedBy, GTItems.CRYSTAL_LIFE.get())
					.side(Ingredient.of(Items.VINE), 8)
					.save(pvd);

		}

		// earth
		{
			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_SAND)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Ingredient.of(Tags.Items.SANDS), 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_GRAVEL)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Ingredient.of(Tags.Items.GRAVELS), 4)
					.side(Ingredient.of(Items.FLINT), 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_QUARTZ)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Ingredient.of(Items.QUARTZ_BLOCK), 4)
					.side(Ingredient.of(Items.GLASS), 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_CLAY)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Ingredient.of(Items.CLAY), 4)
					.side(Ingredient.of(Items.MUD), 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_DRIPSTONE)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Ingredient.of(Items.DRIPSTONE_BLOCK), 4)
					.side(Ingredient.of(Items.POINTED_DRIPSTONE), 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_AMETHYST)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Ingredient.of(Items.AMETHYST_BLOCK), 4)
					.side(Ingredient.of(Items.AMETHYST_SHARD), 4)
					.save(pvd);

		}

	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, Criterion<InventoryChangeTrigger.TriggerInstance>, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCriterion(pvd));
	}


}
