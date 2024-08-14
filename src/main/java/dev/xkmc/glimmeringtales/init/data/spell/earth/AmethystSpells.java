package dev.xkmc.glimmeringtales.init.data.spell.earth;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.content.engine.render.CrossRenderData;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTTagGen;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellEntry;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.content.entity.engine.CustomProjectileShoot;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.Map;

public class AmethystSpells extends NatureSpellEntry {

	public static final ResourceLocation ID = GlimmeringTales.loc("amethyst");
	public static final DataGenCachedHolder<SpellAction> SPELL = spell(ID);
	public static final DataGenCachedHolder<NatureSpell> NATURE = nature(ID);
	public static final DataGenCachedHolder<ProjectileConfig> PROJECTILE = projectile(ID);

	public static final ResourceLocation TEX = GlimmeringTales.loc("textures/spell/amethyst.png");

	@Override
	public void regNature(BootstrapContext<NatureSpell> ctx) {
		NATURE.gen(ctx, new NatureSpell(SPELL, GTRegistries.EARTH.get(), 40));
	}

	@Override
	public void registerProjectile(BootstrapContext<ProjectileConfig> ctx) {
		proj(new DataGenContext(ctx)).verifyOnBuild(ctx, PROJECTILE);
	}

	@Override
	public void regBlock(DataMapProvider.Builder<BlockSpell, Block> builder) {
		builder.add(GTTagGen.AMETHYST, new BlockSpell(NATURE), false);
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		new SpellAction(
				gen(new DataGenContext(ctx)),
				GTItems.RUNE_AMETHYST.asItem(),
				1030,
				SpellCastType.INSTANT,
				SpellTriggerType.TARGET_POS
		).verifyOnBuild(ctx, SPELL);
	}

	@Override
	public void genLang(RegistrateLangProvider ctx) {
		ctx.add(SpellAction.lang(ID), "Scattering Amethyst");
	}

	private static ProjectileConfig proj(DataGenContext ctx) {
		return ProjectileConfig.builder(SelectionType.ENEMY_NO_FAMILY)
				.tick(new DustParticleInstance(
						ColorVariable.Static.of(0xCFA0F3),
						DoubleVariable.of("0.5"),
						DoubleVariable.ZERO,
						IntVariable.of("20")
				).move(ForwardOffsetModifier.of("-0.2")))
				.hit(new DamageProcessor(
						ctx.damage(DamageTypes.ARROW),
						DoubleVariable.of("4"),
						true,
						true
				)).hit(new EffectProcessor(
						LCEffects.ARMOR_REDUCE,
						IntVariable.of("300"),
						IntVariable.of("1"),
						false,
						true
				)).size(DoubleVariable.of("0.25"))
				.motion(SimpleMotion.BREAKING)
				.renderer(new CrossRenderData(TEX))
				.build();
	}

	private static ConfiguredEngine<?> gen(DataGenContext ctx) {
		int phi = 7;
		int theta = 24;
		return new LoopIterator(
				IntVariable.of("" + phi),
				new LoopIterator(
						IntVariable.of("" + theta),
						new CustomProjectileShoot(
								DoubleVariable.of("1"),
								PROJECTILE,
								IntVariable.of("100"),
								false, true,
								Map.of()
						).move(new RotationModifier(
								DoubleVariable.of(360 / theta + "*j"),
								DoubleVariable.of(90 / phi + "*(i+0.5)")
						)), "j"
				), "i"
		).move(
				OffsetModifier.of("0", "0.55", "0"),
				SetDirectionModifier.of("1", "0", "0")
		);
	}

}
