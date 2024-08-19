package dev.xkmc.glimmeringtales.content.engine.processor.strange_smell;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;

public record EffectCloudProcessor(
        Holder<Potion> eff,
        DoubleVariable radius,
        IntVariable duration

        )  implements EntityProcessor<EffectCloudProcessor> {

    public static final MapCodec<EffectCloudProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            BuiltInRegistries.POTION.holderByNameCodec().fieldOf("effect").forGetter(e -> e.eff),
            DoubleVariable.codec("radius", e -> e.radius),
            IntVariable.codec("duration", e -> e.duration)

            ).apply(i, EffectCloudProcessor::new));

    @Override
    public ProcessorType<EffectCloudProcessor> type() {
        return GTEngine.EFFECT_CLOUD.get();
    }

    @Override
    public void process(Collection<LivingEntity> le, EngineContext ctx) {
        if (!(ctx.user().level() instanceof ServerLevel)) return;

        PotionContents potioncontents = new PotionContents(eff);

        makeAreaOfEffectCloud(potioncontents,ctx);
    }


    private void makeAreaOfEffectCloud(PotionContents potionContents,EngineContext ctx) {
        Vec3 vec3 = ctx.loc().pos();
        AreaEffectCloud areaeffectcloud = new AreaEffectCloud(ctx.user().level(), vec3.x, vec3.y, vec3.z);

        Player player = (Player) ctx.user().user();
        areaeffectcloud.setOwner(player);

        areaeffectcloud.setRadius((float) radius.eval(ctx));
        areaeffectcloud.setRadiusOnUse(-0.5F);
        areaeffectcloud.setWaitTime(duration.eval(ctx));
        areaeffectcloud.setRadiusPerTick(-areaeffectcloud.getRadius() / (float)areaeffectcloud.getDuration());
        areaeffectcloud.setPotionContents(potionContents);
        ctx.user().level().addFreshEntity(areaeffectcloud);
    }

}