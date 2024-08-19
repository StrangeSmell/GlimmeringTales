package dev.xkmc.glimmeringtales.content.engine.processor.strange_smell;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.fastprojectileapi.collision.EntityStorageCache;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EntityProcessor;
import dev.xkmc.l2magic.content.engine.core.ProcessorType;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import dev.xkmc.l2magic.init.registrate.EngineRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StonecutterBlock;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.Iterator;


public record AffectingProjectilesProcessor(
        IntVariable num
)  implements EntityProcessor<AffectingProjectilesProcessor> {

    public static final MapCodec<AffectingProjectilesProcessor> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
            IntVariable.codec("num", e -> e.num)
    ).apply(i, AffectingProjectilesProcessor::new));
    @Override
    public ProcessorType<AffectingProjectilesProcessor> type() {
        return GTEngine.AFFECTINGPROJECT.get();
    }

    @Override
    public void process(Collection<LivingEntity> le, EngineContext ctx) {
        if (!(ctx.user().level() instanceof ServerLevel sl)) return;
        Player player = (Player) ctx.user().user();
        EntityStorageCache entityStorageCache = EntityStorageCache.get(sl);
        AABB aabb = player.getBoundingBox();
        Iterator<Entity> effectList = sl.getEntities(player,aabb.expandTowards(3,3,3).expandTowards(-3,-3,-3)).iterator();
        //Iterable<Entity> effectList = entityStorageCache.foreach(aabb.expandTowards(3,3,3).expandTowards(-3,-3,-3),this::canBeAffected);
        if(!effectList.hasNext()) return;
        Vec3 position = player.getEyePosition();
        for (Iterator<Entity> it = effectList; it.hasNext(); ) {
            Entity entity = it.next();
            Vec3 entityPosition = entity.getEyePosition();
            Vec3 diff = entityPosition.subtract(position);
            double distance = diff.length();
            if(distance<1){
                distance = 5;
            }else{
                distance =5/(distance*distance);
            }
            entity.setDeltaMovement(entity.getDeltaMovement().add(diff.normalize().multiply(distance,distance,distance)));
        }
    }


    public boolean canBeAffected(Entity projectile) {
        if(projectile instanceof Projectile) return true;
        else return false;
    }

}