package xiroc.deltahardmode.common.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FallingBlockHelper {

	public static void updateNeighbors(World worldIn, BlockPos pos) {
		checkFallable(worldIn, pos.east());
		checkFallable(worldIn, pos.south());
		checkFallable(worldIn, pos.west());
		checkFallable(worldIn, pos.north());
		checkFallable(worldIn, pos.up());
		checkFallable(worldIn, pos.down());
	}

	public static void checkFallable(World worldIn, BlockPos pos) {
		if (!ConfigCache.gravityBlocks.contains(worldIn.getBlockState(pos).getBlock()))
			return;
		if (worldIn.isAirBlock(pos.down())
				|| canFallThrough(worldIn, pos, worldIn.getBlockState(pos.down())) && pos.getY() >= 0) {
			if (!worldIn.isRemote) {
//				DeltaHardMode.LOGGER.debug("Spawning a FallingBlockEntity at {}", pos);
				FallingBlockEntity fallingblockentity = new FallingBlockEntity(worldIn, (double) pos.getX() + 0.5D,
						(double) pos.getY(), (double) pos.getZ() + 0.5D, worldIn.getBlockState(pos));
//	            this.onStartFalling(fallingblockentity);
				worldIn.addEntity(fallingblockentity);
			}

		}
	}

	public static boolean canFallThrough(World world, BlockPos pos, BlockState state) {
		Block block = state.getBlock();
		Material material = state.getMaterial();
		return state.isAir(world, pos.down()) || block == Blocks.FIRE || material.isLiquid() || material.isReplaceable();
	}

}
