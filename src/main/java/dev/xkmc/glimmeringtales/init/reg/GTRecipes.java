package dev.xkmc.glimmeringtales.init.reg;

import dev.xkmc.glimmeringtales.content.recipe.StrikeBlockRecipe;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2core.init.reg.simple.SR;
import dev.xkmc.l2core.init.reg.simple.Val;
import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

public class GTRecipes {

	private static final SR<RecipeType<?>> RT = SR.of(GlimmeringTales.REG, BuiltInRegistries.RECIPE_TYPE);
	private static final SR<RecipeSerializer<?>> RS = SR.of(GlimmeringTales.REG, BuiltInRegistries.RECIPE_SERIALIZER);

	public static Val<RecipeType<StrikeBlockRecipe>> RT_STRIKE_BLOCK = RT.reg("strike_block", RecipeType::simple);

	public static final Val<BaseRecipe.RecType<StrikeBlockRecipe, StrikeBlockRecipe, StrikeBlockRecipe.Inv>> RS_STRIKE_BLOCK =
			RS.reg("strike_block", () -> new BaseRecipe.RecType<>(StrikeBlockRecipe.class, RT_STRIKE_BLOCK));

	public static void register() {

	}

}
