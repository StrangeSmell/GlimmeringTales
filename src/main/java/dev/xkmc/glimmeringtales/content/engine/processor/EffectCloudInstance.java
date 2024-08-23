package dev.xkmc.glimmeringtales.content.engine.processor;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.ConfiguredEngine;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.phys.Vec3;

public record EffectCloudInstance(
		Holder<Potion> eff,
		DoubleVariable radius,
		IntVariable duration
) implements ConfiguredEngine<EffectCloudInstance> {

	public static final MapCodec<EffectCloudInstance> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			BuiltInRegistries.POTION.holderByNameCodec().fieldOf("effect").forGetter(e -> e.eff),
			DoubleVariable.codec("radius", e -> e.radius),
			IntVariable.codec("duration", e -> e.duration)

	).apply(i, EffectCloudInstance::new));

	@Override
	public EngineType<EffectCloudInstance> type() {
		return GTEngine.EFFECT_CLOUD.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		if (!(ctx.user().level() instanceof ServerLevel)) return;
		PotionContents potioncontents = new PotionContents(eff);
		makeAreaOfEffectCloud(potioncontents, ctx);
	}


	private void makeAreaOfEffectCloud(PotionContents content, EngineContext ctx) {
		Vec3 vec3 = ctx.loc().pos();
		AreaEffectCloud e = new AreaEffectCloud(ctx.user().level(), vec3.x, vec3.y, vec3.z);

		Player player = (Player) ctx.user().user();
		e.setOwner(player);

		e.setRadius((float) radius.eval(ctx));
		e.setRadiusOnUse(-0.5F);
		e.setWaitTime(duration.eval(ctx));
		e.setRadiusPerTick(-e.getRadius() / (float) e.getDuration());
		e.setPotionContents(content);
		ctx.user().level().addFreshEntity(e);
	}

}