package dev.xkmc.glimmeringtales.init.data.spell.strange_smell;

import com.tterrag.registrate.providers.RegistrateLangProvider;
import dev.xkmc.glimmeringtales.content.core.spell.BlockSpell;
import dev.xkmc.glimmeringtales.content.core.spell.NatureSpell;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTTagGen;
import dev.xkmc.glimmeringtales.init.data.spell.NatureSpellEntry;
import dev.xkmc.glimmeringtales.init.reg.GTRegistries;
import dev.xkmc.l2magic.content.engine.context.DataGenContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.iterator.*;
import dev.xkmc.l2magic.content.engine.logic.*;
import dev.xkmc.l2magic.content.engine.particle.BlockParticleInstance;
import dev.xkmc.l2magic.content.engine.processor.*;
import dev.xkmc.l2magic.content.engine.selector.*;
import dev.xkmc.l2magic.content.engine.spell.SpellAction;
import dev.xkmc.l2magic.content.engine.spell.SpellCastType;
import dev.xkmc.l2magic.content.engine.spell.SpellTriggerType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.data.DataGenCachedHolder;
import dev.xkmc.l2magic.init.data.SpellDataGenEntry;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.DataMapProvider;

import java.util.List;

public class VinesSpell extends NatureSpellEntry {

    public static final ResourceLocation ID = GlimmeringTales.loc("vine");
    public static final DataGenCachedHolder<SpellAction> SPELL = spell(ID);
    public static final DataGenCachedHolder<NatureSpell> NATURE = nature(ID);

    @Override
    public void regNature(BootstrapContext<NatureSpell> ctx) {
        NATURE.gen(ctx, new NatureSpell(SPELL, GTRegistries.LIFE.get(), 20));
    }

    @Override
    public void regBlock(DataMapProvider.Builder<BlockSpell, Block> builder) {
        builder.add(GTTagGen.VINE, new BlockSpell(NATURE), false);
    }


    @Override
    public void genLang(RegistrateLangProvider ctx) {
        ctx.add(SpellAction.lang(ID), "Vine");
    }

    @Override
    public void register(BootstrapContext<SpellAction> ctx) {
        new SpellAction(
                vine(new DataGenContext(ctx), 4, 0.3),
                Items.VINE, 3100,
                SpellCastType.INSTANT,
                SpellTriggerType.TARGET_POS
        ).verifyOnBuild(ctx, SPELL);
    }

    private static ConfiguredEngine<?> vine(DataGenContext ctx, double radius, double step) {
        return new ListLogic(List.of(
                new ProcessorEngine(
                        SelectionType.ENEMY,
                        new BoxSelector(
                                DoubleVariable.of("16"),
                                DoubleVariable.of("16"),
                                true
                        ),
                        List.of(
                                new DamageProcessor(
                                        ctx.damage(DamageTypes.INDIRECT_MAGIC),
                                        DoubleVariable.of("1"),
                                        true, false
                                ),
                                new PushProcessor(
                                        DoubleVariable.of("-.5"),
                                        DoubleVariable.ZERO,
                                        DoubleVariable.ZERO,
                                        PushProcessor.Type.TO_CENTER
                                )
                        )
                ),
                new LoopIterator(
                        IntVariable.of("1"),
                        new SphereRandomIterator(
                                DoubleVariable.of("8"),
                                IntVariable.of("200"),
                                new BlockParticleInstance(
                                        Blocks.VINE,
                                        DoubleVariable.of("-.5"),
                                        DoubleVariable.of("1"),
                                        IntVariable.of("40"),
                                        true
                                ),
                                null
                        )
                        ,
                        null

                )

        ));
    }


}