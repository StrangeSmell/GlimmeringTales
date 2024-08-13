package dev.xkmc.glimmeringtales.init.data.spell.earth;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.content.engine.render.VerticalRenderData;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellEntry;
import dev.xkmc.glimmeringtales.init.reg.GTItems;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.predicate.BlockMatchCondition;
import dev.xkmc.l2magic.content.engine.predicate.BlockTestCondition;
import dev.xkmc.l2magic.content.engine.predicate.OrPredicate;
import dev.xkmc.l2magic.content.engine.predicate.SurfaceBelowCondition;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.EffectProcessor;
import dev.xkmc.l2magic.content.engine.processor.PushProcessor;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.content.entity.engine.CustomProjectileShoot;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.List;
import java.util.Map;

public class DripstoneSpells extends NatureSpellEntry {

	public static final ResourceLocation ID = GlimmeringTales.loc("dripstone");
	public static final DataGenCachedHolder<SpellAction> SPELL = spell(ID);
	public static final DataGenCachedHolder<NatureSpell> NATURE = nature(ID);
	public static final DataGenCachedHolder<ProjectileConfig> PROJECTILE = projectile(ID);

	public static final ResourceLocation TEX = GlimmeringTales.loc("textures/spell/pointed_dripstone_up_tip.png");

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
		builder.add(Blocks.DRIPSTONE_BLOCK.builtInRegistryHolder(), new BlockSpell(NATURE), false);
		builder.add(Blocks.POINTED_DRIPSTONE.builtInRegistryHolder(), new BlockSpell(NATURE), false);
	}

	@Override
	public void register(BootstrapContext<SpellAction> ctx) {
		var spell = new SpellAction(
				gen(new DataGenContext(ctx)),
				GTItems.RUNE_DRIPSTONE.asItem(),
				1010,
				SpellCastType.INSTANT,
				SpellTriggerType.TARGET_POS
		);
		spell.verify(SPELL.key.location());
		SPELL.gen(ctx, spell);
	}

	@Override
	public void genLang(RegistrateLangProvider ctx) {
		ctx.add(SpellAction.lang(ID), "Stalactite Burst");
	}

	private static ProjectileConfig proj(DataGenContext ctx) {
		return ProjectileConfig.builder(SelectionType.ENEMY_NO_FAMILY)
				.tick(new DustParticleInstance(
						ColorVariable.Static.of(0x836356),
						DoubleVariable.of("0.5"),
						DoubleVariable.ZERO,
						IntVariable.of("20")
				).move(OffsetModifier.of("0", "-0.2", "0")))
				.hit(new DamageProcessor(
						ctx.damage(DamageTypes.STALAGMITE),
						DoubleVariable.of("4"),
						true,
						true
				)).hit(new EffectProcessor(
						MobEffects.MOVEMENT_SLOWDOWN,
						IntVariable.of("100"),
						IntVariable.of("2"),
						false,
						true
				)).hit(new PushProcessor(
						DoubleVariable.of("1"),
						DoubleVariable.ZERO,
						DoubleVariable.ZERO,
						PushProcessor.Type.UNIFORM
				)).size(DoubleVariable.of("0.25"))
				.renderer(new VerticalRenderData(TEX))
				.build();
	}

	private static ConfiguredEngine<?> gen(DataGenContext ctx) {
		return new CustomProjectileShoot(
				DoubleVariable.of("0.8"),
				PROJECTILE,
				IntVariable.of("20"),
				false, true,
				Map.of()
		).move(OffsetModifier.of("0", "-0.45", "0"),
				new SetDirectionModifier(
						DoubleVariable.ZERO,
						DoubleVariable.of("1"),
						DoubleVariable.ZERO
				)).circular(
				DoubleVariable.of("2"),
				DoubleVariable.of("2"),
				false, null,
				new OrPredicate(List.of(
						SurfaceBelowCondition.full(),
						BlockMatchCondition.of(Blocks.POINTED_DRIPSTONE)
								.move(OffsetModifier.BELOW)
				)),
				BlockTestCondition.Type.BLOCKS_MOTION.get().invert()
		);
	}

}
