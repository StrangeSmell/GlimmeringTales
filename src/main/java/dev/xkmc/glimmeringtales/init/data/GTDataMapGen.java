package dev.xkmc.glimmeringtales.init.data;

import com.tterrag.registrate.providers.RegistrateDataMapProvider;
import dev.xkmc.glimmeringtales.content.core.spell.BlockReplace;
import dev.xkmc.glimmeringtales.content.core.spell.ElementAffinity;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.Map;

public class GTDataMapGen {

	public static void genMap(RegistrateDataMapProvider pvd) {
		// affinity
		{
			var builder = pvd.builder(GTRegistries.AFFINITY.reg());
			builder.add(GTItems.CRYSTAL_NATURE, ElementAffinity.of(Map.of(
					GTRegistries.LIFE.get(), 0.5d,
					GTRegistries.EARTH.get(), 0.5d,
					GTRegistries.FLAME.get(), 0.5d,
					GTRegistries.SNOW.get(), 0.5d
			)), false);
			builder.add(GTItems.CRYSTAL_LIFE, ElementAffinity.of(Map.of(
					GTRegistries.LIFE.get(), 1.5d
			)), false);
			builder.add(GTItems.CRYSTAL_EARTH, ElementAffinity.of(Map.of(
					GTRegistries.EARTH.get(), 1.5d
			)), false);
			builder.add(GTItems.CRYSTAL_FLAME, ElementAffinity.of(Map.of(
					GTRegistries.FLAME.get(), 1.5d
			)), false);
			builder.add(GTItems.CRYSTAL_WINTERSTORM, ElementAffinity.of(Map.of(
					GTRegistries.SNOW.get(), 1.5d
			)), false);
			builder.add(GTItems.CRYSTAL_OCEAN, ElementAffinity.of(Map.of(
					GTRegistries.OCEAN.get(), 1.5d
			)), false);
			builder.add(GTItems.CRYSTAL_THUNDER, ElementAffinity.of(Map.of(
					GTRegistries.THUNDER.get(), 1.5d
			)), false);

			builder.add(GTItems.WOOD_WAND, ElementAffinity.of(Map.of(
					GTRegistries.LIFE.get(), 0.25d
			)), false);
			builder.add(GTItems.GOLD_WAND, ElementAffinity.of(Map.of(
					GTRegistries.EARTH.get(), 0.25d
			)), false);
		}
		// melt
		{
			var melt = pvd.builder(GTRegistries.MELT.reg());
			var replace = pvd.builder(GTRegistries.REPLACE.reg());
			melt(melt, replace, Blocks.STONE, GTItems.MAGMA_STONE.get());
			melt(melt, replace, Blocks.DEEPSLATE, GTItems.MAGMA_DEEPSLATE.get());
			melt(melt, replace, Blocks.NETHERRACK, GTItems.MAGMA_NETHERRACK.get());

		}
	}

	private static void melt(DataMapProvider.Builder<BlockReplace, Block> melt, DataMapProvider.Builder<BlockReplace, Block> replace, Block from, Block to) {
		melt.add(from.builtInRegistryHolder(), BlockReplace.of(to), false);
		replace.add(to.builtInRegistryHolder(), BlockReplace.of(from), false);
	}

}
