package dev.xkmc.glimmeringtales.content.engine.instance;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xkmc.glimmeringtales.init.reg.GTEngine;
import dev.xkmc.l2magic.content.engine.block.IBlockProcessor;
import dev.xkmc.l2magic.content.engine.context.EngineContext;
import dev.xkmc.l2magic.content.engine.core.EngineType;
import dev.xkmc.l2magic.content.engine.variable.IntVariable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Predicate;

public record RemoveLiquidInstance(
		IntVariable depth,
		IntVariable limit,
		FluidType fluid
) implements IBlockProcessor<RemoveLiquidInstance> {

	public static final MapCodec<RemoveLiquidInstance> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			IntVariable.codec("depth", RemoveLiquidInstance::depth),
			IntVariable.codec("limit", RemoveLiquidInstance::limit),
			NeoForgeRegistries.FLUID_TYPES.byNameCodec().fieldOf("fluid").forGetter(RemoveLiquidInstance::fluid)
	).apply(i, RemoveLiquidInstance::new));

	@Override
	public EngineType<RemoveLiquidInstance> type() {
		return GTEngine.SPONGE.get();
	}

	@Override
	public void execute(EngineContext ctx) {
		removeWaterBreadthFirstSearch(ctx.user().level(), BlockPos.containing(ctx.loc().pos()),
				depth.eval(ctx), limit.eval(ctx), e -> e.getType().getFluidType() == fluid);
	}

	private static boolean removeWaterBreadthFirstSearch(Level level, BlockPos pos, int depth, int limit, Predicate<FluidState> pred) {
		return BlockPos.breadthFirstTraversal(pos, depth, limit,
				(p, c) -> {
					for (Direction dir : Direction.values()) {
						c.accept(p.relative(dir));
					}
				},
				p -> {
					BlockState state = level.getBlockState(p);
					FluidState fluid = level.getFluidState(p);
					if (!pred.test(fluid)) return false;
					if (state.getBlock() instanceof BucketPickup bucket &&
							!bucket.pickupBlock(null, level, p, state).isEmpty())
						return true;
					if (state.getBlock() instanceof LiquidBlock) {
						level.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
					} else {
						BlockEntity blockentity = state.hasBlockEntity() ? level.getBlockEntity(p) : null;
						Block.dropResources(state, level, p, blockentity);
						level.setBlock(p, Blocks.AIR.defaultBlockState(), 3);
					}
					return true;
				}
		) > 1;
	}

}
