package dev.xkmc.glimmeringtales.init.data.spell.life;

import dev.xkmc.glimmeringtales.content.core.analysis.SpellTooltipData;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.engine.processor.StackingEffectProcessor;
import dev.xkmc.glimmeringtales.content.engine.render.CrossRenderData;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellBuilder;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellEntry;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.LoopIterator;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.content.entity.core.ProjectileConfig;
import dev.xkmc.l2magic.content.entity.engine.CustomProjectileShoot;
import dev.xkmc.l2magic.content.entity.motion.SimpleMotion;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;

public class CactusSpell   {
	public static final NatureSpellBuilder BUILDER = GTRegistries.LIFE.get()
			.build(GlimmeringTales.loc("cactus")).cost(40)
			.damageCustom(msg -> new DamageType(msg, 1f),
					"%s is pierced by cactus thorn",
					"%s is pierced by %s with cactus thorn",
					DamageTypeTags.IS_PROJECTILE)
			.projectile(CactusSpell::proj)
			.spell(ctx -> NatureSpellEntry.ofBlock(gen(ctx),Items.CACTUS, 1030))
			.block((b, e) -> b.add(Blocks.CACTUS.builtInRegistryHolder(), new BlockSpell(e, true, 1)))
			.lang("Cactus").desc(
					"[Block] Splash Cactus Thorn",
					"Launch cactus spikes in all directions, dealing %s ",
					SpellTooltipData.damage()
			);

	public static final ResourceLocation TEX = GlimmeringTales.loc("textures/spell/cactus.png");



	private static ConfiguredEngine<?> gen(NatureSpellBuilder ctx) {
		int phi = 7;
		int theta = 24;
		return new LoopIterator(
				IntVariable.of("" + phi),
				new LoopIterator(
						IntVariable.of("" + theta),
						new CustomProjectileShoot(
								DoubleVariable.of("1"),
								ctx.proj,
								IntVariable.of("100"),
								false, true,
								Map.of()
						).move(SetDirectionModifier.of("rand(" + "-1" + "," + 1 + ")", "0", "rand(" + "-1" + "," + 1 + ")"))
								.move(OffsetModifier.of("0", "rand(" + "-1" + "," + 0.5 + ")", "0"))
						, "j"
				), "i"
		).move(
				OffsetModifier.of("0", "0.55", "0"),
				SetDirectionModifier.of("1", "0", "0")
		);
	}

	private static ProjectileConfig proj(NatureSpellBuilder ctx) {
		return ProjectileConfig.builder(SelectionType.ENEMY_NO_FAMILY)
				.tick(new SimpleParticleInstance(
						ParticleTypes.CRIT,
						DoubleVariable.of("rand(" + 0.5 + "," + 1 + ")")
				).move(ForwardOffsetModifier.of("-0.2")).move(OffsetModifier.of("0", "rand(" + "-1" + "," + 1 + ")", "0")))

				.hit(new DamageProcessor(
						ctx.damage(),
						DoubleVariable.of("1"),
						true,
						true
				)).hit(new StackingEffectProcessor(
						LCEffects.BLEED,
						IntVariable.of("100"),
						IntVariable.of("6")
				)).size(DoubleVariable.of("0.25"))
				.motion(SimpleMotion.BREAKING)
				.renderer(new CrossRenderData(TEX))
				.build();
	}
}
