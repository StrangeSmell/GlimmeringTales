package dev.xkmc.glimmeringtales.content.engine.instance;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.glimmeringtales.init.data.GTDamageTypeGen;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import dev.xkmc.l2library.init.FlagMarker;
import dev.xkmc.l2magic.content.engine.block.IBlockProcessor;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.phys.Vec3;

public record LightningInstance(
		DoubleVariable damage
) implements IBlockProcessor<LightningInstance> {

	public static final MapCodec<LightningInstance> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			DoubleVariable.codec("damage", e -> e.damage)
	).apply(i, LightningInstance::new));

	public static DamageSource source(DamageSource source, LightningBolt bolt) {
		var sp = bolt.getCause();
		if (sp == null) return source;
		return AttackEventHandler.createSource(sp.serverLevel(), sp, GTDamageTypeGen.THUNDER, bolt, null);
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
		LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
		assert bolt != null;
		bolt.addTag(FlagMarker.LIGHTNING);
		bolt.addTag(GlimmeringTales.MODID);
		bolt.moveTo(Vec3.atBottomCenterOf(pos));
		bolt.setCause(ctx.user().user() instanceof ServerPlayer sp ? sp : null);
		bolt.setDamage((float) damage.eval(ctx));
		level.addFreshEntity(bolt);
	}

}