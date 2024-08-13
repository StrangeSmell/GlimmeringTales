package dev.xkmc.glimmeringtales.init.reg;

import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.ElementAffinity;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.content.core.spell.SpellElement;
import dev.xkmc.glimmeringtales.content.item.wand.RuneSwapType;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2backpack.content.quickswap.type.MatcherSwapType;
import dev.xkmc.l2core.init.reg.datapack.DataMapReg;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class GTRegistries {

	public static final L2Registrate.RegistryInstance<SpellElement> ELEMENT =
			GlimmeringTales.REGISTRATE.newRegistry("element", SpellElement.class);
	public static final ResourceKey<Registry<NatureSpell>> SPELL =
			ResourceKey.createRegistryKey(GlimmeringTales.loc("spell"));
	public static final DataMapReg<Block, BlockSpell> BLOCK =
			GlimmeringTales.REG.dataMap("block_spell", Registries.BLOCK, BlockSpell.class);
	public static final DataMapReg<Item, ElementAffinity> AFFINITY =
			GlimmeringTales.REG.dataMap("element_affinity", Registries.ITEM, ElementAffinity.class);

	public static final SimpleEntry<SpellElement> LIFE = reg("life", ChatFormatting.GREEN);
	public static final SimpleEntry<SpellElement> EARTH = reg("earth", ChatFormatting.GOLD);
	public static final SimpleEntry<SpellElement> FLAME = reg("flame", ChatFormatting.RED);
	public static final SimpleEntry<SpellElement> SNOW = reg("snow", ChatFormatting.AQUA);
	//public static final SimpleEntry<SpellElement> SEA = reg("sea", GTItems.CRYSTAL_NATURE::get);
	//public static final SimpleEntry<SpellElement> THUNDER = reg("thunder");

	public static final MatcherSwapType SWAP = new RuneSwapType();

	private static SimpleEntry<SpellElement> reg(String id, ChatFormatting color) {
		return new SimpleEntry<>(GlimmeringTales.REGISTRATE.generic(ELEMENT, id, () -> new SpellElement(color))
				.defaultLang().register());
	}

	public static void register() {
	}

}
