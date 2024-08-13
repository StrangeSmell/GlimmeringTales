package dev.xkmc.glimmeringtales.content.item.rune;

import dev.xkmc.l2magic.content.engine.context.SpellContext;
import dev.xkmc.l2magic.content.engine.helper.Orientation;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

public record BlockSpellContext(SpellContext ctx, BlockState state) {

	@Nullable
	public static BlockSpellContext blockSpellContext(LivingEntity user, int distance) {
		Level level = user.level();
		Vec3 dir = user.getEyePosition();
		Vec3 forward = SpellContext.getForward(user);
		Vec3 end = dir.add(forward.scale(distance));
		BlockHitResult bhit = level.clip(new ClipContext(dir, end, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, user));
		if (bhit.getType() == HitResult.Type.MISS) return null;
		var pos = bhit.getBlockPos();
		var ori = Orientation.regular().asNormal();
		long seed = 0L;
		if (!level.isClientSide()) {
			seed = ThreadLocalRandom.current().nextLong();
		}
		var ctx = new SpellContext(user, pos.getCenter(), ori, seed, 0, 1);
		return new BlockSpellContext(ctx, level.getBlockState(pos));
	}

}
