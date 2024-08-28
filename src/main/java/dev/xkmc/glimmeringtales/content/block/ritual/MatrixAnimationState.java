package dev.xkmc.glimmeringtales.content.block.ritual;

import dev.xkmc.glimmeringtales.content.item.rune.SpellCoreItem;
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
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MatrixAnimationState {

	private static final int MIN_SPEED = 4, MAX_SPEED = 44;
	private static final float W = 0.75f;

	private final List<ItemStack> cachedStacks = new ArrayList<>();

	private int speed = MIN_SPEED, nextSpeed = MIN_SPEED;
	private float angle = 0;
	private boolean prevState = false;

	public float getTime(float pTick) {
		int diff = nextSpeed - speed;
		return angle + W * speed * pTick * (1 + diff * pTick / 2f);
	}

	public boolean tick(boolean crafting) {
		boolean change = prevState != crafting;
		int diff = nextSpeed - speed;
		angle += W * speed * (1 + diff / 2f);
		speed = nextSpeed;
		nextSpeed = crafting ? Math.min(MAX_SPEED, speed + 1) : Math.max(MIN_SPEED, speed - 1);
		prevState = crafting;
		return change;
	}

	public void setup(List<ItemStack> list) {
		cachedStacks.clear();
		cachedStacks.addAll(list);
	}

	public void release(Level level, BlockPos pos, NatureCoreBlockEntity be) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		int m = 300;
		int col = -1;
		if (!cachedStacks.isEmpty() && cachedStacks.getFirst().getItem() instanceof SpellCoreItem item) {
			var aff = item.getAffinity(level);
			if (aff != null && aff.affinity().entrySet().size() == 1) {
				var e = new ArrayList<>(aff.affinity().entrySet()).getFirst().getKey();
				col = e.getColor();
			}
		}
		var center = pos.above().getCenter();
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
		cachedStacks.clear();
	}

}
