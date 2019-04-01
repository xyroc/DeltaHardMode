package xiroc.deltahard.util;

import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCocoa;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import xiroc.deltahard.DeltaHard;
import xiroc.deltahard.common.block.DeltaHardBlocks;

public class EventManager {

	@SubscribeEvent
	public void onBlockRegistry(RegistryEvent.Register<Block> event) {
		FallingBlockHelper.gravityBlocks.add(Blocks.DIRT);
		FallingBlockHelper.gravityBlocks.add(Blocks.COBBLESTONE);
		FallingBlockHelper.gravityBlocks.add(Blocks.ACACIA_STAIRS);
		FallingBlockHelper.gravityBlocks.add(Blocks.BIRCH_STAIRS);
		FallingBlockHelper.gravityBlocks.add(Blocks.DARK_OAK_STAIRS);
		FallingBlockHelper.gravityBlocks.add(Blocks.JUNGLE_STAIRS);
		FallingBlockHelper.gravityBlocks.add(Blocks.OAK_STAIRS);
		FallingBlockHelper.gravityBlocks.add(Blocks.SPRUCE_STAIRS);
		FallingBlockHelper.gravityBlocks.add(Blocks.PLANKS);
		FallingBlockHelper.gravityBlocks.add(Blocks.WOODEN_SLAB);
		FallingBlockHelper.gravityBlocks.add(Blocks.DOUBLE_WOODEN_SLAB);
	}

	@SubscribeEvent
	public void onItemRegistry(RegistryEvent.Register<Item> event) {
	}

	@SubscribeEvent
	public void registerRenderers(ModelRegistryEvent event) {
	}

	@SubscribeEvent
	public void onRecipeRegistry(RegistryEvent.Register<IRecipe> event) {
		ConfigHelper.loadConfig();
		DeltaHard.logger.info("Modifying Recipes");
		IForgeRegistryModifiable registry = (IForgeRegistryModifiable) event.getRegistry();
		registry.remove(new ResourceLocation("minecraft:golden_carrot"));
		registry.remove(new ResourceLocation("minecraft:speckled_melon"));
	}

