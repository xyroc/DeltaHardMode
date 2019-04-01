package xiroc.deltahard.util;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FallingBlockHelper {

	public static ArrayList<Block> gravityBlocks = new ArrayList();

	public static void updateNeighbors(World worldIn, BlockPos pos, boolean fallInstantly) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		checkFallable(worldIn, new BlockPos(x - 1, y, z), fallInstantly);
		checkFallable(worldIn, new BlockPos(x + 1, y, z), fallInstantly);
		checkFallable(worldIn, new BlockPos(x, y - 1, z), fallInstantly);
		checkFallable(worldIn, new BlockPos(x, y + 1, z), fallInstantly);
		checkFallable(worldIn, new BlockPos(x, y, z - 1), fallInstantly);
		checkFallable(worldIn, new BlockPos(x, y, z + 1), fallInstantly);
	}

	public static void checkFallable(World worldIn, BlockPos pos, boolean fallInstantly) {
		if(!gravityBlocks.contains(worldIn.getBlockState(pos).getBlock())) return;
		if ((worldIn.isAirBlock(pos.down()) || canFallThrough(worldIn.getBlockState(pos.down()))) && pos.getY() >= 0) {
			int i = 32;

			if (!fallInstantly && worldIn.isAreaLoaded(pos.add(-32, -32, -32), pos.add(32, 32, 32))) {
				if (!worldIn.isRemote) {
					EntityFallingBlock entityfallingblock = new EntityFallingBlock(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY(), (double) pos.getZ() + 0.5D, worldIn.getBlockState(pos));
					worldIn.spawnEntity(entityfallingblock);
				}
			} else {
				IBlockState state = worldIn.getBlockState(pos);
				worldIn.setBlockToAir(pos);
				BlockPos blockpos;

				for (blockpos = pos.down(); (worldIn.isAirBlock(blockpos) || canFallThrough(worldIn.getBlockState(blockpos))) && blockpos.getY() > 0; blockpos = blockpos.down()) {
					;
				}

				if (blockpos.getY() > 0) {
					worldIn.setBlockState(blockpos.up(), state); // Forge: Fix loss of state information during world gen.
				}
			}
		}
	}

	public static boolean canFallThrough(IBlockState state) {
		Block block = state.getBlock();
		Material material = state.getMaterial();
		return block == Blocks.FIRE || material == Material.AIR || material == Material.WATER || material == Material.LAVA;
	}

}
