package dev.xkmc.glimmeringtales.init.reg;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.glimmeringtales.content.block.crop.LifeCrystalCrop;
import dev.xkmc.glimmeringtales.content.block.misc.ClayCarpetImpl;
import dev.xkmc.glimmeringtales.content.block.misc.SelfDestroyImpl;
import dev.xkmc.glimmeringtales.content.item.materials.DepletedItem;
import dev.xkmc.glimmeringtales.content.item.materials.SpellCoreItem;
import dev.xkmc.glimmeringtales.content.item.rune.RuneItem;
import dev.xkmc.glimmeringtales.content.item.wand.RuneWandItem;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTConfigs;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.DCReg;
import dev.xkmc.l2core.init.reg.simple.DCVal;
import dev.xkmc.l2modularblock.core.DelegateBlock;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.function.Supplier;

public class GTItems {

	public static final SimpleEntry<CreativeModeTab> TAB = GlimmeringTales.REGISTRATE
			.buildL2CreativeTab("glimmeringtales", "Glimmering Tales", e ->
					e.icon(GTItems.CRYSTAL_NATURE::asStack));

	public static final ItemEntry<RuneWandItem> WAND;

	public static final ItemEntry<SpellCoreItem> CRYSTAL_NATURE;
	public static final ItemEntry<SpellCoreItem> CRYSTAL_LIFE, CRYSTAL_FLAME, CRYSTAL_EARTH, CRYSTAL_WINTERSTORM;
	public static final ItemEntry<DepletedItem> DEPLETED_FLAME, DEPLETED_WINTERSTORM;
	public static final BlockEntry<LifeCrystalCrop> CRYSTAL_VINE;

	public static final ItemEntry<RuneItem> RUNE_CLAY, RUNE_DRIPSTONE, RUNE_AMETHYST;

	public static final BlockEntry<DelegateBlock> CLAY_CARPET;

	private static final DCReg DC = DCReg.of(GlimmeringTales.REG);

	public static final DCVal<Integer> PROGRESS = DC.intVal("progress");

	static {

		{
			CRYSTAL_NATURE = GlimmeringTales.REGISTRATE.item("crystal_of_nature", SpellCoreItem::new).register();
			CRYSTAL_EARTH = GlimmeringTales.REGISTRATE.item("crystal_of_earth", SpellCoreItem::new).register();
			CRYSTAL_LIFE = GlimmeringTales.REGISTRATE.item("crystal_of_life", SpellCoreItem::new).register();
			CRYSTAL_FLAME = GlimmeringTales.REGISTRATE.item("crystal_of_flame", SpellCoreItem::new).register();
			CRYSTAL_WINTERSTORM = GlimmeringTales.REGISTRATE.item("crystal_of_winterstorm", SpellCoreItem::new).register();
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
					.register();
		}

		{
			RUNE_CLAY = rune("clay", () -> Blocks.CLAY, "Rune: Clay");
			RUNE_DRIPSTONE = rune("dripstone", () -> Blocks.DRIPSTONE_BLOCK, "Rune: Stalactite");
			RUNE_AMETHYST = rune("amethyst", () -> Blocks.AMETHYST_BLOCK, "Rune: Amethyst");
		}

		{
			CLAY_CARPET = GlimmeringTales.REGISTRATE.block("clay_carpet",
							p -> DelegateBlock.newBaseBlock(p, new SelfDestroyImpl(), new ClayCarpetImpl()))
					.properties(p -> p.mapColor(MapColor.CLAY).strength(0.6f).speedFactor(0.05F)
							.sound(SoundType.GRAVEL).pushReaction(PushReaction.DESTROY).noLootTable())
					.blockstate((ctx, pvd) -> pvd.simpleBlock(ctx.get(), pvd.models().carpet(ctx.getName(), pvd.mcLoc("block/clay"))))
					.register();
		}

	}

	private static ItemEntry<RuneItem> rune(String id, Supplier<Block> block, String name) {
		return GlimmeringTales.REGISTRATE.item(id, p -> new RuneItem(p.stacksTo(1).fireResistant(), block))
				.model((ctx, pvd) -> pvd.generated(ctx, pvd.modLoc("item/rune/" + ctx.getName())))
				.lang(name).register();
	}

	public static void register() {

	}

}
