package dev.xkmc.glimmeringtales.content.engine.instance;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.glimmeringtales.content.entity.misc.GTLightningBolt;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTDamageTypeGen;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.glimmeringtales.init.reg.GTEntities;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2library.init.FlagMarker;
import dev.xkmc.l2magic.content.engine.block.IBlockProcessor;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.selector.SelectionType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public record LightningInstance(
		DoubleVariable damage
) implements IBlockProcessor<LightningInstance> {

	public static final MapCodec<LightningInstance> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			DoubleVariable.codec("damage", e -> e.damage)
	).apply(i, LightningInstance::new));

	@Nullable
	public static DamageSource source(DamageSource source, LightningBolt bolt, Entity target) {
		var e = bolt instanceof GTLightningBolt gt ? gt.getOwner() : bolt.getCause();
		if (!(e instanceof LivingEntity sp) || !(bolt.level() instanceof ServerLevel sl)) return source;
		if (!SelectionType.ENEMY_NO_FAMILY.test(target, sp)) return null;
		return AttackEventHandler.createSource(sl, sp, GTDamageTypeGen.THUNDER, bolt, null);
	}

	@Override
	public EngineType<LightningInstance> type() {
		return GTEngine.THUNDER.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		if (!(ctx.user().level() instanceof ServerLevel)) return;
		var level = ctx.user().level();
		var pos = BlockPos.containing(ctx.loc().pos());
		var state = level.getBlockState(pos);
		if (state.isCollisionShapeFullBlock(level, pos))
			pos = pos.above();
		GTLightningBolt bolt = new GTLightningBolt(GTEntities.LIGHTNING.get(), level);
		bolt.addTag(FlagMarker.LIGHTNING);
		bolt.addTag(GlimmeringTales.MODID);
		bolt.moveTo(Vec3.atBottomCenterOf(pos));
		bolt.setOwner(ctx.user().user());
		bolt.setDamage((float) damage.eval(ctx));
		level.addFreshEntity(bolt);
	}

}