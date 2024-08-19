package dev.xkmc.glimmeringtales.init.data.spell.strange_smell;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellEntry;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.*;
import dev.xkmc.l2magic.content.engine.logic.ListLogic;
import dev.xkmc.l2magic.content.engine.modifier.SetDirectionModifier;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.logic.ProcessorEngine;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.modifier.OffsetModifier;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.DamageProcessor;
import dev.xkmc.l2magic.content.engine.processor.PushProcessor;
import dev.xkmc.l2magic.content.engine.selector.BoxSelector;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
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
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.List;

public class CactusSpell extends NatureSpellEntry {
    public static final ResourceLocation ID = GlimmeringTales.loc("cactus");
    public static final DataGenCachedHolder<SpellAction> SPELL = spell(ID);
    public static final DataGenCachedHolder<NatureSpell> NATURE = nature(ID);

    @Override
    public void regNature(BootstrapContext<NatureSpell> ctx) {
        NATURE.gen(ctx, new NatureSpell(SPELL, GTRegistries.LIFE.get(), 20));
    }

    @Override
    public void regBlock(DataMapProvider.Builder<BlockSpell, Block> builder) {
        builder.add(Blocks.CACTUS.builtInRegistryHolder(), new BlockSpell(NATURE), false);
    }


    @Override
    public void genLang(RegistrateLangProvider ctx) {
        ctx.add(SpellAction.lang(ID), "Cactus");
    }

    @Override
    public void register(BootstrapContext<SpellAction> ctx) {
        new SpellAction(
                cactus(new DataGenContext(ctx)),
                Items.CACTUS, 3100,
                SpellCastType.INSTANT,
                SpellTriggerType.TARGET_POS
        ).verifyOnBuild(ctx, SPELL);
    }

    private static ConfiguredEngine<?> cactus(DataGenContext ctx) {
        return new RingIterator(
                DoubleVariable.of("0.1"),
                DoubleVariable.of("0"),
                DoubleVariable.of("360"),
                IntVariable.of("120"),
                true,
                shootMove(ctx).move(SetDirectionModifier.of("rand(" +"-1"+ "," + 1+ ")","0","rand(" +"-1"+ "," + 1+ ")")),
                "i"
        );
    }

    private static ConfiguredEngine<?> shootMove(DataGenContext ctx) {
        int dis = 12;
        double rad = 1;

        return new DelayedIterator(IntVariable.of(dis + ""), IntVariable.of("1"),
                new ListLogic(List.of(
                        new ProcessorEngine(SelectionType.ENEMY,
                                new BoxSelector(
                                        DoubleVariable.of(rad + ""),
                                        DoubleVariable.of(rad + ""),
                                        true
                                ), List.of(new DamageProcessor(
                                        ctx.damage(DamageTypes.INDIRECT_MAGIC),
                                        DoubleVariable.of("6"),
                                        true, true),
                                new PushProcessor(
                                        DoubleVariable.of("0.5"),
                                        DoubleVariable.ZERO,
                                        DoubleVariable.ZERO,
                                        PushProcessor.Type.UNIFORM
                                )
                        )),
                        new SimpleParticleInstance(
                                ParticleTypes.CRIT,
                                DoubleVariable.of("rand(" + 0.5+ "," +1 + ")")
                        ).move(OffsetModifier.of("0", "rand(" +"-1"+ "," + 1+ ")", "0"))
                )).move(ForwardOffsetModifier.of(rad + "*i")),
                "i");
    }
}
