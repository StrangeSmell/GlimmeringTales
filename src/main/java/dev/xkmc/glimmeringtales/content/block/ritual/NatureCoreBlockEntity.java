package dev.xkmc.glimmeringtales.content.block.ritual;

import dev.xkmc.glimmeringtales.content.block.altar.CoreRitualBlockEntity;
import dev.xkmc.glimmeringtales.content.recipe.ritual.RitualInput;
import dev.xkmc.glimmeringtales.content.recipe.ritual.RitualRecipe;
import dev.xkmc.glimmeringtales.init.reg.GTRecipes;
import dev.xkmc.l2serial.serialization.marker.SerialClass;
import dev.xkmc.l2serial.serialization.marker.SerialField;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

@SerialClass
public class NatureCoreBlockEntity extends CoreRitualBlockEntity {

	private static float curve(int m, float lap) {
		int t = 40;
		float s = 5;
		float factor = (s - 1) / 2;
		if (lap < t) {
			return lap + lap * lap * factor / t;
		} else if (lap < m - t) {
			return lap * s - factor * t;
		} else if (lap < m) {
			float diff = m - lap;
			return m * s - t * (s - 1) - (diff + diff * diff * factor / t);
		} else return (m - t) * (s - 1) + lap;
	}

	@SerialField
	private int totalTime, remainTime;

	@SerialField
	private long lastKeyTime, lastTimeStamp;

	@SerialField
	private ResourceLocation recipeId;

	private RitualRecipe<?> recipe;

	public float getTime(float pTick) {
		long time = level == null ? 0 : level.getGameTime();
		float lap = time - lastTimeStamp + pTick;
		float animTime = totalTime == 0 ? lap : curve(totalTime, lap);
		return (lastKeyTime + animTime) * 3 % 360;
	}

	public NatureCoreBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void tick() {
		super.tick();
		tickCraft();
		if (level != null && level.isClientSide()) {
			tickParticle();
		}
	}

	private void laps() {
		long currentTime = level == null ? 0 : level.getGameTime();
		long lap = currentTime - lastTimeStamp;
		lastKeyTime += lap;
		lastTimeStamp = currentTime;
	}

	private void stopLap() {
		long time = level == null ? 0 : level.getGameTime();
		float lap = time - lastTimeStamp;
		float animTime = totalTime == 0 ? lap : curve(totalTime, lap);
		lastKeyTime += (int) animTime;
		lastTimeStamp = time;
	}

	public void triggerCraft() {
		if (level == null || level.isClientSide || remainTime > 0) return;
		RitualInput input = new RitualInput(this, getLinked());
		var opt = level.getRecipeManager().getRecipeFor(GTRecipes.RT_RITUAL.get(), input, level);
		if (opt.isEmpty()) return;
		recipeId = opt.get().id();
		recipe = opt.get().value();
		totalTime = remainTime = recipe.getTime();
		laps();
		sync();
		setChanged();
	}

	private void tickCraft() {
		if (level == null || level.isClientSide() || remainTime <= 0) return;
		boolean update = false;
		if (recipeId == null || recipe == null) {
			RitualInput input = new RitualInput(this, getLinked());
			var opt = level.getRecipeManager().getRecipeFor(GTRecipes.RT_RITUAL.get(), input, level);
			if (opt.isEmpty()) {
				onLinkBreak();
				return;
			}
			recipeId = opt.get().id();
			recipe = opt.get().value();
			update = true;
		}
		remainTime--;
		if (remainTime <= 0) {
			recipe.assemble(new RitualInput(this, getLinked()), level.registryAccess());
			stopLap();
			remainTime = 0;
			totalTime = 0;
			recipeId = null;
			recipe = null;
			update = true;
		}
		if (update) {
			sync();
			setChanged();
		}
	}

	private void tickParticle() {
		if (level == null) return;
		var pos = getBlockPos().getCenter().add(0, 1, 0);
		for (var e : getLinked()) {
			var ip = e.getBlockPos().getCenter().add(0, 1, 0).subtract(pos);
			if (remainTime > 0 || level.getRandom().nextInt(8) == 0)
				level.addParticle(ParticleTypes.ENCHANT, pos.x, pos.y, pos.z, ip.x, ip.y, ip.z);
		}
	}

	@Override
	public boolean locked() {
		return remainTime > 0;
	}

	@Override
	public void onLinkBreak() {
		remainTime = 0;
		totalTime = 0;
		recipeId = null;
		recipe = null;
		stopLap();
		sync();
		setChanged();
	}

}
