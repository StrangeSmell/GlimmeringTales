package dev.xkmc.glimmeringtales.init.reg;

import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.glimmeringtales.content.block.crop.LifeCrystalCrop;
import dev.xkmc.glimmeringtales.content.item.materials.DepletedItem;
import dev.xkmc.glimmeringtales.content.item.materials.LightningImmuneItem;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTConfigs;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.DCReg;
import dev.xkmc.l2core.init.reg.simple.DCVal;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.common.Tags;

public class GTItems {

	public static final SimpleEntry<CreativeModeTab> TAB = GlimmeringTales.REGISTRATE
			.buildL2CreativeTab("glimmeringtales", "Glimmering Tales", e ->
					e.icon(GTItems.CRYSTAL_NATURE::asStack));

	public static final ItemEntry<LightningImmuneItem> CRYSTAL_NATURE;
	public static final ItemEntry<LightningImmuneItem> CRYSTAL_LIFE, CRYSTAL_FLAME, CRYSTAL_EARTH, CRYSTAL_WINTERSTORM;
	public static final ItemEntry<DepletedItem> DEPLETED_FLAME, DEPLETED_WINTERSTORM;
	public static final BlockEntry<LifeCrystalCrop> CRYSTAL_VINE;

	private static final DCReg DC = DCReg.of(GlimmeringTales.REG);

	public static final DCVal<Integer> PROGRESS = DC.intVal("progress");

	static {
		CRYSTAL_NATURE = GlimmeringTales.REGISTRATE.item("crystal_of_nature", LightningImmuneItem::new).register();
		CRYSTAL_EARTH = GlimmeringTales.REGISTRATE.item("crystal_of_earth", LightningImmuneItem::new).register();
		CRYSTAL_LIFE = GlimmeringTales.REGISTRATE.item("crystal_of_life", LightningImmuneItem::new).register();
		CRYSTAL_FLAME = GlimmeringTales.REGISTRATE.item("crystal_of_flame", LightningImmuneItem::new).register();
		CRYSTAL_WINTERSTORM = GlimmeringTales.REGISTRATE.item("crystal_of_winterstorm", LightningImmuneItem::new).register();
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
				.lang("Seed of Nature").tag(Tags.Items.SEEDS).build()
				.blockstate(LifeCrystalCrop::buildState)
				.loot(LifeCrystalCrop::builtLoot)
				.tag(BlockTags.CROPS)
				.register();
	}

	public static void register() {

	}

}
