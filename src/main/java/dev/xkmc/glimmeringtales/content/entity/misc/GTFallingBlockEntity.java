package dev.xkmc.glimmeringtales.content.entity.misc;

import dev.xkmc.glimmeringtales.init.data.GTDamageTypeGen;
import dev.xkmc.glimmeringtales.init.reg.GTEntities;
import dev.xkmc.l2damagetracker.contents.attack.AttackEventHandler;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.UUID;

public class GTFallingBlockEntity extends FallingBlockEntity implements TraceableEntity {

	@Nullable
	private UUID ownerUUID;
	@Nullable
	private Entity cachedOwner;

	public GTFallingBlockEntity(EntityType<GTFallingBlockEntity> entityType, Level level) {
		super(entityType, level);
	}

	public GTFallingBlockEntity(Level level, double x, double y, double z, BlockState state) {
		this(GTEntities.FALLING.get(), level);
		this.blockState = state;
		this.blocksBuilding = true;
		this.setPos(x, y, z);
		this.setDeltaMovement(Vec3.ZERO);
		this.xo = x;
		this.yo = y;
		this.zo = z;
		this.setStartPos(this.blockPosition());
	}

	private DamageSource damageSource(DamageSource old) {
		if (getOwner() instanceof LivingEntity le && le.level() == level() && level() instanceof ServerLevel sl) {
			return AttackEventHandler.createSource(sl, le, GTDamageTypeGen.FALLING, this, null);
		}
		return old;
	}

	public void setOwner(@Nullable Entity pOwner) {
		if (pOwner != null) {
			ownerUUID = pOwner.getUUID();
			cachedOwner = pOwner;
		}
	}

	@Nullable
	public Entity getOwner() {
		if (cachedOwner != null && !cachedOwner.isRemoved()) {
			return cachedOwner;
		} else if (ownerUUID != null && level() instanceof ServerLevel) {
			cachedOwner = ((ServerLevel) level()).getEntity(ownerUUID);
			return cachedOwner;
		} else {
			return null;
		}
	}

	@OverridingMethodsMustInvokeSuper
	protected void addAdditionalSaveData(CompoundTag nbt) {
		if (ownerUUID != null) {
			nbt.putUUID("Owner", ownerUUID);
		}
	}

	@OverridingMethodsMustInvokeSuper
	protected void readAdditionalSaveData(CompoundTag nbt) {
		if (nbt.hasUUID("Owner")) {
			ownerUUID = nbt.getUUID("Owner");
			cachedOwner = null;
		}
	}

	@Override
	public DamageSources damageSources() {
		return new CustomSources(level().registryAccess());
	}

	private static class CustomSources extends DamageSources {

		public CustomSources(RegistryAccess registry) {
			super(registry);
		}

		@Override
		public DamageSource fallingBlock(Entity entity) {
			if (entity instanceof GTFallingBlockEntity fall)
				return fall.damageSource(super.fallingBlock(entity));
			return super.fallingBlock(entity);
		}
	}

}
