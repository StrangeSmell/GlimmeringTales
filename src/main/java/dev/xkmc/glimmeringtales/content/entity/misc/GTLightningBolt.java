package dev.xkmc.glimmeringtales.content.entity.misc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.UUID;

public class GTLightningBolt extends LightningBolt implements TraceableEntity {

	@Nullable
	private UUID ownerUUID;
	@Nullable
	private Entity cachedOwner;

	public GTLightningBolt(EntityType<? extends GTLightningBolt> entityType, Level level) {
		super(entityType, level);
	}

	public void setOwner(@Nullable Entity pOwner) {
		if (pOwner != null) {
			ownerUUID = pOwner.getUUID();
			cachedOwner = pOwner;
			if (pOwner instanceof ServerPlayer sp) {
				setCause(sp);
			}
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

}
