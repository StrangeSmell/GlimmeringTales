package dev.xkmc.glimmeringtales.content.recipe.ritual;

import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import dev.xkmc.l2serial.serialization.marker.SerialClass;

@SerialClass
public class SimpleRitualRecipe extends RitualRecipe<SimpleRitualRecipe> {

	public SimpleRitualRecipe() {
		super(GTRecipes.RSR_SIMPLE.get());
	}

}
