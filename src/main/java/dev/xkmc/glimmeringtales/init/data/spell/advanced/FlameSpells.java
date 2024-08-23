
package dev.xkmc.glimmeringtales.init.data.spell.advanced;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellEntry;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.LinearIterator;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.PredicateLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.logic.RandomVariableLogic;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RandomOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.BlockParticleInstance;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.KnockBackProcessor;
import dev.xkmc.l2magic.content.engine.processor.PropertyProcessor;
import dev.xkmc.l2magic.content.engine.processor.PushProcessor;
import dev.xkmc.l2magic.content.engine.selector.ApproxCylinderSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.List;

public class FlameSpells extends NatureSpellEntry {

	public static final ResourceLocation HM = GlimmeringTales.loc("hell_mark");
	public static final DataGenCachedHolder<SpellAction> HM_SPELL = spell(HM);
	public static final DataGenCachedHolder<NatureSpell> HM_NATURE = nature(HM);

	public static final ResourceLocation LB = GlimmeringTales.loc("lava_burst");
	public static final DataGenCachedHolder<SpellAction> LB_SPELL = spell(LB);
	public static final DataGenCachedHolder<NatureSpell> LB_NATURE = nature(LB);

	@Override
	public void regNature(BootstrapContext<NatureSpell> ctx) {
		HM_NATURE.gen(ctx, new NatureSpell(HM_SPELL, GTRegistries.FLAME.get(), 160));
		LB_NATURE.gen(ctx, new NatureSpell(LB_SPELL, GTRegistries.FLAME.get(), 10, 30));
	}

	@Override
	public void regBlock(DataMapProvider.Builder<BlockSpell, Block> builder) {

	}

	public void genLang(RegistrateLangProvider pvd) {
		pvd.add(SpellAction.lang(HM), "Hell Mark");
		pvd.add(SpellAction.lang(LB), "Lava Burst");
	}

	public void register(BootstrapContext<SpellAction> ctx) {
		new SpellAction(
				flameBurst(new DataGenContext(ctx)),
				Items.FIRE_CHARGE, 200,
				SpellCastType.INSTANT, SpellTriggerType.TARGET_POS
		).verifyOnBuild(ctx, HM_SPELL);

		new SpellAction(
				earthquake(new DataGenContext(ctx)),
				Items.TNT, 300,
				SpellCastType.CHARGE, SpellTriggerType.HORIZONTAL_FACING
		).verifyOnBuild(ctx, LB_SPELL);
	}

	private static ConfiguredEngine<?> flameBurst(DataGenContext ctx) {
		return new ListLogic(List.of(
				star(4, 0.3).move(
						new SetDirectionModifier(
								DoubleVariable.of("1"),
								DoubleVariable.ZERO,
								DoubleVariable.ZERO),
						RotationModifier.of("rand(0,360)")
				),
				new DelayedIterator(
						IntVariable.of("40"),
						IntVariable.of("1"),
						new ListLogic(List.of(
								new ProcessorEngine(SelectionType.ENEMY,
										new ApproxCylinderSelector(
												DoubleVariable.of("4"),
												DoubleVariable.of("6")
										), List.of(
										new DamageProcessor(
												ctx.damage(DamageTypes.IN_FIRE),
												DoubleVariable.of("4"),
												true, false
										),
										new PushProcessor(
												DoubleVariable.of("0.1"),
												DoubleVariable.ZERO,
												DoubleVariable.ZERO,
												PushProcessor.Type.UNIFORM
										),
										new PropertyProcessor(
												PropertyProcessor.Type.IGNITE,
												IntVariable.of("100")
										)
								)),
								new RingRandomIterator(
										DoubleVariable.of("0"),
										DoubleVariable.of("4"),
										DoubleVariable.of("-180"),
										DoubleVariable.of("180"),
										IntVariable.of("10"),
										new RandomVariableLogic(
												"r", 4,
												new PredicateLogic(
														BooleanVariable.of("r2<0.25"),
														new SimpleParticleInstance(
																ParticleTypes.SOUL,
																DoubleVariable.of("0.5+r3*0.2")
														),
														new SimpleParticleInstance(
																ParticleTypes.FLAME,
																DoubleVariable.of("0.5+r3*0.2")
														)
												).move(new SetDirectionModifier(
														DoubleVariable.of("(r0-0.5)*0.2"),
														DoubleVariable.of("1"),
														DoubleVariable.of("(r1-0.5)*0.2")
												))
										), "i"
								))), null
				)
		));
	}

