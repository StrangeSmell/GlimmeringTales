package dev.xkmc.glimmeringtales.content.recipe.ritual;

import dev.xkmc.glimmeringtales.content.block.altar.CoreRitualBlockEntity;
import dev.xkmc.glimmeringtales.content.block.altar.SideRitualBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.common.util.RecipeMatcher;

import java.util.List;

public class RitualInput implements RecipeInput {

	protected final CoreRitualBlockEntity core;
	protected final List<SideRitualBlockEntity> list;
	private RitualRecipe<?> cachedRecipe = null;
	private int[] cachedResult = null;

	public RitualInput(CoreRitualBlockEntity core, List<SideRitualBlockEntity> list) {
		this.core = core;
		this.list = list.stream().filter(e -> !e.getItem().isEmpty()).toList();
	}

	public boolean match(RitualRecipe<?> recipe) {
		if (cachedRecipe == recipe)
			return cachedResult != null;
		cachedRecipe = recipe;
		if (!recipe.core.ingredient().test(core.getItem())) {
			return false;
		}
		cachedResult = RecipeMatcher.findMatches(list, recipe.getPredicates());
		return cachedResult != null;
	}

	public void assemble(RitualRecipe<?> recipe) {
		if (!match(recipe)) return;
		if (cachedResult == null) return;
		for (int i = 0; i < cachedResult.length; i++) {
			list.get(i).setItem(recipe.list.get(cachedResult[i]).remainder().copy());
		}
	}

	@Override
	public ItemStack getItem(int slot) {
		return slot == 0 ? core.getItem() : list.get(slot - 1).getItem();
	}

	@Override
	public int size() {
		return list.size() + 1;
	}

}
