package dev.xkmc.glimmeringtales.init.reg;

import dev.xkmc.glimmeringtales.content.recipe.ritual.RitualInput;
import dev.xkmc.glimmeringtales.content.recipe.ritual.RitualRecipe;
import dev.xkmc.glimmeringtales.content.recipe.ritual.SimpleRitualRecipe;
import dev.xkmc.glimmeringtales.content.recipe.thunder.StrikeBlockRecipe;
import dev.xkmc.glimmeringtales.content.recipe.thunder.StrikeItemRecipe;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;

public class GTRecipes {

	private static final SR<RecipeType<?>> RT = SR.of(GlimmeringTales.REG, BuiltInRegistries.RECIPE_TYPE);
	private static final SR<RecipeSerializer<?>> RS = SR.of(GlimmeringTales.REG, BuiltInRegistries.RECIPE_SERIALIZER);

	public static Val<RecipeType<StrikeBlockRecipe>> RT_STRIKE_BLOCK = RT.reg("strike_block", RecipeType::simple);
	public static Val<RecipeType<StrikeItemRecipe>> RT_STRIKE_ITEM = RT.reg("strike_item", RecipeType::simple);
	public static Val<RecipeType<RitualRecipe<?>>> RT_RITUAL = RT.reg("ritual", RecipeType::simple);

	public static final Val<BaseRecipe.RecType<StrikeBlockRecipe, StrikeBlockRecipe, StrikeBlockRecipe.Inv>> RS_STRIKE_BLOCK =
			RS.reg("strike_block", () -> new BaseRecipe.RecType<>(StrikeBlockRecipe.class, RT_STRIKE_BLOCK));
	public static final Val<BaseRecipe.RecType<StrikeItemRecipe, StrikeItemRecipe, SingleRecipeInput>> RS_STRIKE_ITEM =
			RS.reg("strike_item", () -> new BaseRecipe.RecType<>(StrikeItemRecipe.class, RT_STRIKE_ITEM));
	public static final Val<BaseRecipe.RecType<SimpleRitualRecipe, RitualRecipe<?>, RitualInput>> RSR_SIMPLE =
			RS.reg("simple_ritual", () -> new BaseRecipe.RecType<>(SimpleRitualRecipe.class, RT_RITUAL));

	public static void register() {
	}

}
