package xiroc.deltahard.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;

public class DeltaHardBlocks {
	
	// Wood
	public static final Block stairsFallingAcacia = new BlockStairsFalling("acacia_stairs", Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA), Material.WOOD, 1.5F, 1.5F);
	public static final Block stairsFallingBirch = new BlockStairsFalling("birch_stairs", Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH), Material.WOOD, 1.5F, 1.5F);
	public static final Block stairsFallingDarkOak = new BlockStairsFalling("dark_oak_stairs", Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK), Material.WOOD, 1.5F, 1.5F);
	public static final Block stairsFallingJungle = new BlockStairsFalling("jungle_stairs", Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE), Material.WOOD, 1.5F, 1.5F);
	public static final Block stairsFallingOak = new BlockStairsFalling("oak_stairs", Blocks.PLANKS.getDefaultState(), Material.WOOD, 1.5F, 1.5F);
	public static final Block stairsFallingSpruce = new BlockStairsFalling("spruce_stairs", Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE), Material.WOOD, 1.5F, 1.5F);
	
	// Everything else
	public static final Block stairsFallingBrick = new BlockStairsFalling("brick_stairs", Blocks.BRICK_BLOCK.getDefaultState(), Material.GROUND, 2.0F, 2.0F);
	public static final Block stairsFallingNetherBrick = new BlockStairsFalling("nether_brick_stairs", Blocks.NETHER_BRICK.getDefaultState(), Material.GROUND, 2.0F, 2.0F);
	public static final Block stairsFallingPurpur = new BlockStairsFalling("purpur_stairs", Blocks.PURPUR_BLOCK.getDefaultState(), Material.GROUND, 2.0F, 2.0F);
	public static final Block stairsFallingQuartz = new BlockStairsFalling("quartz_stairs", Blocks.QUARTZ_BLOCK.getDefaultState(), Material.GROUND, 2.0F, 2.0F);
	public static final Block stairsFallingRedSandstone = new BlockStairsFalling("red_sandstone_stairs", Blocks.RED_SANDSTONE.getDefaultState(), Material.GROUND, 2.0F, 2.0F);
	public static final Block stairsFallingSandstone = new BlockStairsFalling("sandstone_stairs", Blocks.BRICK_BLOCK.getDefaultState(), Material.GROUND, 2.0F, 2.0F);
	public static final Block stairsFallingStoneBrick = new BlockStairsFalling("stone_brick_stairs", Blocks.STONEBRICK.getDefaultState(), Material.GROUND, 2.0F, 2.0F);
	public static final Block stairsFallingStone = new BlockStairsFalling("stone_stairs", Blocks.COBBLESTONE.getDefaultState(), Material.GROUND, 2.0F, 2.0F);

}
