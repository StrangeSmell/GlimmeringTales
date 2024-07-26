package dev.xkmc.glimmeringtales.init.reg;

import com.tterrag.registrate.util.entry.ItemEntry;
import dev.xkmc.glimmeringtales.content.item.materials.DepletedItem;
import dev.xkmc.glimmeringtales.content.item.materials.LightningImmuneItem;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTConfigs;
import dev.xkmc.l2core.init.reg.simple.DCReg;
import dev.xkmc.l2core.init.reg.simple.DCVal;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;

public class GTItems {

	public static final ItemEntry<LightningImmuneItem> CRYSTAL_NATURE;
	public static final ItemEntry<LightningImmuneItem> CRYSTAL_LIFE, CRYSTAL_FLAME, CRYSTAL_EARTH, CRYSTAL_WINTERSTORM;
	public static final ItemEntry<DepletedItem> DEPLETED_FLAME, DEPLETED_WINTERSTORM;

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
	}

	public static void register() {

	}

}
