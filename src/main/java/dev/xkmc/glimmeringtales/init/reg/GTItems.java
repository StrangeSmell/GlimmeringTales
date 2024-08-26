package dev.xkmc.glimmeringtales.init.reg;

import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.glimmeringtales.content.block.crop.LifeCrystalCrop;
import dev.xkmc.glimmeringtales.content.block.misc.*;
import dev.xkmc.glimmeringtales.content.block.ritual.NatureCoreBlockEntity;
import dev.xkmc.glimmeringtales.content.block.ritual.NatureSideBlockEntity;
import dev.xkmc.glimmeringtales.content.block.ritual.RitualBlock;
import dev.xkmc.glimmeringtales.content.block.ritual.RitualRenderer;
import dev.xkmc.glimmeringtales.content.item.materials.AmethystCompass;
import dev.xkmc.glimmeringtales.content.item.materials.DepletedItem;
import dev.xkmc.glimmeringtales.content.item.rune.BlockRuneItem;
import dev.xkmc.glimmeringtales.content.item.rune.SpellCoreItem;
import dev.xkmc.glimmeringtales.content.item.rune.SpellRuneItem;
import dev.xkmc.glimmeringtales.content.item.wand.GTBEWLR;
import dev.xkmc.glimmeringtales.content.item.wand.IWandCoreItem;
import dev.xkmc.glimmeringtales.content.item.wand.RuneWandItem;
import dev.xkmc.glimmeringtales.content.item.wand.WandHandleItem;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTConfigs;
import dev.xkmc.glimmeringtales.init.data.GTTagGen;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.DCReg;
import dev.xkmc.l2core.init.reg.simple.DCVal;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.generators.ModelFile;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class GTItems {

	public static final SimpleEntry<CreativeModeTab> TAB = GlimmeringTales.REGISTRATE
			.buildL2CreativeTab("glimmeringtales", "Glimmering Tales", e ->
					e.icon(GTItems.CRYSTAL_EARTH::asStack));

	public static final List<ItemEntry<? extends IWandCoreItem>> CORES = new ArrayList<>();
	public static final List<ItemEntry<? extends WandHandleItem>> HANDLES = new ArrayList<>();

	public static final ItemEntry<SpellCoreItem> CRYSTAL_NATURE,
			CRYSTAL_LIFE, CRYSTAL_FLAME, CRYSTAL_EARTH, CRYSTAL_WINTERSTORM,
			CRYSTAL_OCEAN, CRYSTAL_THUNDER;
	public static final ItemEntry<DepletedItem> DEPLETED_FLAME, DEPLETED_WINTERSTORM;
	public static final BlockEntry<LifeCrystalCrop> CRYSTAL_VINE;
	public static final BlockEntry<StruckLogBlock> STRUCK_LOG;
	public static final ItemEntry<AmethystCompass> COMPASS;

	public static final BlockEntry<DelegateBlock> RITUAL_ALTAR, RITUAL_MATRIX;
	public static final BlockEntityEntry<NatureSideBlockEntity> ALTAR_BE;
	public static final BlockEntityEntry<NatureCoreBlockEntity> MATRIX_BE;

	public static final ItemEntry<RuneWandItem> WAND;
	public static final ItemEntry<WandHandleItem> WOOD_WAND, GOLD_WAND;

	public static final ItemEntry<BlockRuneItem>
			RUNE_BAMBOO, RUNE_CACTUS, RUNE_FLOWER, RUNE_VINE, RUNE_HAYBALE,
			RUNE_SAND, RUNE_GRAVEL, RUNE_QUARTZ, RUNE_CLAY, RUNE_STONE, RUNE_DRIPSTONE, RUNE_AMETHYST,
			RUNE_MAGMA, RUNE_SOUL_SAND, RUNE_SNOW, RUNE_ICE, RUNE_PACKED_ICE, RUNE_BLUE_ICE, RUNE_POWDER_SNOW,
			RUNE_THUNDER;

	public static final ItemEntry<SpellRuneItem>
			HELL_MARK, LAVA_BURST, WINTER_STORM, SNOW_TORNADO;

	public static final BlockEntry<DelegateBlock> CLAY_CARPET, FAKE_STONE,
			MAGMA_STONE, MAGMA_DEEPSLATE, MAGMA_NETHERRACK;
	public static final BlockEntry<SelfDestroyTransparent> FAKE_GLASS;
	public static final BlockEntry<DelegateBlock> FAKE_BAMBOO;

	private static final DCReg DC = DCReg.of(GlimmeringTales.REG);

	public static final DCVal<Integer> PROGRESS = DC.intVal("progress");
	public static final DCVal<Holder<Item>> WAND_HANDLE = DC.registry("handle", BuiltInRegistries.ITEM);

	static {

		{
			CRYSTAL_NATURE = core("crystal_of_nature");
			CRYSTAL_EARTH = core("crystal_of_earth");
			CRYSTAL_LIFE = core("crystal_of_life");
			CRYSTAL_FLAME = core("crystal_of_flame");
			CRYSTAL_WINTERSTORM = core("crystal_of_winterstorm");
			CRYSTAL_OCEAN = core("crystal_of_ocean");
			CRYSTAL_THUNDER = core("crystal_of_thunder");
			DEPLETED_FLAME = GlimmeringTales.REGISTRATE.item("depleted_crystal_of_flame", p ->
					new DepletedItem(p, () -> Blocks.LAVA, GTConfigs.SERVER.crystalOfFlameRequirement,
							CRYSTAL_FLAME::get, () -> SoundEvents.BUCKET_FILL_LAVA)
			).register();
			DEPLETED_WINTERSTORM = GlimmeringTales.REGISTRATE.item("depleted_crystal_of_winterstorm", p ->
					new DepletedItem(p, () -> Blocks.POWDER_SNOW, GTConfigs.SERVER.crystalOfWinterstormRequirement,
							CRYSTAL_WINTERSTORM::get, () -> SoundEvents.BUCKET_FILL_POWDER_SNOW)
			).register();

			CRYSTAL_VINE = GlimmeringTales.REGISTRATE.block("crystal_vine", LifeCrystalCrop::new)
					.properties(p -> p.mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak()
							.sound(SoundType.CROP).pushReaction(PushReaction.DESTROY))
					.item(ItemNameBlockItem::new).model((ctx, pvd) -> pvd.generated(ctx))
					.lang("Seed of Nature").build()
					.blockstate(LifeCrystalCrop::buildState)
					.loot(LifeCrystalCrop::builtLoot)
					.tag(BlockTags.CROPS)
					.register();

			STRUCK_LOG = GlimmeringTales.REGISTRATE.block("struck_log", p ->
							new StruckLogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)))
					.blockstate((ctx, pvd) -> pvd.logBlock(ctx.get()))
					.tag(BlockTags.LOGS_THAT_BURN).simpleItem().register();

			COMPASS = GlimmeringTales.REGISTRATE.item("amethyst_compass", p ->
							new AmethystCompass(p.stacksTo(1)))
					.register();

			RITUAL_ALTAR = GlimmeringTales.REGISTRATE.block("ritual_altar", p ->
							DelegateBlock.newBaseBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)
									.noOcclusion(), RitualBlock.ITEM, RitualBlock.LINK, RitualBlock.SIDE))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().getBuilder("block/" + ctx.getName())
							.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/ritual_altar")))
							.texture("all", "block/" + ctx.getName())
							.renderType("cutout")))
					.simpleItem()
					.register();

			ALTAR_BE = GlimmeringTales.REGISTRATE.blockEntity("ritual_altar", NatureSideBlockEntity::new)
					.validBlock(RITUAL_ALTAR)
					.renderer(() -> RitualRenderer::new)
					.register();

			RITUAL_MATRIX = GlimmeringTales.REGISTRATE.block("ritual_matrix", p ->
							DelegateBlock.newBaseBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)
									.noOcclusion(), RitualBlock.ITEM, RitualBlock.LINK, RitualBlock.START, RitualBlock.CORE))
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().getBuilder("block/" + ctx.getName())
							.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/ritual_altar")))
							.texture("all", "block/" + ctx.getName())
							.renderType("cutout")))
					.simpleItem()
					.register();

			MATRIX_BE = GlimmeringTales.REGISTRATE.blockEntity("ritual_matrix", NatureCoreBlockEntity::new)
					.validBlock(RITUAL_MATRIX)
					.renderer(() -> RitualRenderer::new)
					.register();
		}

		{
			WAND = GlimmeringTales.REGISTRATE.item("wand",
							p -> new RuneWandItem(p.stacksTo(1).fireResistant()))
					.model((ctx, pvd) -> pvd.getBuilder(ctx.getName())
							.parent(new ModelFile.UncheckedModelFile(pvd.modLoc("custom/wand")))
							.guiLight(BlockModel.GuiLight.FRONT)
							.texture("particle", "minecraft:item/stick"))
					.clientExtension(() -> () -> GTBEWLR.EXTENSIONS)
					.tab(TAB.key(), (m, x) -> m.get().fillCreativeTabs(x))
					.lang(" Wand")
					.register();

			WOOD_WAND = handle("wood_wand", 0.25f, 0.75f, "Wooden");
			GOLD_WAND = handle("gold_wand", 0.25f, 0.75f, "Golden");

		}

		{

			RUNE_BAMBOO = rune("bamboo", () -> Blocks.BAMBOO, "Rune: Bamboo");
			RUNE_CACTUS = rune("cactus", () -> Blocks.CACTUS, "Rune: Cactus");
			RUNE_FLOWER = rune("flower", () -> Blocks.POPPY, "Rune: Flower");
			RUNE_VINE = rune("vine", () -> Blocks.VINE, "Rune: Vine");
			RUNE_HAYBALE = rune("hay_bale", () -> Blocks.HAY_BLOCK, "Rune: Hay Bale");

			RUNE_SAND = rune("sand", () -> Blocks.SAND, "Rune: Sand");
			RUNE_GRAVEL = rune("gravel", () -> Blocks.GRAVEL, "Rune: Gravel");
			RUNE_CLAY = rune("clay", () -> Blocks.CLAY, "Rune: Clay");
			RUNE_STONE = rune("stone", () -> Blocks.STONE, "Rune: Stone");
			RUNE_QUARTZ = rune("quartz", () -> Blocks.QUARTZ_BLOCK, "Rune: Quartz");
			RUNE_DRIPSTONE = rune("dripstone", () -> Blocks.DRIPSTONE_BLOCK, "Rune: Stalactite");
			RUNE_AMETHYST = rune("amethyst", () -> Blocks.AMETHYST_BLOCK, "Rune: Amethyst");

			RUNE_MAGMA = rune("magma", () -> Blocks.MAGMA_BLOCK, "Rune: Magma Block");
			RUNE_SOUL_SAND = rune("soul_sand", () -> Blocks.SOUL_SAND, "Rune: Soul Sand");

			RUNE_SNOW = rune("snow", () -> Blocks.SNOW_BLOCK, "Rune: Snow");
			RUNE_POWDER_SNOW = rune("powder_snow", () -> Blocks.POWDER_SNOW, "Rune: Powder Snow");
			RUNE_ICE = rune("ice", () -> Blocks.ICE, "Rune: Ice");
			RUNE_PACKED_ICE = rune("packed_ice", () -> Blocks.PACKED_ICE, "Rune: Packed Ice");
			RUNE_BLUE_ICE = rune("blue_ice", () -> Blocks.BLUE_ICE, "Rune: Blue Ice");

			RUNE_THUNDER = rune("thunder", STRUCK_LOG::get, "Rune: Thunder");

			HELL_MARK = spell("hell_mark", "Rune: Hell Mark");
			LAVA_BURST = spell("lava_burst", "Rune: Lava Burst");
			WINTER_STORM = spell("winter_storm", "Rune: Winter Storm");
			SNOW_TORNADO = spell("snow_tornado", "Rune: Snow Tornado");
		}

		{
			CLAY_CARPET = GlimmeringTales.REGISTRATE.block("clay_carpet",
							p -> DelegateBlock.newBaseBlock(p, new SelfDestroyImpl(), new ClayCarpetImpl(),
									new StuckEntityMethod(new Vec3(0.05, 1f, 0.05))))
					.properties(p -> p.mapColor(MapColor.CLAY).strength(0.6f).speedFactor(0.2F).jumpFactor(0.2f)
							.sound(SoundType.GRAVEL).pushReaction(PushReaction.DESTROY).noLootTable())
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().carpet(ctx.getName(), pvd.mcLoc("block/clay"))))
					.register();

			FAKE_STONE = GlimmeringTales.REGISTRATE.block("stone",
							p -> DelegateBlock.newBaseBlock(p, new SelfDestroyImpl()))
					.properties(p -> BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).noLootTable())
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().cubeAll(ctx.getName(), pvd.mcLoc("block/stone"))))
					.register();

			FAKE_GLASS = GlimmeringTales.REGISTRATE.block("glass", SelfDestroyTransparent::new)
					.properties(p -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noLootTable())
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(),
							pvd.models().withExistingParent(ctx.getName(), pvd.mcLoc("block/glass")).renderType("cutout")))
					.register();

			FAKE_BAMBOO = GlimmeringTales.REGISTRATE.block("bamboo",
							p -> DelegateBlock.newBaseBlock(p, new SelfDestroyImpl(), new BamBooImpl()))
					.properties(p -> BlockBehaviour.Properties.of()
							.mapColor(MapColor.PLANT).forceSolidOn().randomTicks().instabreak()
							.strength(1.0F).sound(SoundType.BAMBOO).noOcclusion()
							.dynamicShape().offsetType(BlockBehaviour.OffsetType.XZ)
							.ignitedByLava().pushReaction(PushReaction.DESTROY).noLootTable())
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(),
							pvd.models().withExistingParent(ctx.getName(), pvd.mcLoc("block/bamboo1_age1")).renderType("cutout")))
					.register();

			MAGMA_STONE = magma("stone");
			MAGMA_DEEPSLATE = magma("deepslate");
			MAGMA_NETHERRACK = magma("netherrack");

		}

	}

	private static ItemEntry<SpellCoreItem> core(String id) {
		var ans = GlimmeringTales.REGISTRATE.item(id, SpellCoreItem::new)
				.model((ctx, pvd) -> {
					pvd.generated(ctx, pvd.modLoc("item/crystal/" + ctx.getName()));
					pvd.getBuilder(ctx.getName() + "_core").parent(
									new ModelFile.UncheckedModelFile(pvd.modLoc("custom/rune_core")))
							.texture("all", pvd.modLoc("item/core/" + ctx.getName()))
							.renderType("cutout");
				})
				.tag(GTTagGen.CRYSTAL)
				.register();
		CORES.add(ans);
		return ans;
	}

	private static ItemEntry<BlockRuneItem> rune(String id, Supplier<Block> block, String name) {
		var ans = GlimmeringTales.REGISTRATE.item(id, p ->
						new BlockRuneItem(p.stacksTo(1).fireResistant(), block))
				.model((ctx, pvd) -> {
					pvd.generated(ctx, pvd.modLoc("item/rune/" + ctx.getName()));
					pvd.getBuilder(ctx.getName() + "_core").parent(
									new ModelFile.UncheckedModelFile(pvd.modLoc("custom/rune_core")))
							.texture("all", pvd.modLoc("item/rune/" + ctx.getName()))
							.renderType("cutout");
				})
				.tag(GTTagGen.CORE)
				.lang(name).register();
		CORES.add(ans);
		return ans;
	}

	private static ItemEntry<SpellRuneItem> spell(String id, String name) {
		var ans = GlimmeringTales.REGISTRATE.item(id, p ->
						new SpellRuneItem(p.stacksTo(1).fireResistant(), GlimmeringTales.loc(id)))
				.model((ctx, pvd) -> {
					pvd.generated(ctx, pvd.modLoc("item/spell/" + ctx.getName()));
					pvd.getBuilder(ctx.getName() + "_core").parent(
									new ModelFile.UncheckedModelFile(pvd.modLoc("custom/rune_core")))
							.texture("all", pvd.modLoc("item/spell/" + ctx.getName()))
							.renderType("cutout");
				})
				.tag(GTTagGen.CORE)
				.lang(name).register();
		CORES.add(ans);
		return ans;
	}

	private static ItemEntry<WandHandleItem> handle(String id, float size, float offset, String name) {
		var ans = GlimmeringTales.REGISTRATE.item(id,
						p -> new WandHandleItem(p.stacksTo(1), size, offset))
				.model((ctx, pvd) -> {
					pvd.handheld(ctx, pvd.modLoc("item/handle/" + id));
					pvd.getBuilder(ctx.getName() + "_icon").parent(
									new ModelFile.UncheckedModelFile(pvd.mcLoc("item/handheld")))
							.texture("layer0", pvd.modLoc("item/handle/" + id));
					pvd.getBuilder(ctx.getName() + "_handle").parent(
									new ModelFile.UncheckedModelFile(pvd.modLoc("custom/" + ctx.getName())))
							.texture("all", pvd.modLoc("item/wand/" + ctx.getName()))
							.renderType("cutout");
				}).removeTab(TAB.key()).lang(name)
				.register();
		HANDLES.add(ans);
		return ans;
	}

	private static BlockEntry<DelegateBlock> magma(String id) {
		return GlimmeringTales.REGISTRATE.block("magma_block_for_" + id,
						p -> DelegateBlock.newBaseBlock(p, new MagmaImpl(), new SelfReplaceImpl()))
				.properties(p -> BlockBehaviour.Properties.ofFullCopy(Blocks.MAGMA_BLOCK).noLootTable())
				.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().cubeAll(ctx.getName(), pvd.mcLoc("block/magma"))))
				.tag(GTTagGen.FAKE_MAGMA)
				.lang("Magma Block")
				.register();
	}

	public static void register() {

	}

}
