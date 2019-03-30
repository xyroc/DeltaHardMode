package xiroc.deltahard.util;

import java.awt.event.ItemEvent;
import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockObsidian;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.CropGrowEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import xiroc.deltahard.DeltaHard;

public class EventManager {

	@SubscribeEvent
	public void onRecipeRegistry(RegistryEvent.Register<IRecipe> event) {
		DeltaHard.logger.info("Modifying Recipes");
		IForgeRegistryModifiable registry = (IForgeRegistryModifiable) event.getRegistry();
		registry.remove(new ResourceLocation("minecraft:golden_carrot"));
		registry.remove(new ResourceLocation("minecraft:speckled_melon"));
	}

	@SubscribeEvent
	public void loadloot(LootTableLoadEvent event) {
		// minecraft:chests/stronghold_corridor, minecraft:chests/stronghold_crossing,
		// chests/simple_dungeon, minecraft:chests/nether_bridge,
		// minecraft:chests/abandoned_mineshaft, minecraft:chests/jungle_temple,
		// minecraft:chests/desert_pyramid, minecraft:chests/village_blacksmith
		switch (event.getName().toString()) {
		case "minecraft:chests/stronghold_corridor": {
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			LootEntryItem obsidian = new LootEntryItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 20, 1,
					new LootFunction[0], new LootCondition[0], "deltahard:obsidian");
			LootPool pool = new LootPool(new LootEntry[] { obsidian }, new LootCondition[0], new RandomValueRange(2),
					new RandomValueRange(1, 2), "deltahard:obsidian_pool");
			event.getTable().addPool(pool);
			break;
		}
		case "minecraft:chests/stronghold_crossing": {
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			LootEntryItem obsidian = new LootEntryItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 20, 1,
					new LootFunction[0], new LootCondition[0], "deltahard:obsidian");
			LootPool pool = new LootPool(new LootEntry[] { obsidian }, new LootCondition[0], new RandomValueRange(2),
					new RandomValueRange(1, 2), "deltahard:obsidian_pool");
			event.getTable().addPool(pool);
			break;
		}
		case "minecraft:chests/simple_dungeon": {
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			LootEntryItem obsidian = new LootEntryItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 15, 1,
					new LootFunction[0], new LootCondition[0], "deltahard:obsidian");
			LootPool pool = new LootPool(new LootEntry[] { obsidian }, new LootCondition[0], new RandomValueRange(2),
					new RandomValueRange(1, 2), "deltahard:obsidian_pool");
			event.getTable().addPool(pool);
			break;
		}
		case "minecraft:chests/nether_bridge": {
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			LootEntryItem obsidian = new LootEntryItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 10, 1,
					new LootFunction[0], new LootCondition[0], "deltahard:obsidian");
			LootPool pool = new LootPool(new LootEntry[] { obsidian }, new LootCondition[0], new RandomValueRange(2),
					new RandomValueRange(1, 2), "deltahard:obsidian_pool");
			event.getTable().addPool(pool);
			break;
		}
		case "minecraft:chests/abandoned_mineshaft": {
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			LootEntryItem obsidian = new LootEntryItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 15, 1,
					new LootFunction[0], new LootCondition[0], "deltahard:obsidian");
			LootPool pool = new LootPool(new LootEntry[] { obsidian }, new LootCondition[0], new RandomValueRange(2),
					new RandomValueRange(1, 2), "deltahard:obsidian_pool");
			event.getTable().addPool(pool);
			break;
		}
		case "minecraft:chests/jungle_temple": {
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			LootEntryItem obsidian = new LootEntryItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 15, 1,
					new LootFunction[0], new LootCondition[0], "deltahard:obsidian");
			LootPool pool = new LootPool(new LootEntry[] { obsidian }, new LootCondition[0], new RandomValueRange(2),
					new RandomValueRange(1, 2), "deltahard:obsidian_pool");
			event.getTable().addPool(pool);
			break;
		}
		case "minecraft:chests/village_blacksmith": {
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			LootEntryItem obsidian = new LootEntryItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 20, 1,
					new LootFunction[0], new LootCondition[0], "deltahard:obsidian");
			LootPool pool = new LootPool(new LootEntry[] { obsidian }, new LootCondition[0], new RandomValueRange(2),
					new RandomValueRange(1, 1), "deltahard:obsidian_pool");
			event.getTable().addPool(pool);
			break;
		}
		}
	}

	@SubscribeEvent
	public void onGrow(BonemealEvent event) {
		if (event.getEntityPlayer().world.isRemote)
			return;
		if (!(event.getBlock().getBlock() instanceof BlockCrops))
			return;
		event.setCanceled(true);
		IBlockState iblockstate = event.getBlock();
		World worldIn = event.getEntityPlayer().world;
		BlockCrops block = (BlockCrops) iblockstate.getBlock();
		if (iblockstate.getBlock() instanceof IGrowable) {
			IGrowable igrowable = (IGrowable) iblockstate.getBlock();
			if (igrowable.canGrow(worldIn, event.getPos(), iblockstate, worldIn.isRemote)) {
				if (!worldIn.isRemote) {
					if (igrowable.canUseBonemeal(worldIn, worldIn.rand, event.getPos(), iblockstate)) {
						int i = ((Integer) iblockstate.getValue(BlockCrops.AGE)).intValue() + 1;
						int j = block.getMaxAge();
						if (i > j)
							i = j;
						worldIn.setBlockState(event.getPos(), block.withAge(i), 2);
						ItemDye.spawnBonemealParticles(worldIn, event.getPos(), 2);
						event.getEntityPlayer().swingArm(event.getHand());
					}
					event.getEntityPlayer().getActiveItemStack().shrink(1);
				}
			}
		}
	}

	@SubscribeEvent
	public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		if (event.getItemStack().getItem() == Items.BUCKET) {
			RayTraceResult raytraceresult = RayTrace.rayTrace(event.getWorld(), event.getEntityPlayer(), true);
			if (event.getWorld().getBlockState(raytraceresult.getBlockPos()).getMaterial() == Material.LAVA)
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onBlock(RegistryEvent.Register<Block> event) {
		Blocks.OBSIDIAN.setHarvestLevel("pickaxe", 5);
	}

	@SubscribeEvent
	public void onBlockDestroyed(BlockEvent.BreakEvent event) {

	}

	@SubscribeEvent
	public void onBlockHarvestDrops(BlockEvent.HarvestDropsEvent event) {
		if (event.getWorld().isRemote)
			return;
		if (event.getState().getBlock() instanceof BlockLeaves) {
			event.getDrops().clear();
			return;
		}
		if (event.getState().getBlock() instanceof BlockTallGrass) {
			event.getDrops().clear();
			return;
		}
	}

	@SubscribeEvent
	public void onSleep(PlayerSleepInBedEvent event) {
		if (event.getEntityPlayer().world.isRemote)
			return;
		if(event.getEntityPlayer().getEntityWorld().provider.getDimension() != 0) return;
		event.setResult(SleepResult.OTHER_PROBLEM);
		event.getEntityPlayer().setSpawnPoint(event.getEntityPlayer().getPosition(), true);
	}

}