	private static ConfiguredEngine<?> earthquake(DataGenContext ctx) {
		return new PredicateLogic(BooleanVariable.of("Power==0"),
				new RingRandomIterator(
						DoubleVariable.of("0.5"),
						DoubleVariable.of("1"),
						DoubleVariable.of("-180"),
						DoubleVariable.of("180"),
						IntVariable.of("5*min(TickUsing/10,3)"),
						new SimpleParticleInstance(
								ParticleTypes.SMALL_FLAME,
								DoubleVariable.of("0.3")
						).move(RotationModifier.of("135", "rand(-15*min(floor(TickUsing/10),3),0)"),
								ForwardOffsetModifier.of("-4")
						), null
				),
				earthquakeStart(ctx)
		);
	}

	private static ConfiguredEngine<?> earthquakeStart(DataGenContext ctx) {
		return new DelayedIterator(
				IntVariable.of("min(TickUsing/10,3)"),
				IntVariable.of("10"),
				new RandomVariableLogic("r", 2,
						new LoopIterator(
								IntVariable.of("3+i*2"),
								new ListLogic(List.of(
										star(2, 0.2).move(RotationModifier.of("rand(0,360)")),
										new ProcessorEngine(SelectionType.ENEMY,
												new ApproxCylinderSelector(
														DoubleVariable.of("4"),
														DoubleVariable.of("2")
												), List.of(
												new DamageProcessor(ctx.damage(DamageTypes.EXPLOSION),
														DoubleVariable.of("10"), true, true),
												KnockBackProcessor.of("2")
										)),
										new RingRandomIterator(
												DoubleVariable.of("0"),
												DoubleVariable.of("2"),
												DoubleVariable.of("-180"),
												DoubleVariable.of("180"),
												IntVariable.of("100"),
												new BlockParticleInstance(
														Blocks.STONE,
														DoubleVariable.of("0.5+rand(0,0.4)"),
														DoubleVariable.of("0.5"),
														IntVariable.of("rand(20,40)"),
														true
												).move(new SetDirectionModifier(
														DoubleVariable.ZERO,
														DoubleVariable.of("1"),
														DoubleVariable.ZERO)
												), null
										)
								)).move(RotationModifier.of("180/(3+i*2)*(j+(r0+r1)/2)-90"),
										ForwardOffsetModifier.of("6*i+4"),
										new RandomOffsetModifier(
												RandomOffsetModifier.Type.SPHERE,
												DoubleVariable.of("0.1"),
												DoubleVariable.ZERO,
												DoubleVariable.of("0.1")
										)
								).delay(IntVariable.of("abs(i+1-j)*1")), "j"
						)
				), "i"
		);
	}

	private static ConfiguredEngine<?> star(double radius, double step) {
		int linestep = (int) Math.round(1.9 * radius / step);
		int circlestep = (int) Math.round(radius * Math.PI * 2 / step);
		return new ListLogic(List.of(
				new LoopIterator(
						IntVariable.of("5"),
						new LinearIterator(
								DoubleVariable.of(radius * 1.9 / linestep + ""),
								Vec3.ZERO,
								DoubleVariable.ZERO,
								IntVariable.of(linestep + 1 + ""),
								true,
								new SimpleParticleInstance(
										ParticleTypes.FLAME,
										DoubleVariable.ZERO
								),
								null
						).move(
								RotationModifier.of("72*ri"),
								ForwardOffsetModifier.of(radius + ""),
								RotationModifier.of("162")
						), "ri"
				),
				new LoopIterator(
						IntVariable.of(circlestep + ""),
						new SimpleParticleInstance(
								ParticleTypes.FLAME,
								DoubleVariable.ZERO
						).move(RotationModifier.of(360d / circlestep + "*ri"),
								ForwardOffsetModifier.of(radius + "")
						), "ri"
				)
		));
	}

}
