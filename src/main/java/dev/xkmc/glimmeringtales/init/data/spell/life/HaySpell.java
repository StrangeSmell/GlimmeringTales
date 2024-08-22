package dev.xkmc.glimmeringtales.init.data.spell.life;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.content.engine.processor.ProcreationProcessor;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellEntry;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.DelayedIterator;
import dev.xkmc.l2magic.content.engine.iterator.RingRandomIterator;
import dev.xkmc.l2magic.content.engine.iterator.SphereRandomIterator;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.logic.MoveEngine;
import dev.xkmc.l2magic.content.engine.logic.PredicateLogic;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.RotationModifier;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.selector.ApproxBallSelector;
import dev.xkmc.l2magic.content.engine.selector.ArcCubeSelector;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.List;

public class HaySpell extends NatureSpellEntry {

    public static final ResourceKey<SpellAction> PROCREATION = spell("procreation");
    public static final ResourceLocation ID = GlimmeringTales.loc("procreation");
    public static final DataGenCachedHolder<SpellAction> SPELL = spell(ID);
    public static final DataGenCachedHolder<NatureSpell> NATURE = nature(ID);


    @Override
    public void regNature(BootstrapContext<NatureSpell> ctx) {
        NATURE.gen(ctx, new NatureSpell(SPELL, GTRegistries.LIFE.get(), 20));
    }


	@Override
	public void regBlock(DataMapProvider.Builder<BlockSpell, Block> builder) {
		builder.add(Blocks.HAY_BLOCK.builtInRegistryHolder(), new BlockSpell(NATURE, true, 1), false);
	}


    @Override
    public void genLang(RegistrateLangProvider ctx) {
        ctx.add(SpellAction.lang(ID), "Procreation");
    }

    @Override
    public void register(BootstrapContext<SpellAction> ctx) {
        new SpellAction(
                procreation(new DataGenContext(ctx), 5, 1.5, 1),
                Items.WHEAT, 100,
                SpellCastType.INSTANT,
                SpellTriggerType.TARGET_POS
        ).verifyOnBuild(ctx, SPELL);

/*        new SpellAction(
                affectProjectile(new DataGenContext(ctx), 4, 1.5, 1),
                Items.ARROW, 100,
                SpellCastType.CONTINUOUS,
                SpellTriggerType.SELF_POS
        ).verifyOnBuild(ctx, AFFECTING_PROJECTILE);*/
    }

    private static ConfiguredEngine<?> affectProjectile(DataGenContext ctx, double r, double y, double size) {
        return new ListLogic(List.of(
                new DelayedIterator(
                        IntVariable.of("10"),
                        IntVariable.of("2"),
                        new RingRandomIterator(
                                DoubleVariable.of((r - size) + ""),
                                DoubleVariable.of((r + size) + ""),
                                DoubleVariable.of("-180"),
                                DoubleVariable.of("180"),
                                IntVariable.of("5"),
                                new MoveEngine(List.of(
                                        RotationModifier.of("75"),
                                        OffsetModifier.of("0", "rand(" + (y - size) + "," + (y + size) + ")", "0")),
                                        new SimpleParticleInstance(
                                                ParticleTypes.BUBBLE,
                                                DoubleVariable.of("0.5")
                                        )
                                ), null
                        ), null
                )
        ));
    }

    private static ConfiguredEngine<?> procreation(DataGenContext ctx, double r, double y, double size) {
        return new ListLogic(List.of(
                new ProcessorEngine(SelectionType.ALL,
                        new ApproxBallSelector(
                                DoubleVariable.of(r + "")
                        ),
                        List.of(
                                new ProcreationProcessor(
                                        IntVariable.of("4")
                                )
                        )),
                new DelayedIterator(
                        IntVariable.of("10"),
                        IntVariable.of("2"),
                        new SphereRandomIterator(
                                DoubleVariable.of("5"),
                                IntVariable.of("20"),
                                new SimpleParticleInstance(
                                        ParticleTypes.HEART,
                                        DoubleVariable.of("0.5")
                                ),
                                null
                        ), null
                )
        ));
    }




}
