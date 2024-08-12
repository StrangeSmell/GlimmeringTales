package dev.xkmc.glimmeringtales.init.reg;

import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.content.core.spell.SpellElement;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2core.init.reg.datapack.DataMapReg;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class GTRegistries {

	public static final L2Registrate.RegistryInstance<SpellElement> ELEMENT =
			GlimmeringTales.REGISTRATE.newRegistry("element", SpellElement.class);
	public static final ResourceKey<Registry<NatureSpell>> SPELL =
			ResourceKey.createRegistryKey(GlimmeringTales.loc("spell"));
	public static final DataMapReg<Block, BlockSpell> BLOCK =
			GlimmeringTales.REG.dataMap("block_spell", Registries.BLOCK, BlockSpell.class);

	public static final SimpleEntry<SpellElement> LIFE = reg("life", GTItems.CRYSTAL_LIFE::get);
	public static final SimpleEntry<SpellElement> EARTH = reg("earth", GTItems.CRYSTAL_EARTH::get);
	public static final SimpleEntry<SpellElement> FLAME = reg("flame", GTItems.CRYSTAL_FLAME::get);
	public static final SimpleEntry<SpellElement> SNOW = reg("snow", GTItems.CRYSTAL_WINTERSTORM::get);
	//public static final SimpleEntry<SpellElement> SEA = reg("sea", GTItems.CRYSTAL_NATURE::get);
	//public static final SimpleEntry<SpellElement> THUNDER = reg("thunder");

	private static SimpleEntry<SpellElement> reg(String id, Supplier<Item> item) {
		return new SimpleEntry<>(GlimmeringTales.REGISTRATE.generic(ELEMENT, id, SpellElement::new)
				.defaultLang().register());
	}

	public static void register() {
	}

}
