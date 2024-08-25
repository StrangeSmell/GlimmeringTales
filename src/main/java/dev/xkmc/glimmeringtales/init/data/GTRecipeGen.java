package dev.xkmc.glimmeringtales.init.data;

import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.DataIngredient;
import dev.xkmc.glimmeringtales.content.recipe.craft.WandRecipeBuilder;
import dev.xkmc.glimmeringtales.content.recipe.ritual.SimpleRitualRecipeBuilder;
import dev.xkmc.glimmeringtales.content.recipe.thunder.StrikeBlockRecipeBuilder;
import dev.xkmc.glimmeringtales.content.recipe.thunder.StrikeItemRecipeBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.l2complements.init.registrate.LCItems;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

			unlock(pvd, new StrikeBlockRecipeBuilder(ItemTags.LOGS_THAT_BURN, GTItems.STRUCK_LOG.get(),
					ItemStack.EMPTY)::unlockedBy, Items.OAK_LOG)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GTItems.DEPLETED_FLAME.get())::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern("AAA").pattern("ABA").pattern("AAA")
					.define('A', Items.MAGMA_BLOCK)
					.define('B', GTItems.CRYSTAL_NATURE)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GTItems.DEPLETED_WINTERSTORM.get())::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern("ACA").pattern("BXB").pattern("ACA")
					.define('A', Items.ICE)
					.define('B', Items.PACKED_ICE)
					.define('C', Items.BLUE_ICE)
					.define('X', GTItems.CRYSTAL_NATURE)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GTItems.CRYSTAL_VINE.get())::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern("AAA").pattern("ABA").pattern("AAA")
					.define('A', Tags.Items.SEEDS)
					.define('B', GTItems.CRYSTAL_NATURE)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(Items.CONDUIT, GTItems.CRYSTAL_OCEAN)::unlockedBy, Items.CONDUIT)
					.side(Items.PRISMARINE, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_NATURE, GTItems.CRYSTAL_THUNDER)::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.side(GTItems.CRYSTAL_EARTH, 1)
					.side(GTItems.CRYSTAL_FLAME, 1)
					.side(GTItems.CRYSTAL_WINTERSTORM, 1)
					.side(GTItems.CRYSTAL_OCEAN, 1)
					.side(GTItems.STRUCK_LOG, 4)
					.save(pvd);

			unlock(pvd, ShapedRecipeBuilder.shaped(RecipeCategory.MISC, GTItems.RITUAL_ALTAR.get())::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern("DGD").pattern("ANA").pattern("DWD")
					.define('A', Items.AMETHYST_SHARD)
					.define('G', Items.GOLD_INGOT)
					.define('N', GTItems.CRYSTAL_NATURE)
					.define('W', GTItems.STRUCK_LOG)
					.define('D', Items.DEEPSLATE_BRICKS)
					.save(pvd);
		}

		// wand
		{
			unlock(pvd, new WandRecipeBuilder(GTItems.WOOD_WAND, 1)::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern("  O").pattern(" I ").pattern("I  ")
					.define('O', GTTagGen.CRYSTAL)
					.define('I', Items.STICK)
					.save(pvd);

			unlock(pvd, new WandRecipeBuilder(GTItems.GOLD_WAND, 1)::unlockedBy, GTItems.CRYSTAL_NATURE.get())
					.pattern("  O").pattern("RI ").pattern("IR ")
					.define('O', GTTagGen.CRYSTAL)
					.define('I', Items.GOLD_INGOT)
					.define('R', Items.REDSTONE)
					.save(pvd);
		}

		// life
		{

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_LIFE, GTItems.RUNE_BAMBOO)::unlockedBy, GTItems.CRYSTAL_LIFE.get())
					.side(Items.BAMBOO, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_LIFE, GTItems.RUNE_CACTUS)::unlockedBy, GTItems.CRYSTAL_LIFE.get())
					.side(Items.CACTUS, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_LIFE, GTItems.RUNE_FLOWER)::unlockedBy, GTItems.CRYSTAL_LIFE.get())
					.side(ItemTags.FLOWERS, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_LIFE, GTItems.RUNE_VINE)::unlockedBy, GTItems.CRYSTAL_LIFE.get())
					.side(Items.VINE, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_LIFE, GTItems.RUNE_HAYBALE)::unlockedBy, GTItems.CRYSTAL_LIFE.get())
					.side(Items.HAY_BLOCK, 4)
					.side(Items.CARROT, 4)
					.save(pvd);

		}

		// earth
		{
			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_SAND)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Tags.Items.SANDS, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_GRAVEL)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Tags.Items.GRAVELS, 4)
					.side(Items.FLINT, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_QUARTZ)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Items.QUARTZ_BLOCK, 4)
					.side(Items.GLASS, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_CLAY)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Items.CLAY, 4)
					.side(Items.MUD, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_STONE)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Items.STONE, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_DRIPSTONE)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Items.DRIPSTONE_BLOCK, 4)
					.side(Items.POINTED_DRIPSTONE, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_EARTH, GTItems.RUNE_AMETHYST)::unlockedBy, GTItems.CRYSTAL_EARTH.get())
					.side(Items.AMETHYST_BLOCK, 4)
					.side(Items.AMETHYST_SHARD, 4)
					.save(pvd);

		}

		//
		{

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_FLAME, GTItems.RUNE_MAGMA)::unlockedBy, GTItems.CRYSTAL_FLAME.get())
					.side(Items.MAGMA_BLOCK, 4)
					.side(Items.MAGMA_CREAM, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_FLAME, GTItems.RUNE_SOUL_SAND)::unlockedBy, GTItems.CRYSTAL_FLAME.get())
					.side(Items.MAGMA_BLOCK, 4)
					.side(Items.MAGMA_CREAM, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_WINTERSTORM, GTItems.RUNE_SNOW)::unlockedBy, GTItems.CRYSTAL_WINTERSTORM.get())
					.side(Items.SNOW_BLOCK, 4)
					.side(Items.SNOWBALL, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_WINTERSTORM, GTItems.RUNE_ICE)::unlockedBy, GTItems.CRYSTAL_WINTERSTORM.get())
					.side(Items.ICE, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_WINTERSTORM, GTItems.RUNE_PACKED_ICE)::unlockedBy, GTItems.CRYSTAL_WINTERSTORM.get())
					.side(Items.PACKED_ICE, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_WINTERSTORM, GTItems.RUNE_BLUE_ICE)::unlockedBy, GTItems.CRYSTAL_WINTERSTORM.get())
					.side(Items.BLUE_ICE, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_WINTERSTORM, GTItems.RUNE_POWDER_SNOW)::unlockedBy, GTItems.CRYSTAL_WINTERSTORM.get())
					.side(Items.POWDER_SNOW_BUCKET, 4)
					.side(Items.SNOWBALL, 4)
					.save(pvd);
		}

		{

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_THUNDER, GTItems.RUNE_THUNDER)::unlockedBy, GTItems.CRYSTAL_THUNDER.get())
					.side(GTItems.STRUCK_LOG, 8)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_FLAME, GTItems.HELL_MARK)::unlockedBy, GTItems.CRYSTAL_FLAME.get())
					.side(GTItems.RUNE_SOUL_SAND, 1)
					.side(GTItems.RUNE_DRIPSTONE, 1)
					.side(LCItems.SOUL_CHARGE, 2)
					.side(Items.BLAZE_POWDER, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_FLAME, GTItems.LAVA_BURST)::unlockedBy, GTItems.CRYSTAL_FLAME.get())
					.side(GTItems.RUNE_MAGMA, 1)
					.side(GTItems.RUNE_STONE, 1)
					.side(LCItems.STRONG_CHARGE, 2)
					.side(Items.BLAZE_POWDER, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_WINTERSTORM, GTItems.WINTER_STORM)::unlockedBy, GTItems.CRYSTAL_WINTERSTORM.get())
					.side(GTItems.RUNE_POWDER_SNOW, 1)
					.side(GTItems.RUNE_GRAVEL, 1)
					.side(LCItems.HARD_ICE, 2)
					.side(Items.SNOW_BLOCK, 4)
					.save(pvd);

			unlock(pvd, new SimpleRitualRecipeBuilder(GTItems.CRYSTAL_WINTERSTORM, GTItems.SNOW_TORNADO)::unlockedBy, GTItems.CRYSTAL_WINTERSTORM.get())
					.side(GTItems.RUNE_SNOW, 1)
					.side(GTItems.RUNE_SAND, 1)
					.side(LCItems.HARD_ICE, 2)
					.side(Items.SNOW_BLOCK, 4)
					.save(pvd);
		}

	}

	public static <T> T unlock(RegistrateRecipeProvider pvd, BiFunction<String, Criterion<InventoryChangeTrigger.TriggerInstance>, T> func, Item item) {
		return func.apply("has_" + pvd.safeName(item), DataIngredient.items(item).getCriterion(pvd));
	}


}
