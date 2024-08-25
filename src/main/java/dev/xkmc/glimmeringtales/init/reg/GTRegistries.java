package dev.xkmc.glimmeringtales.init.reg;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.glimmeringtales.content.capability.PlayerManaCapability;
import dev.xkmc.glimmeringtales.content.core.description.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.*;
import dev.xkmc.glimmeringtales.content.item.wand.RuneSwapType;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2backpack.content.quickswap.type.MatcherSwapType;
import dev.xkmc.l2core.capability.player.PlayerCapabilityNetworkHandler;
import dev.xkmc.l2core.init.reg.datapack.DataMapReg;
import dev.xkmc.l2core.init.reg.registrate.L2Registrate;
import dev.xkmc.l2core.init.reg.registrate.SimpleEntry;
import dev.xkmc.l2core.init.reg.simple.AttReg;
import dev.xkmc.l2core.init.reg.simple.AttVal;
import dev.xkmc.l2damagetracker.init.L2DamageTracker;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.ai.attributes.Attribute;
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
	public static final DataMapReg<Block, BlockReplace> REPLACE =
			GlimmeringTales.REG.dataMap("block_replace", Registries.BLOCK, BlockReplace.class);
	public static final DataMapReg<Block, BlockReplace> MELT =
			GlimmeringTales.REG.dataMap("block_melt", Registries.BLOCK, BlockReplace.class);

	public static final Holder<Attribute> MAX_MANA = reg("max_mana", 400, 1000000, "Max Mana");
	public static final Holder<Attribute> MANA_REGEN = reg("mana_regen", 20, 1000000, "Mana Regen");

	public static final SimpleEntry<SpellElement> LIFE = reg("life", ChatFormatting.GREEN);
	public static final SimpleEntry<SpellElement> EARTH = reg("earth", ChatFormatting.GOLD);
	public static final SimpleEntry<SpellElement> FLAME = reg("flame", ChatFormatting.RED);
	public static final SimpleEntry<SpellElement> SNOW = reg("snow", ChatFormatting.AQUA);
	public static final SimpleEntry<SpellElement> OCEAN = reg("ocean", ChatFormatting.DARK_AQUA);
	public static final SimpleEntry<SpellElement> THUNDER = reg("thunder", ChatFormatting.YELLOW);

	public static final AttReg ATT = AttReg.of(GlimmeringTales.REG);
	public static final AttVal.PlayerVal<PlayerManaCapability> MANA = ATT.player("mana",
			PlayerManaCapability.class, PlayerManaCapability::new, PlayerCapabilityNetworkHandler::new);

	public static final MatcherSwapType SWAP = new RuneSwapType();

	private static SimpleEntry<SpellElement> reg(String id, ChatFormatting color) {
		var attr = reg(id + "_affinity", 0, 1000, RegistrateLangProvider.toEnglishName(id + "_affinity"));
		return new SimpleEntry<>(GlimmeringTales.REGISTRATE.generic(ELEMENT, id, () -> new SpellElement(color, attr))
				.defaultLang().register());
	}

	public static SimpleEntry<Attribute> reg(String id, double def, double max, String name) {
		return L2DamageTracker.regWrapped(GlimmeringTales.REGISTRATE, id, def, 0, max, name);
	}

	public static void register() {
	}

}
