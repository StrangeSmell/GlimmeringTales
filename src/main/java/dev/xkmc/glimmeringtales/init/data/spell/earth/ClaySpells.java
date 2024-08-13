package dev.xkmc.glimmeringtales.init.data.spell.earth;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellEntry;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.block.ScheduleTick;
import dev.xkmc.l2magic.content.engine.block.SetBlock;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.predicate.BlockTestCondition;
import dev.xkmc.l2magic.content.engine.predicate.SurfaceBelowCondition;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.List;

public class ClaySpells extends NatureSpellEntry {

	public static final ResourceLocation ID = GlimmeringTales.loc("clay");
	public static final DataGenCachedHolder<SpellAction> SPELL = spell(ID);
	public static final DataGenCachedHolder<NatureSpell> NATURE = nature(ID);

	@Override
	public void regNature(BootstrapContext<NatureSpell> ctx) {
		NATURE.gen(ctx, new NatureSpell(SPELL, GTRegistries.EARTH.get(), 20));
	}

	@Override
	public void regBlock(DataMapProvider.Builder<BlockSpell, Block> builder) {
		builder.add(Blocks.CLAY.builtInRegistryHolder(), new BlockSpell(NATURE), false);
		builder.add(GTItems.CLAY_CARPET, new BlockSpell(NATURE), false);
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		var spell = new SpellAction(
				gen(new DataGenContext(ctx)),
				GTItems.RUNE_CLAY.asItem(),
				1020,
				SpellCastType.INSTANT,
				SpellTriggerType.TARGET_POS
		);
		spell.verify(SPELL.key.location());
		SPELL.gen(ctx, spell);
	}

	@Override
	public void genLang(RegistrateLangProvider ctx) {
		ctx.add(SpellAction.lang(ID), "Clay Overflow");
	}

	private static ConfiguredEngine<?> gen(DataGenContext ctx) {
		return new ListLogic(List.of(
				new SetBlock(GTItems.CLAY_CARPET.getDefaultState()),
				new ScheduleTick(IntVariable.of("rand(80,120)"), GTItems.CLAY_CARPET.get())
		)).circular(
				DoubleVariable.of("4"),
				DoubleVariable.of("2"),
				false, null,
				SurfaceBelowCondition.full(),
				BlockTestCondition.Type.REPLACEABLE.get()
		);
	}

}
