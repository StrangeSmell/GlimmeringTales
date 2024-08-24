package dev.xkmc.glimmeringtales.content.block.misc;

import dev.xkmc.l2modularblock.mult.ShapeUpdateBlockMethod;
import dev.xkmc.l2modularblock.mult.SurviveBlockMethod;
import dev.xkmc.l2modularblock.one.ShapeBlockMethod;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BamBooImpl implements ShapeBlockMethod, ShapeUpdateBlockMethod, SurviveBlockMethod {
	protected static final VoxelShape SMALL_SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);
	protected static final VoxelShape LARGE_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);
	protected static final VoxelShape COLLISION_SHAPE = Block.box(6.5, 0.0, 6.5, 9.5, 16.0, 9.5);

	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return LARGE_SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return COLLISION_SHAPE;
	}

	@Override
	public BlockState updateShape(Block block, BlockState current, BlockState state, Direction dire, BlockState nState, LevelAccessor level, BlockPos pos, BlockPos nPos) {
		return !state.canSurvive(level, pos) ? Blocks.AIR.defaultBlockState() : current;
	}

	@Override
	public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
		return true;
	}
}
