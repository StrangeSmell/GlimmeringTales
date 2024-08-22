package dev.xkmc.glimmeringtales.init.data.spell.earth;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellEntry;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.PredicateLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.logic.RandomVariableLogic;
import dev.xkmc.l2magic.content.engine.modifier.*;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.content.engine.processor.SetDeltaProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxCylinderSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.motion.MovePosMotion;
import dev.xkmc.l2magic.content.particle.engine.*;
import dev.xkmc.l2magic.content.particle.render.SpriteGeom;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.List;
import java.util.function.BiFunction;

public class GravelSpells extends NatureSpellEntry {

	public static final ResourceLocation ID = GlimmeringTales.loc("gravel");
	public static final DataGenCachedHolder<SpellAction> SPELL = spell(ID);
	public static final DataGenCachedHolder<NatureSpell> NATURE = nature(ID);

	@Override
	public void regNature(BootstrapContext<NatureSpell> ctx) {
		NATURE.gen(ctx, new NatureSpell(SPELL, GTRegistries.EARTH.get(), 20));
	}

	@Override
	public void regBlock(DataMapProvider.Builder<BlockSpell, Block> builder) {
		builder.add(Tags.Blocks.GRAVELS, new BlockSpell(NATURE, false, 0), false);
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		new SpellAction(
				gen(new DataGenContext(ctx)),
				GTItems.RUNE_GRAVEL.asItem(),
				1060,
				SpellCastType.INSTANT,
				SpellTriggerType.TARGET_POS
		).verifyOnBuild(ctx, SPELL);
	}

	@Override
	public void genLang(RegistrateLangProvider ctx) {
		ctx.add(SpellAction.lang(ID), "Flint Storm");
	}

	private static ConfiguredEngine<?> gen(DataGenContext ctx) {
		double vsp = 0.2;
		int life = 20;
		double rate = Math.tan(10 * Mth.DEG_TO_RAD);
		double w = vsp * 180 / Math.PI / 2 * 6;
		double ir = 3;
		String radius = "i_radius+TickCount*" + vsp * rate;
		String angle = w / (vsp * rate) + "*(log(" + radius + ")-log(i_radius))";

		var damage = new ProcessorEngine(SelectionType.ENEMY,
				new ApproxCylinderSelector(
						DoubleVariable.of("3"),
						DoubleVariable.of("3")
				),
				List.of(
						new DamageProcessor(
								ctx.damage(DamageTypes.ARROW),
								DoubleVariable.of("4"),
								true, true
						),
						SetDeltaProcessor.ZERO,
						new EffectProcessor(
								LCEffects.ARMOR_REDUCE,
								IntVariable.of("100"),
								IntVariable.of("2"),
								false, false
						)
				));

		BiFunction<ParticleRenderData<?>, Double, CustomParticleInstance> pf = (r, s) -> new CustomParticleInstance(
				DoubleVariable.of("0"),
				DoubleVariable.of("" + s),
				IntVariable.of("" + life),
				true,
				new MovePosMotion(List.of(
						ForwardOffsetModifier.of("-i_radius"),
						RotationModifier.of(angle),
						ForwardOffsetModifier.of(radius),
						new Normal2DirModifier(),
						ForwardOffsetModifier.of("TickCount*" + vsp)
				)), r
		);

		var particle = new PredicateLogic(
				BooleanVariable.of("rand(0,1)>0.05"),
				pf.apply(new DustParticleData(
						RenderTypePreset.NORMAL,
						ColorVariable.Static.of(-8356741)), 0.15),
				pf.apply(new ItemParticleData(
						RenderTypePreset.BLOCK,
						Items.FLINT,
						SpriteGeom.INSTANCE), 0.1)
		);

		var tick = new DelayedIterator(
				IntVariable.of("10"),
				IntVariable.of("1"),
				new RandomVariableLogic("r", 1,
						new RingRandomIterator(
								DoubleVariable.of("0.5"),
								DoubleVariable.of("" + ir),
								DoubleVariable.of("-180"),
								DoubleVariable.of("180"),
								IntVariable.of("6"),
								particle, "i"
						).move(new Dir2NormalModifier())
				), null
		);


		return new ListLogic(List.of(
				new DelayedIterator(IntVariable.of("18"), IntVariable.of("2"), damage, null),
				new DelayedIterator(IntVariable.of("30"), IntVariable.of("1"), tick, null)
		)).move(OffsetModifier.ABOVE);
	}


}
