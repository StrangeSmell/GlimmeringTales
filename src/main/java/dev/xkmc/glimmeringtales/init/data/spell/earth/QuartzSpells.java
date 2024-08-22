package dev.xkmc.glimmeringtales.init.data.spell.earth;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTTagGen;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellEntry;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.block.ScheduleTick;
import dev.xkmc.l2magic.content.engine.block.SetBlock;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.predicate.BlockTestCondition;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.List;

public class QuartzSpells extends NatureSpellEntry {

	public static final ResourceLocation ID = GlimmeringTales.loc("quartz");
	public static final DataGenCachedHolder<SpellAction> SPELL = spell(ID);
	public static final DataGenCachedHolder<NatureSpell> NATURE = nature(ID);

	@Override
	public void regNature(BootstrapContext<NatureSpell> ctx) {
		NATURE.gen(ctx, new NatureSpell(SPELL, GTRegistries.EARTH.get(), 40));
	}

	@Override
	public void regBlock(DataMapProvider.Builder<BlockSpell, Block> builder) {
		builder.add(GTTagGen.QUARTZ, new BlockSpell(NATURE, false, 0), false);
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		new SpellAction(
				gen(new DataGenContext(ctx)),
				GTItems.RUNE_QUARTZ.asItem(),
				1050,
				SpellCastType.INSTANT,
				SpellTriggerType.TARGET_POS
		).verifyOnBuild(ctx, SPELL);
	}

	@Override
	public void genLang(RegistrateLangProvider ctx) {
		ctx.add(SpellAction.lang(ID), "Crystalization");
	}

	private static ConfiguredEngine<?> gen(DataGenContext ctx) {
		return new ListLogic(List.of(
				new SetBlock(GTItems.FAKE_GLASS.getDefaultState()),
				new ScheduleTick(IntVariable.of("rand(180,220)"), GTItems.FAKE_GLASS.get())
		)).circular(
				DoubleVariable.of("4"),
				DoubleVariable.ZERO,
				false, "i",
				BooleanVariable.of("abs(i_r-3)<0.5"),
				BlockTestCondition.Type.REPLACEABLE.get()
		).move(OffsetModifier.ABOVE);
	}

}
