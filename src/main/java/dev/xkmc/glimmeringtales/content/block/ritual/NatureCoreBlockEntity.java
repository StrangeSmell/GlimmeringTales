package dev.xkmc.glimmeringtales.content.block.ritual;

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

	@SerialField
	private int remainTime;

	@SerialField
	private ResourceLocation recipeId;

	private RitualRecipe<?> recipe;

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

	public void triggerCraft() {
		if (level == null || level.isClientSide || remainTime > 0) return;
		RitualInput input = new RitualInput(this, getLinked());
		var opt = level.getRecipeManager().getRecipeFor(GTRecipes.RT_RITUAL.get(), input, level);
		if (opt.isEmpty()) return;
		recipeId = opt.get().id();
		recipe = opt.get().value();
		remainTime = recipe.getTime();
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
			remainTime = 0;
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
		recipeId = null;
		recipe = null;
	}

}
