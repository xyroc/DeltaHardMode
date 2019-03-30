package xiroc.deltahard.world;

import java.util.Random;

import com.google.common.base.Predicate;

import net.minecraft.block.BlockSilverfish;
import net.minecraft.block.state.IBlockState;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

public class WorldGenOre implements IWorldGenerator {

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		switch (world.provider.getDimension()) {
		// -1: Nether, 0: Overworld, 1: End
		case -1:
			break;
		case 0:
			// BlockSilverfish
			runGenerator(Blocks.MONSTER_EGG.getDefaultState(), 7, 25, 1, 63, BlockMatcher.forBlock(Blocks.STONE), world,
					random, chunkX, chunkZ);
			break;
		case 1:
			break;
		default:
			break;
		}
	}

	private void runGenerator(IBlockState blockToGen, int blockAmount, int spawnTries, int minHeight, int maxHeight,
			Predicate<IBlockState> blockToReplace, World world, Random rand, int chunk_X, int chunk_Z) {
		if (minHeight < 0 || maxHeight > 256 || minHeight > maxHeight)
			throw new IllegalArgumentException(
					"Illegal Height Arguments: (min:" + minHeight + ",max:" + maxHeight + ")");
		WorldGenMinable generator = new WorldGenMinable(blockToGen, blockAmount, blockToReplace);
		int heightdiff = maxHeight - minHeight + 1;
		for (int i = 0; i < spawnTries; i++) {
			int x = chunk_X * 16 + rand.nextInt(16);
			int y = minHeight + rand.nextInt(heightdiff);
			int z = chunk_Z * 16 + rand.nextInt(16);
			generator.generate(world, rand, new BlockPos(x, y, z));
		}
	}

}
