package dev.xkmc.glimmeringtales.content.block.ritual;

import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import dev.xkmc.l2magic.content.engine.context.BuilderContext;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.context.LocationContext;
import dev.xkmc.l2magic.content.engine.context.UserContext;
import dev.xkmc.l2magic.content.engine.logic.PredicateLogic;
import dev.xkmc.l2magic.content.engine.modifier.ForwardOffsetModifier;
import dev.xkmc.l2magic.content.engine.particle.DustParticleInstance;
import dev.xkmc.l2magic.content.engine.particle.SimpleParticleInstance;
import dev.xkmc.l2magic.content.engine.variable.BooleanVariable;
import dev.xkmc.l2magic.content.engine.variable.ColorVariable;
import dev.xkmc.l2magic.content.engine.variable.DoubleVariable;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.Set;

public class MatrixParticleHelper {

	public static void complete(Level level, Vec3 center, int col) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		int m = 300;
		var dust = new PredicateLogic(BooleanVariable.of("rand(0,2)<1"),
				new DustParticleInstance(
						ColorVariable.Static.of(col),
						DoubleVariable.of("0.5"),
						DoubleVariable.of("0.2"),
						IntVariable.of("40")
				),
				new SimpleParticleInstance(
						ParticleTypes.END_ROD,
						DoubleVariable.of("0.2")
				)
		).move(ForwardOffsetModifier.of("0.05"));
		var build = new BuilderContext(GlimmeringTales.LOGGER, "", Set.of(), false);
		dust.verify(build);
		for (int i = 0; i < m; i++) {
			double x = level.getRandom().nextGaussian();
			double y = level.getRandom().nextGaussian();
			double z = level.getRandom().nextGaussian();
			var dir = new Vec3(x, y, z).normalize();
			var ctx = new EngineContext(
					new UserContext(level, player, null),
					new LocationContext(center, dir, Vec3.ZERO),
					level.random, Map.of());
			dust.execute(ctx);
		}
	}

}
