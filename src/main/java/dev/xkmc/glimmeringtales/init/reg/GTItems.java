package dev.xkmc.glimmeringtales.init.reg;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.glimmeringtales.content.block.crop.LifeCrystalCrop;
import dev.xkmc.glimmeringtales.content.block.misc.ClayCarpetImpl;
import dev.xkmc.glimmeringtales.content.block.misc.SelfDestroyImpl;
import dev.xkmc.glimmeringtales.content.block.misc.SelfDestroyTransparent;
import dev.xkmc.glimmeringtales.content.block.misc.StuckEntityMethod;
import dev.xkmc.glimmeringtales.content.item.materials.DepletedItem;
import dev.xkmc.glimmeringtales.content.item.rune.BlockRuneItem;
import dev.xkmc.glimmeringtales.content.item.rune.SpellCoreItem;
import dev.xkmc.glimmeringtales.content.item.wand.GTBEWLR;
import dev.xkmc.glimmeringtales.content.item.wand.IWandCoreItem;
import dev.xkmc.glimmeringtales.content.item.wand.RuneWandItem;
import dev.xkmc.glimmeringtales.content.item.wand.WandHandleItem;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTConfigs;
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

	public static final ItemEntry<RuneWandItem> WAND;

	public static final ItemEntry<SpellCoreItem> CRYSTAL_NATURE,
			CRYSTAL_LIFE, CRYSTAL_FLAME, CRYSTAL_EARTH, CRYSTAL_WINTERSTORM,
			CRYSTAL_OCEAN, CRYSTAL_THUNDER;
	public static final ItemEntry<DepletedItem> DEPLETED_FLAME, DEPLETED_WINTERSTORM;
	public static final BlockEntry<LifeCrystalCrop> CRYSTAL_VINE;

	public static final ItemEntry<WandHandleItem> WOOD_WAND, GOLD_WAND;

	public static final ItemEntry<BlockRuneItem> RUNE_BAMBOO, RUNE_CACTUS, RUNE_FLOWER, RUNE_VINE,
			RUNE_SAND, RUNE_GRAVEL, RUNE_QUARTZ, RUNE_CLAY, RUNE_DRIPSTONE, RUNE_AMETHYST,
			RUNE_LAVA, RUNE_SOUL_SAND, RUNE_SNOW, RUNE_ICE, RUNE_POWDER_SNOW;

	public static final BlockEntry<DelegateBlock> CLAY_CARPET;
	public static final BlockEntry<SelfDestroyTransparent> FAKE_GLASS;

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
					.register();

			WOOD_WAND = handle("wood_wand", 0.25f, 0.75f);
			GOLD_WAND = handle("gold_wand", 0.4f, 0.8f);

		}

		{

			RUNE_BAMBOO = rune("bamboo", () -> Blocks.BAMBOO, "Rune: Bamboo");
			RUNE_CACTUS = rune("cactus", () -> Blocks.CACTUS, "Rune: Cactus");
			RUNE_FLOWER = rune("flower", () -> Blocks.POPPY, "Rune: Flower");
			RUNE_VINE = rune("vine", () -> Blocks.VINE, "Rune: Vine");

			RUNE_SAND = rune("sand", () -> Blocks.SAND, "Rune: Sand");
			RUNE_GRAVEL = rune("gravel", () -> Blocks.GRAVEL, "Rune: Gravel");
			RUNE_CLAY = rune("clay", () -> Blocks.CLAY, "Rune: Clay");
			RUNE_QUARTZ = rune("quartz", () -> Blocks.QUARTZ_BLOCK, "Rune: Quartz");
			RUNE_DRIPSTONE = rune("dripstone", () -> Blocks.DRIPSTONE_BLOCK, "Rune: Stalactite");
			RUNE_AMETHYST = rune("amethyst", () -> Blocks.AMETHYST_BLOCK, "Rune: Amethyst");

			RUNE_LAVA = rune("lava", () -> Blocks.LAVA, "Rune: Lava");
			RUNE_SOUL_SAND = rune("soul_sand", () -> Blocks.SOUL_SAND, "Rune: Soul Sand");

			RUNE_SNOW = rune("snow", () -> Blocks.SNOW_BLOCK, "Rune: Snow");
			RUNE_ICE = rune("ice", () -> Blocks.ICE, "Rune: Ice");
			RUNE_POWDER_SNOW = rune("powder_snow", () -> Blocks.POWDER_SNOW, "Rune: Powder Snow");
		}

		{
			CLAY_CARPET = GlimmeringTales.REGISTRATE.block("clay_carpet",
							p -> DelegateBlock.newBaseBlock(p, new SelfDestroyImpl(), new ClayCarpetImpl(),
									new StuckEntityMethod(new Vec3(0.05, 1f, 0.05))))
					.properties(p -> p.mapColor(MapColor.CLAY).strength(0.6f).speedFactor(0.2F).jumpFactor(0.2f)
							.sound(SoundType.GRAVEL).pushReaction(PushReaction.DESTROY).noLootTable())
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().carpet(ctx.getName(), pvd.mcLoc("block/clay"))))
					.register();

			FAKE_GLASS = GlimmeringTales.REGISTRATE.block("glass", SelfDestroyTransparent::new)
					.properties(p -> BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).noLootTable())
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models()
							.withExistingParent(ctx.getName(), pvd.mcLoc("block/glass")).renderType("cutout")))
					.register();

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
				.lang(name).register();
		CORES.add(ans);
		return ans;
	}

	private static ItemEntry<WandHandleItem> handle(String id, float size, float offset) {
		var ans = GlimmeringTales.REGISTRATE.item(id,
						p -> new WandHandleItem(p.stacksTo(1), size, offset))
				.model((ctx, pvd) -> {
					pvd.handheld(ctx, pvd.modLoc("item/wand"));//TODO
					pvd.getBuilder(ctx.getName() + "_handle").parent(
									new ModelFile.UncheckedModelFile(pvd.modLoc("custom/" + ctx.getName())))
							.texture("all", pvd.modLoc("item/wand/" + ctx.getName()))
							.renderType("cutout");
				})
				.register();
		HANDLES.add(ans);
		return ans;
	}

	public static void register() {

	}

}
