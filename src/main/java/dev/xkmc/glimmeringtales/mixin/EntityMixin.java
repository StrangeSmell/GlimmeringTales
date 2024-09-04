package dev.xkmc.glimmeringtales.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.xkmc.glimmeringtales.content.engine.instance.LightningInstance;
import dev.xkmc.glimmeringtales.init.GlimmeringTales;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LightningBolt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {

	@WrapOperation(method = "thunderHit", at = @At(value = "INVOKE", target =
			"Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
	public boolean glimmeringTales$thunderHit(
			Entity e, DamageSource source, float amount, Operation<Boolean> original,
			@Local(argsOnly = true) LightningBolt bolt
	) {
		if (bolt.getTags().contains(GlimmeringTales.MODID)) {
			source = LightningInstance.source(source, bolt);
		}
		return original.call(e, source, amount);
	}

}
