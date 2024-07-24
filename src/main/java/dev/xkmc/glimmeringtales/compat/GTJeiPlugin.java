package dev.xkmc.glimmeringtales.compat;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;

@JeiPlugin
public class GTJeiPlugin implements IModPlugin {

	public static GTJeiPlugin INSTANCE;

	public final ResourceLocation UID = GlimmeringTales.loc("main");

	public final StrikeBlockRecipeCategory STRIKE_BLOCK = new StrikeBlockRecipeCategory();

	public IGuiHelper GUI_HELPER;

	public GTJeiPlugin() {
		INSTANCE = this;
	}

	@Override
	public ResourceLocation getPluginUid() {
		return UID;
	}

	@Override
	public void registerIngredients(IModIngredientRegistration registration) {
	}

	@Override
	public void registerCategories(IRecipeCategoryRegistration registration) {
		IGuiHelper helper = registration.getJeiHelpers().getGuiHelper();
		registration.addRecipeCategories(STRIKE_BLOCK.init(helper));
		GUI_HELPER = helper;
	}

	@Override
	public void registerVanillaCategoryExtensions(IVanillaCategoryExtensionRegistration registration) {
	}

	@Override
	public void registerRecipes(IRecipeRegistration registration) {
		var level = Minecraft.getInstance().level;
		assert level != null;
		registration.addRecipes(STRIKE_BLOCK.getRecipeType(), level.getRecipeManager().getAllRecipesFor(GTRecipes.RT_STRIKE_BLOCK.get()).stream().map(RecipeHolder::value).toList());
	}

	@Override
	public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
	}

	@Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(Items.TRIDENT.getDefaultInstance(), STRIKE_BLOCK.getRecipeType());
		registration.addRecipeCatalyst(Blocks.LIGHTNING_ROD.asItem().getDefaultInstance(), STRIKE_BLOCK.getRecipeType());
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
	}

	@Override
	public void registerAdvanced(IAdvancedRegistration registration) {
	}

	@Override
	public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
	}

}
