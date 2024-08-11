package dev.xkmc.glimmeringtales.content.block.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ClayCarpetBlock extends Block {

	protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);

	public ClayCarpetBlock(BlockBehaviour.Properties prop) {
		super(prop);
	}

	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
		return SHAPE;
	}

	protected BlockState updateShape(BlockState state, Direction dire, BlockState nState, LevelAccessor level, BlockPos pos, BlockPos nPos) {
		return !state.canSurvive(level, pos) ?
				Blocks.AIR.defaultBlockState() :
				super.updateShape(state, dire, nState, level, pos, nPos);
	}

	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		var below = level.getBlockState(pos.below());
		return Block.isFaceFull(below.getCollisionShape(level, pos.below()), Direction.UP);
	}

}
