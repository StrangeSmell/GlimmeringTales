package dev.xkmc.glimmeringtales.init.reg;

import com.tterrag.registrate.providers.DataProviderInitializer;
import com.tterrag.registrate.providers.RegistrateTagsProvider;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.OptionalInt;

public class GTWorldGen {

	public static class Placement {

		private final String id;
		private final int spread;

		public final ResourceKey<PlacedFeature> placeKey;
		public final TagKey<Biome> biomeTag;


		public Placement(String id, int spread) {
			this.id = id;
			this.spread = spread;
			placeKey = ResourceKey.create(Registries.PLACED_FEATURE, GlimmeringTales.loc(id));
			biomeTag = TagKey.create(Registries.BIOME, GlimmeringTales.loc(id));
		}

		public void placed(BootstrapContext<PlacedFeature> ctx, Holder<ConfiguredFeature<?, ?>> cf) {
			ctx.register(placeKey, new PlacedFeature(cf, VegetationPlacements.treePlacement(
					PlacementUtils.countExtra(0, 1f / spread, 1),
					GTItems.STRUCK_SAPLING.get())));
		}

		public void biome(BootstrapContext<BiomeModifier> ctx) {
			var biomes = ctx.lookup(Registries.BIOME);
			var features = ctx.lookup(Registries.PLACED_FEATURE);
			HolderSet<Biome> set = biomes.getOrThrow(biomeTag);
			ctx.register(ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, GlimmeringTales.loc(id)),
					new BiomeModifiers.AddFeaturesBiomeModifier(set,
							HolderSet.direct(features.getOrThrow(placeKey)),
							GenerationStep.Decoration.VEGETAL_DECORATION));
		}

	}

	public static final ResourceKey<ConfiguredFeature<?, ?>> CF_TREE = ResourceKey.create(Registries.CONFIGURED_FEATURE, GlimmeringTales.loc("struck_tree"));
	private static final Placement PF_SPARSE = new Placement("struck_tree_sparse", 32);
	private static final Placement PF_COMMON = new Placement("struck_tree_common", 16);
	private static final Placement PF_DENSE = new Placement("struck_tree_dense", 8);

	public static void genFeatures(DataProviderInitializer init) {
		init.add(Registries.CONFIGURED_FEATURE, ctx -> {
			ctx.register(CF_TREE, new ConfiguredFeature<>(Feature.TREE, new TreeConfiguration.TreeConfigurationBuilder(
					BlockStateProvider.simple(GTItems.STRUCK_LOG.getDefaultState()),
					new StraightTrunkPlacer(5, 2, 0),
					BlockStateProvider.simple(GTItems.STRUCK_LEAVES.getDefaultState()),
					new BlobFoliagePlacer(ConstantInt.of(2), ConstantInt.of(0), 3),
					new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
			).ignoreVines().build()));
		});
		init.add(Registries.PLACED_FEATURE, ctx -> {
			var cf = ctx.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(CF_TREE);
			PF_SPARSE.placed(ctx, cf);
			PF_COMMON.placed(ctx, cf);
			PF_DENSE.placed(ctx, cf);
		});
		init.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, ctx -> {
			PF_SPARSE.biome(ctx);
			PF_COMMON.biome(ctx);
			PF_DENSE.biome(ctx);
		});
	}

	public static void genBiomeTags(RegistrateTagsProvider.Impl<Biome> pvd) {
		pvd.addTag(PF_SPARSE.biomeTag).add(Biomes.PLAINS, Biomes.SNOWY_PLAINS, Biomes.GROVE, Biomes.TAIGA, Biomes.SNOWY_TAIGA);
		pvd.addTag(PF_COMMON.biomeTag).add(Biomes.SWAMP, Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.WINDSWEPT_FOREST);
		pvd.addTag(PF_DENSE.biomeTag).add(Biomes.JUNGLE, Biomes.DARK_FOREST);
	}


}
