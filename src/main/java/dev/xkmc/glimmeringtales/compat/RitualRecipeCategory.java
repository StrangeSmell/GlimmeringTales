package dev.xkmc.glimmeringtales.compat;

import dev.xkmc.glimmeringtales.content.recipe.ritual.RitualRecipe;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTLang;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.l2core.compat.jei.BaseRecipeCategory;
import dev.xkmc.l2serial.util.Wrappers;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class RitualRecipeCategory extends BaseRecipeCategory<RitualRecipe<?>, RitualRecipeCategory> {

	protected static final ResourceLocation BG = GlimmeringTales.loc("textures/jei/background.png");
	private static final int[] SLOTS = {1, 7, 3, 5, 0, 2, 6, 8};

	public RitualRecipeCategory() {
		super(GlimmeringTales.loc("ritual"), Wrappers.cast(RitualRecipe.class));
	}

	public RitualRecipeCategory init(IGuiHelper guiHelper) {
		background = guiHelper.createDrawable(BG, 0, 36, 144, 54);
		icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, GTItems.RITUAL_MATRIX.asStack());
		return this;
	}

	@Override
	public Component getTitle() {
		return GTLang.JEI_RITUAL.get();
	}

	@Override
	public void setRecipe(IRecipeLayoutBuilder builder, RitualRecipe<?> recipe, IFocusGroup focuses) {
		builder.addSlot(RecipeIngredientRole.INPUT, 19, 19).addIngredients(recipe.core.ingredient());
		builder.addSlot(RecipeIngredientRole.OUTPUT, 109, 19).addItemStack(recipe.core.remainder());
		int n = recipe.list.size();
		for (int i = 0; i < n; i++) {
			var e = recipe.list.get(i);
			int slot = SLOTS[i];
			int x = 18 * (slot % 3);
			int y = 18 * (slot / 3);
			builder.addSlot(RecipeIngredientRole.INPUT, 1 + x, 1 + y).addIngredients(e.ingredient());
			builder.addSlot(RecipeIngredientRole.OUTPUT, 91 + x, 1 + y).addItemStack(e.remainder());
		}
	}

}
