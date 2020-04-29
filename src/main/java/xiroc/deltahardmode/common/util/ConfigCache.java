package xiroc.deltahardmode.common.util;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;

public class ConfigCache {

	public static ArrayList<Block> gravityBlocks = new ArrayList<Block>();
	public static ArrayList<BlockState> gravityStates = new ArrayList<BlockState>();
	public static ArrayList<String> dropBlocks = new ArrayList<String>();
	public static ArrayList<BlockState> dropStates = new ArrayList<BlockState>();
	public static ArrayList<String> removedDrops = new ArrayList<String>();
	public static ArrayList<Item> removedItems = new ArrayList<Item>();

}
