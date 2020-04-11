package xiroc.deltahard.common.util;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing.Plane;
import xiroc.deltahard.DeltaHard;

public class ConfigCache {

	public static ArrayList<Block> gravityBlocks = new ArrayList();
	public static ArrayList<IBlockState> gravityStates = new ArrayList();
	public static HashMap<IBlockState, Item[]> bannedDrops = new HashMap<IBlockState, Item[]>();

	public static void load() {
		DeltaHard.logger.info("Loading Config Cache");

		if (Config.getProperty("GRAVITY")) {
			bannedDrops.put(
					Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.BIRCH),
					new Item[] { Item.getItemFromBlock(Blocks.PLANKS) });
			bannedDrops.put(
					Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.DARK_OAK),
					new Item[] { Item.getItemFromBlock(Blocks.PLANKS) });
		}

	}

}
