package xiroc.deltahard.util;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import xiroc.deltahard.common.block.DeltaHardBlocks;

public class RemovedBlocks {

	// HARDCODED stuff

	public static boolean isBlockRemoved(String registryName) {
		switch (registryName) {
		case "minecraft:acacia_stairs":
			return true;
		case "minecraft:birch_stairs":
			return true;
		case "minecraft:brick_stairs":
			return true;
		case "minecraft:dark_oak_stairs":
			return true;
		case "minecraft:jungle_stairs":
			return true;
		case "minecraft:nether_brick_stairs":
			return true;
		case "minecraft:oak_stairs":
			return true;
		case "minecraft:purpur_stairs":
			return true;
		case "minecraft:quartz_stairs":
			return true;
		case "minecraft:red_sandstone_stairs":
			return true;
		case "minecraft:sandstone_stairs":
			return true;
		case "minecraft:spruce_stairs":
			return true;
		case "minecraft:stone_brick_stairs":
			return true;
		case "minecraft:stone_stairs":
			return true;
		case "minecraft:acacia_wooden_slab":
			return false;
		case "minecraft:birch_wooden_slab":
			return false;
		case "minecraft:brick_slab":
			return false;
		case "minecraft:cobblestone_slab":
			return false;
		case "minecraft:dark_oak_wooden_slab":
			return false;
		case "minecraft:jungle_wooden_slab":
			return false;
		case "minecraft:nether_brick_slab":
			return false;
		case "minecraft:oak_wooden_slab":
			return false;
		case "minecraft:purpur_slab":
			return false;
		case "minecraft:quartz_slab":
			return false;
		case "minecraft:red_sandstone_slab":
			return false;
		case "minecraft:sandstone_slab":
			return false;
		case "minecraft:spruce_wooden_slab":
			return false;
		case "minecraft:stone_brick_slab":
			return false;
		case "minecraft:stone_slab":
			return false;
		default:
			return false;
		}
	}

	public static Item getEquivalent(String registryName) {
		switch (registryName) {
		case "minecraft:acacia_stairs":
			return Item.getItemFromBlock(DeltaHardBlocks.stairsFallingAcacia);
		case "minecraft:birch_stairs":
			return Item.getItemFromBlock(DeltaHardBlocks.stairsFallingBirch);
		case "minecraft:brick_stairs":
			return Item.getItemFromBlock(DeltaHardBlocks.stairsFallingBrick);
		case "minecraft:dark_oak_stairs":
			return Item.getItemFromBlock(DeltaHardBlocks.stairsFallingDarkOak);
		case "minecraft:jungle_stairs":
			return Item.getItemFromBlock(DeltaHardBlocks.stairsFallingJungle);
		case "minecraft:nether_brick_stairs":
			return Item.getItemFromBlock(DeltaHardBlocks.stairsFallingNetherBrick);
		case "minecraft:oak_stairs":
			return Item.getItemFromBlock(DeltaHardBlocks.stairsFallingOak);
		case "minecraft:purpur_stairs":
			return Item.getItemFromBlock(DeltaHardBlocks.stairsFallingPurpur);
		case "minecraft:quartz_stairs":
			return Item.getItemFromBlock(DeltaHardBlocks.stairsFallingQuartz);
		case "minecraft:red_sandstone_stairs":
			return Item.getItemFromBlock(DeltaHardBlocks.stairsFallingRedSandstone);
		case "minecraft:sandstone_stairs":
			return Item.getItemFromBlock(DeltaHardBlocks.stairsFallingSandstone);
		case "minecraft:spruce_stairs":
			return Item.getItemFromBlock(DeltaHardBlocks.stairsFallingSpruce);
		case "minecraft:stone_brick_stairs":
			return Item.getItemFromBlock(DeltaHardBlocks.stairsFallingStoneBrick);
		case "minecraft:stone_stairs":
			return Item.getItemFromBlock(DeltaHardBlocks.stairsFallingStone);
		case "minecraft:acacia_wooden_slab":
			return null;
		case "minecraft:birch_wooden_slab":
			return null;
		case "minecraft:brick_slab":
			return null;
		case "minecraft:cobblestone_slab":
			return null;
		case "minecraft:dark_oak_wooden_slab":
			return null;
		case "minecraft:jungle_wooden_slab":
			return null;
		case "minecraft:nether_brick_slab":
			return null;
		case "minecraft:oak_wooden_slab":
			return null;
		case "minecraft:purpur_slab":
			return null;
		case "minecraft:quartz_slab":
			return null;
		case "minecraft:red_sandstone_slab":
			return null;
		case "minecraft:sandstone_slab":
			return null;
		case "minecraft:spruce_wooden_slab":
			return null;
		case "minecraft:stone_brick_slab":
			return null;
		case "minecraft:stone_slab":
			return null;
		default:
			return null;
		}
	}

}
