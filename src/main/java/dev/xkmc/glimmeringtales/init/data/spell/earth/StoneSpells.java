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
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.List;

public class StoneSpells extends NatureSpellEntry {

	public static final ResourceLocation ID = GlimmeringTales.loc("stone");
	public static final DataGenCachedHolder<SpellAction> SPELL = spell(ID);
	public static final DataGenCachedHolder<NatureSpell> NATURE = nature(ID);

	@Override
	public void regNature(BootstrapContext<NatureSpell> ctx) {
		NATURE.gen(ctx, new NatureSpell(SPELL, GTRegistries.EARTH.get(), 20));
	}

	@Override
	public void regBlock(DataMapProvider.Builder<BlockSpell, Block> builder) {
		builder.add(Tags.Blocks.STONES, new BlockSpell(NATURE, false, 0), false);
		builder.add(GTItems.FAKE_STONE, new BlockSpell(NATURE, false, 0), false);
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		new SpellAction(
				gen(new DataGenContext(ctx)),
				GTItems.RUNE_STONE.asItem(),
				1040,
				SpellCastType.INSTANT,
				SpellTriggerType.TARGET_POS
		).verifyOnBuild(ctx, SPELL);
	}

	@Override
	public void genLang(RegistrateLangProvider ctx) {
		ctx.add(SpellAction.lang(ID), "Stone Extension");
	}

	private static ConfiguredEngine<?> gen(DataGenContext ctx) {
		return new ListLogic(List.of(
				new SetBlock(GTItems.FAKE_STONE.getDefaultState()),
				new ScheduleTick(IntVariable.of("rand(180,220)"), GTItems.FAKE_STONE.get())
		)).circular(
				DoubleVariable.of("3"), DoubleVariable.ZERO, true, null,
				BlockTestCondition.Type.REPLACEABLE.get());
	}


}
