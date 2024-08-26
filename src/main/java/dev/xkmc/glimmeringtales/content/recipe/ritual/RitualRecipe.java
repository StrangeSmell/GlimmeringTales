package dev.xkmc.glimmeringtales.content.recipe.ritual;

import dev.xkmc.glimmeringtales.content.block.altar.SideRitualBlockEntity;
import dev.xkmc.l2core.serial.recipe.BaseRecipe;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import dev.xkmc.l2serial.util.Wrappers;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@SerialClass
public abstract class RitualRecipe<T extends RitualRecipe<T>> extends BaseRecipe<T, RitualRecipe<?>, RitualInput> {


	public record Entry(Ingredient ingredient, ItemStack remainder) {

	}

	@SerialField
	public int time;
	@SerialField
	public Entry core;
	@SerialField
	public ArrayList<Entry> list = new ArrayList<>();

	private List<Predicate<SideRitualBlockEntity>> predicates = null;

	public RitualRecipe(RecType<T, RitualRecipe<?>, RitualInput> fac) {
		super(fac);
	}

	public int getTime() {
		return time;
	}

	@Override
	public boolean matches(RitualInput input, Level level) {
		return input.match(self());
	}

	@Override
	public ItemStack assemble(RitualInput input, HolderLookup.Provider provider) {
		input.assemble(self());
		input.core.setItem(core.remainder().copy());
		return core.remainder().copy();
	}

	@Override
	public ItemStack getResultItem(HolderLookup.Provider provider) {
		return core.remainder();
	}

	@Override
	public boolean canCraftInDimensions(int x, int y) {
		return false;
	}

	public T self() {
		return Wrappers.cast(this);
	}

	protected List<Predicate<SideRitualBlockEntity>> getPredicates() {
		if (predicates != null) return predicates;
		predicates = list.stream().<Predicate<SideRitualBlockEntity>>map(e -> x -> e.ingredient().test(x.getItem())).toList();
		return predicates;
	}

}
