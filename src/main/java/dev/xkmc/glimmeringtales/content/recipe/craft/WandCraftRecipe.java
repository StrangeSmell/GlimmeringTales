package dev.xkmc.glimmeringtales.content.recipe.craft;

import dev.xkmc.glimmeringtales.init.data.GTTagGen;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import dev.xkmc.l2backpack.content.common.BaseBagItem;
import dev.xkmc.l2core.serial.recipe.AbstractShapedRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

import java.util.List;

public class WandCraftRecipe extends AbstractShapedRecipe<WandCraftRecipe> {

	public WandCraftRecipe(String group, ShapedRecipePattern pattern, ItemStack result) {
		super(group, pattern, result);
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider pvd) {
		ItemStack core = ItemStack.EMPTY;
		for (int i = 0; i < input.size(); i++) {
			ItemStack ing = input.getItem(i);
			if (ing.is(GTTagGen.CORE)) {
				core = ing;
			}
		}
		ItemStack ans = getResultItem(pvd);
		if (!core.isEmpty()) {
			BaseBagItem.setItems(ans, List.of(core));
		}
		return ans;
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider pvd) {
		return GTItems.WAND_HANDLE.set(GTItems.WAND.asStack(), super.getResultItem(pvd).getItemHolder());
	}

	@Override
	public Serializer<WandCraftRecipe> getSerializer() {
		return GTRecipes.WAND.get();
	}

}