	@SubscribeEvent
	public void loadloot(LootTableLoadEvent event) {
		DeltaHard.logger.info("Loot Table " + event.getName());
		switch (event.getName().toString()) {
		case "minecraft:chests/stronghold_corridor": {
			if (!ConfigHelper.getProperty("LOOT_OBSIDIAN"))
				return;
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			LootEntryItem obsidian = new LootEntryItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 20, 1, new LootFunction[0], new LootCondition[0], "deltahard:obsidian");
			LootPool pool = new LootPool(new LootEntry[] { obsidian }, new LootCondition[0], new RandomValueRange(2), new RandomValueRange(1, 2), "deltahard:obsidian_pool");
			event.getTable().addPool(pool);
			break;
		}
		case "minecraft:chests/stronghold_crossing": {
			if (!ConfigHelper.getProperty("LOOT_OBSIDIAN"))
				return;
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			LootEntryItem obsidian = new LootEntryItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 20, 1, new LootFunction[0], new LootCondition[0], "deltahard:obsidian");
			LootPool pool = new LootPool(new LootEntry[] { obsidian }, new LootCondition[0], new RandomValueRange(2), new RandomValueRange(1, 2), "deltahard:obsidian_pool");
			event.getTable().addPool(pool);
			break;
		}
		case "minecraft:chests/simple_dungeon": {
			if (!ConfigHelper.getProperty("LOOT_OBSIDIAN"))
				return;
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			LootEntryItem obsidian = new LootEntryItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 15, 1, new LootFunction[0], new LootCondition[0], "deltahard:obsidian");
			LootPool pool = new LootPool(new LootEntry[] { obsidian }, new LootCondition[0], new RandomValueRange(2), new RandomValueRange(1, 2), "deltahard:obsidian_pool");
			event.getTable().addPool(pool);
			break;
		}
		case "minecraft:chests/nether_bridge": {
			if (!ConfigHelper.getProperty("LOOT_OBSIDIAN"))
				return;
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			LootEntryItem obsidian = new LootEntryItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 10, 1, new LootFunction[0], new LootCondition[0], "deltahard:obsidian");
			LootPool pool = new LootPool(new LootEntry[] { obsidian }, new LootCondition[0], new RandomValueRange(2), new RandomValueRange(1, 2), "deltahard:obsidian_pool");
			event.getTable().addPool(pool);
			break;
		}
		case "minecraft:chests/abandoned_mineshaft": {
			if (!ConfigHelper.getProperty("LOOT_OBSIDIAN"))
				return;
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			LootEntryItem obsidian = new LootEntryItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 15, 1, new LootFunction[0], new LootCondition[0], "deltahard:obsidian");
			LootPool pool = new LootPool(new LootEntry[] { obsidian }, new LootCondition[0], new RandomValueRange(2), new RandomValueRange(1, 2), "deltahard:obsidian_pool");
			event.getTable().addPool(pool);
			break;
		}
		case "minecraft:chests/jungle_temple": {
			if (!ConfigHelper.getProperty("LOOT_OBSIDIAN"))
				return;
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			LootEntryItem obsidian = new LootEntryItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 15, 1, new LootFunction[0], new LootCondition[0], "deltahard:obsidian");
			LootPool pool = new LootPool(new LootEntry[] { obsidian }, new LootCondition[0], new RandomValueRange(2), new RandomValueRange(1, 2), "deltahard:obsidian_pool");
			event.getTable().addPool(pool);
			break;
		}
		case "minecraft:chests/village_blacksmith": {
			if (!ConfigHelper.getProperty("LOOT_OBSIDIAN"))
				return;
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			LootEntryItem obsidian = new LootEntryItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 20, 1, new LootFunction[0], new LootCondition[0], "deltahard:obsidian");
			LootPool pool = new LootPool(new LootEntry[] { obsidian }, new LootCondition[0], new RandomValueRange(2), new RandomValueRange(1, 1), "deltahard:obsidian_pool");
			event.getTable().addPool(pool);
			break;
		}
		}
	}

	@SubscribeEvent
	public void onBonemeal(BonemealEvent event) {
		if (event.getEntityPlayer().world.isRemote)
			return;
		if (!(event.getBlock().getBlock() instanceof BlockCrops))
			return;
		if (!ConfigHelper.getProperty("CHANGE_BONEMEAL"))
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
		if (event.getItemStack().getItem() == Items.BUCKET && ConfigHelper.getProperty("NO_OBSIDIAN")) {
			RayTraceResult raytraceresult = RayTrace.rayTrace(event.getWorld(), event.getEntityPlayer(), true);
			if (event.getWorld().getBlockState(raytraceresult.getBlockPos()).getMaterial() == Material.LAVA)
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
	}

	@SubscribeEvent
	public void onBlock(RegistryEvent.Register<Block> event) {
		if (ConfigHelper.getProperty("NO_OBSIDIAN"))
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
	public void onNeighborChange(BlockEvent.NeighborNotifyEvent event) {
		if (!ConfigHelper.getProperty("GRAVITY"))
			return;
		event.getWorld().scheduleUpdate(event.getPos(), event.getState().getBlock(), 4);
		FallingBlockHelper.checkFallable(event.getWorld(), event.getPos(), false);
		FallingBlockHelper.updateNeighbors(event.getWorld(), event.getPos(), false);
	}

	@SubscribeEvent
	public void onBlockAdded(BlockEvent.PlaceEvent event) {
		if (event.getWorld().isRemote || !ConfigHelper.getProperty("GRAVITY"))
			return;
		event.getWorld().scheduleUpdate(event.getPos(), event.getState().getBlock(), 4);
		FallingBlockHelper.checkFallable(event.getWorld(), event.getPos(), false);
		FallingBlockHelper.updateNeighbors(event.getWorld(), event.getPos(), false);
	}

	@SubscribeEvent
	public void onDrop(LivingDropsEvent event) {
		if (event.getEntityLiving() instanceof EntitySkeleton && ConfigHelper.getProperty("SKELETON_NO_BOW_DROP")) {
			Iterator<EntityItem> iterator = event.getDrops().iterator();
			while (iterator.hasNext()) {
				EntityItem item = iterator.next();
				if (item.getItem().getItem() == Items.BOW)
					iterator.remove();
			}
			return;
		}
		if (event.getEntityLiving() instanceof EntitySpider && ConfigHelper.getProperty("SPIDER_NO_STRING_DROP")) {
			Iterator<EntityItem> iterator = event.getDrops().iterator();
			while (iterator.hasNext()) {
				EntityItem item = iterator.next();
				if (item.getItem().getItem() == Items.STRING)
					iterator.remove();
			}
		}

	}

	@SubscribeEvent
	public void onSleep(PlayerSleepInBedEvent event) {
		if (event.getEntityPlayer().world.isRemote)
			return;
		if (event.getEntityPlayer().getEntityWorld().provider.getDimension() != 0)
			return;
		if (!ConfigHelper.getProperty("NO_SLEEP"))
			return;
		event.setResult(SleepResult.OTHER_PROBLEM);
		event.getEntityPlayer().setSpawnPoint(event.getEntityPlayer().getPosition(), true);
	}

}
