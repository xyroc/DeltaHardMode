package xiroc.deltahard.common.util;

import java.awt.List;
import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockNewLog;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStone;
import net.minecraft.block.BlockStoneBrick;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockStoneSlabNew;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityHusk;
import net.minecraft.entity.monster.EntityIllusionIllager;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityStray;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityWitherSkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.passive.EntityZombieHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayer.SleepResult;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityDragonFireball;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import xiroc.deltahard.DeltaHard;
import xiroc.deltahard.common.loot.LootFunctionBase;
import xiroc.deltahard.common.loot.LootFunctionSaplings;
import xiroc.deltahard.common.loot.LootFunctionTreasureMap;
import xiroc.deltahard.common.loot.LootPoolItem;
import xiroc.deltahard.common.network.PacketClientExplosion;

public class EventManager {

	@SubscribeEvent
	public void onBlockRegistry(RegistryEvent.Register<Block> event) {
		ConfigCache.gravityBlocks.add(Blocks.GRASS);
		ConfigCache.gravityBlocks.add(Blocks.DIRT);
		ConfigCache.gravityBlocks.add(Blocks.COBBLESTONE);
		ConfigCache.gravityBlocks.add(Blocks.ACACIA_STAIRS);
		ConfigCache.gravityBlocks.add(Blocks.BIRCH_STAIRS);
		ConfigCache.gravityBlocks.add(Blocks.DARK_OAK_STAIRS);
		ConfigCache.gravityBlocks.add(Blocks.JUNGLE_STAIRS);
		ConfigCache.gravityBlocks.add(Blocks.OAK_STAIRS);
		ConfigCache.gravityBlocks.add(Blocks.SPRUCE_STAIRS);
		ConfigCache.gravityBlocks.add(Blocks.WOODEN_SLAB);
		ConfigCache.gravityBlocks.add(Blocks.DOUBLE_WOODEN_SLAB);
		ConfigCache.gravityBlocks.add(Blocks.STONE_STAIRS);

		ConfigCache.gravityStates.add(Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.COBBLESTONE));
		ConfigCache.gravityStates.add(Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.COBBLESTONE).withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP));
		ConfigCache.gravityStates.add(Blocks.DOUBLE_STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.COBBLESTONE));
		ConfigCache.gravityStates.add(Blocks.STONEBRICK.getDefaultState().withProperty(BlockStoneBrick.VARIANT, BlockStoneBrick.EnumType.CRACKED));
		ConfigCache.gravityStates.add(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.ACACIA));
		ConfigCache.gravityStates.add(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.OAK));
		ConfigCache.gravityStates.add(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.JUNGLE));
		ConfigCache.gravityStates.add(Blocks.PLANKS.getDefaultState().withProperty(BlockPlanks.VARIANT, BlockPlanks.EnumType.SPRUCE));

		ConfigCache.dropStates.add(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH).withProperty(BlockOldLog.LOG_AXIS, BlockLog.EnumAxis.X));
		ConfigCache.dropStates.add(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH).withProperty(BlockOldLog.LOG_AXIS, BlockLog.EnumAxis.Y));
		ConfigCache.dropStates.add(Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH).withProperty(BlockOldLog.LOG_AXIS, BlockLog.EnumAxis.Z));
		ConfigCache.dropStates.add(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.X));
		ConfigCache.dropStates.add(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Y));
		ConfigCache.dropStates.add(Blocks.LOG2.getDefaultState().withProperty(BlockNewLog.VARIANT, BlockPlanks.EnumType.DARK_OAK).withProperty(BlockNewLog.LOG_AXIS, BlockLog.EnumAxis.Z));
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
		switch (event.getName().toString()) {
		case "minecraft:chests/stronghold_corridor": {
			if (!ConfigHelper.getProperty("LOOT"))
				return;
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			event.getTable().addPool(new LootPoolItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 3, 5, new LootEntry[0], new LootCondition[0], new RandomValueRange(0), new RandomValueRange(0), "deltahard:obsidian"));
			break;
		}
		case "minecraft:chests/stronghold_library": {
			if (!ConfigHelper.getProperty("LOOT"))
				return;
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			event.getTable().addPool(new LootPool(new LootEntry[] { new LootEntryItem(Items.MAP, 1, 1, new LootFunction[] { new LootFunctionTreasureMap(new LootCondition[0]) }, new LootCondition[0], "deltahard:treasure_map") }, new LootCondition[0], new RandomValueRange(1), new RandomValueRange(0), "deltahard:treasure_map_pool"));
			break;
		}
		case "minecraft:chests/stronghold_crossing": {
			if (!ConfigHelper.getProperty("LOOT"))
				return;
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			event.getTable().addPool(new LootPoolItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 3, 5, new LootEntry[0], new LootCondition[0], new RandomValueRange(0), new RandomValueRange(0), "deltahard:obsidian"));
			// event.getTable().addPool(new LootPool(new LootEntry[] { new
			// LootEntryItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 5, 1, new
			// LootFunction[] { new LootFunctionBase(new LootCondition[0], 1, 5) }, new
			// LootCondition[0], "deltahard:obsidian") }, new LootCondition[0], new
			// RandomValueRange(1), new RandomValueRange(0), "deltahard:obsidian_pool"));
			break;
		}
		case "minecraft:chests/simple_dungeon": {
			if (!ConfigHelper.getProperty("LOOT"))
				return;
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			event.getTable().addPool(new LootPoolItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 3, 5, new LootEntry[0], new LootCondition[0], new RandomValueRange(0), new RandomValueRange(0), "deltahard:obsidian"));
			break;
		}
		case "minecraft:chests/nether_bridge": {
			if (!ConfigHelper.getProperty("LOOT"))
				return;
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			event.getTable().addPool(new LootPoolItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 3, 5, new LootEntry[0], new LootCondition[0], new RandomValueRange(0), new RandomValueRange(0), "deltahard:obsidian"));
			break;
		}
		case "minecraft:chests/abandoned_mineshaft": {
			if (!ConfigHelper.getProperty("LOOT"))
				return;
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			event.getTable().addPool(new LootPoolItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 3, 5, new LootEntry[0], new LootCondition[0], new RandomValueRange(0), new RandomValueRange(0), "deltahard:obsidian"));
			event.getTable().addPool(new LootPool(new LootEntry[] { new LootEntryItem(Items.WHEAT_SEEDS, 1, 1, new LootFunction[0], new LootCondition[0], "deltahard:wheat_seeds"), new LootEntryItem(Item.getItemFromBlock(Blocks.SAPLING), 1, 1, new LootFunction[] { new LootFunctionSaplings(new LootCondition[0]) }, new LootCondition[0], "deltahard:sapling") }, new LootCondition[0], new RandomValueRange(1, 8), new RandomValueRange(0, 3), "deltahard:loot_pool_seeds_and_saplings"));
			break;
		}
		case "minecraft:chests/jungle_temple": {
			if (!ConfigHelper.getProperty("LOOT"))
				return;
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			event.getTable().addPool(new LootPoolItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 3, 5, new LootEntry[0], new LootCondition[0], new RandomValueRange(0), new RandomValueRange(0), "deltahard:obsidian"));
			break;
		}
		case "minecraft:chests/desert_pyramid": {
			if (!ConfigHelper.getProperty("LOOT"))
				return;
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			event.getTable().addPool(new LootPoolItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 3, 5, new LootEntry[0], new LootCondition[0], new RandomValueRange(0), new RandomValueRange(0), "deltahard:obsidian"));
			break;
		}
		case "minecraft:chests/village_blacksmith": {
			if (!ConfigHelper.getProperty("LOOT"))
				return;
			DeltaHard.logger.info("Modifying LootTable: " + event.getName().toString());
			event.getTable().getPool("main").removeEntry("minecraft:obsidian");
			event.getTable().addPool(new LootPoolItem(Item.getItemFromBlock(Blocks.OBSIDIAN), 3, 5, new LootEntry[0], new LootCondition[0], new RandomValueRange(0), new RandomValueRange(0), "deltahard:obsidian"));
			break;
		}
		}
	}

	@SubscribeEvent
	public void onBonemeal(BonemealEvent event) {
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
						event.getStack().shrink(1);
						event.getEntityPlayer().swingArm(event.getHand());
					}
				}
			}
			ItemDye.spawnBonemealParticles(worldIn, event.getPos(), 5);
		}
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if (event.player.world.isRemote)
			return;
		if (event.player.getSleepTimer() >= 80 && ConfigHelper.getProperty("NO_SLEEP")) {
			event.player.wakeUpPlayer(true, false, true);
			event.player.sendStatusMessage(new TextComponentString("I cant sleep now..."), true);
		}
	}

	@SubscribeEvent
	public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		if (event.getItemStack().getItem() == Items.BUCKET && ConfigHelper.getProperty("NO_OBSIDIAN")) {
			RayTraceResult raytraceresult = RayTrace.rayTrace(event.getWorld(), event.getEntityPlayer(), true);
			if (raytraceresult != null && event.getWorld().getBlockState(raytraceresult.getBlockPos()).getMaterial() == Material.LAVA)
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onBlock(RegistryEvent.Register<Block> event) {
		if (ConfigHelper.getProperty("NO_OBSIDIAN"))
			Blocks.OBSIDIAN.setHarvestLevel("pickaxe", 5);
	}

	@SubscribeEvent
	public void onBlockHarvestDrops(BlockEvent.HarvestDropsEvent event) {
		if (event.getWorld().isRemote)
			return;
		// Hardcoded Stuff dont do this at home
		if (ConfigCache.dropStates.contains(event.getState())) {
			Iterator<ItemStack> iterator = event.getDrops().iterator();
			while (iterator.hasNext()) {
				ItemStack item = iterator.next();
				if (item.getItemDamage() == BlockPlanks.EnumType.BIRCH.getMetadata() || item.getItemDamage() == BlockPlanks.EnumType.DARK_OAK.getMetadata() - 4)
					iterator.remove();
				return;
			}
		}
		if (ConfigCache.dropBlocks.contains(event.getState().getBlock().getRegistryName().toString())) {
			Iterator<ItemStack> iterator = event.getDrops().iterator();
			while (iterator.hasNext()) {
				ItemStack item = iterator.next();
				if (ConfigCache.removedDrops.contains(item.getItem().getRegistryName().toString())) {
					iterator.remove();
				}
				return;
			}
		}
	}

	@SubscribeEvent
	public void onNeighborChange(BlockEvent.NeighborNotifyEvent event) {
		if (!event.getWorld().getChunkFromBlockCoords(event.getPos()).wasTicked())
			return;
		if (!ConfigHelper.getProperty("GRAVITY"))
			return;
		event.getWorld().scheduleUpdate(event.getPos(), event.getState().getBlock(), 4);
		FallingBlockHelper.checkFallable(event.getWorld(), event.getPos());
		FallingBlockHelper.updateNeighbors(event.getWorld(), event.getPos());
	}

	@SubscribeEvent
	public void onBlockAdded(BlockEvent.PlaceEvent event) {
		if (!event.getWorld().getChunkFromBlockCoords(event.getPos()).wasTicked())
			return;
		if (event.getWorld().isRemote || !ConfigHelper.getProperty("GRAVITY"))
			return;
		event.getWorld().scheduleUpdate(event.getPos(), event.getState().getBlock(), 4);
		FallingBlockHelper.checkFallable(event.getWorld(), event.getPos());
		FallingBlockHelper.updateNeighbors(event.getWorld(), event.getPos());
	}

	@SubscribeEvent
	public void onDrop(LivingDropsEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (entity instanceof EntityCow) {
			if (!containsEntityItem(event.getDrops().iterator(), Items.LEATHER))
				event.getDrops().add(new EntityItem(entity.getEntityWorld(), entity.posX, entity.posY, entity.posZ, new ItemStack(Items.LEATHER)));
			return;
		}
		if ((entity instanceof EntitySkeleton || entity instanceof EntityStray) && ConfigHelper.getProperty("SKELETON_NO_BOW_DROP")) {
			Iterator<EntityItem> iterator = event.getDrops().iterator();
			while (iterator.hasNext()) {
				EntityItem item = iterator.next();
				if (item.getItem().getItem() == Items.BOW)
					iterator.remove();
			}
			return;
		}
		if (entity instanceof EntitySpider && ConfigHelper.getProperty("SPIDER_NO_STRING_DROP")) {
			Iterator<EntityItem> iterator = event.getDrops().iterator();
			while (iterator.hasNext()) {
				EntityItem item = iterator.next();
				if (item.getItem().getItem() == Items.STRING)
					iterator.remove();
			}
			return;
		}
		if (entity instanceof EntityEnderman) {
			Iterator<EntityItem> iterator = event.getDrops().iterator();
			while (iterator.hasNext()) {
				EntityItem item = iterator.next();
				if (item.getItem().getItem() == Items.ENDER_PEARL) {
					int count = item.getItem().getCount();
					item.setItem(new ItemStack(item.getItem().getItem(), count >= 16 ? count : count + entity.world.rand.nextInt(2)));
				}
			}
			return;
		}
	}

	@SubscribeEvent
	public void onAttack(AttackEntityEvent event) {
		if (event.getTarget() instanceof EntityLargeFireball) {
			EntityLargeFireball fireball = (EntityLargeFireball) event.getTarget();
			event.setCanceled(true);
			fireball.setDead();
			if (!fireball.world.isRemote) {
				boolean mobGriefing = fireball.getEntityWorld().getGameRules().getBoolean("mobGriefing");
				Explosion explosion = ExplosionHelper.newExplosion(fireball, fireball.getEntityWorld(), fireball.posX, fireball.posY, fireball.posZ, 2, mobGriefing, mobGriefing);
				DeltaHard.NET.sendToAllAround(new PacketClientExplosion(fireball.posX, fireball.posY, fireball.posZ, 2, mobGriefing, explosion.getAffectedBlockPositions()), new TargetPoint(fireball.dimension, fireball.posX, fireball.posY, fireball.posZ, 64.0D));
			}
			return;
		}
	}

	@SubscribeEvent
	public void onArrowImpact(ProjectileImpactEvent.Arrow event) {
		if (event.getEntity().world.isRemote)
			return;
		if (event.getRayTraceResult().entityHit instanceof EntityLargeFireball) {
			EntityLargeFireball fireball = (EntityLargeFireball) event.getRayTraceResult().entityHit;
			event.setCanceled(true);
			fireball.setDead();
			if (!fireball.world.isRemote) {
				boolean mobGriefing = fireball.getEntityWorld().getGameRules().getBoolean("mobGriefing");
				Explosion explosion = ExplosionHelper.newExplosion(fireball.shootingEntity, fireball.getEntityWorld(), fireball.posX, fireball.posY, fireball.posZ, 1.5F, mobGriefing, mobGriefing);
				DeltaHard.NET.sendToAllAround(new PacketClientExplosion(fireball.posX, fireball.posY, fireball.posZ, 2, mobGriefing, explosion.getAffectedBlockPositions()), new TargetPoint(fireball.dimension, fireball.posX, fireball.posY, fireball.posZ, 64.0D));
			}
			return;
		}
	}

	@SubscribeEvent
	public void onFireballImpact(ProjectileImpactEvent.Fireball event) {
		if (event.getEntity().world.isRemote)
			return;
		if (event.getEntity() instanceof EntityLargeFireball) {
			EntityLargeFireball fireball = (EntityLargeFireball) event.getEntity();
			// event.setCanceled(true);
			fireball.setDead();
			if (!fireball.world.isRemote) {
				boolean mobGriefing = fireball.getEntityWorld().getGameRules().getBoolean("mobGriefing");
				Explosion explosion = ExplosionHelper.newExplosion(fireball.shootingEntity, fireball.getEntityWorld(), fireball.posX, fireball.posY, fireball.posZ, 1.5F, mobGriefing, mobGriefing);
				DeltaHard.NET.sendToAllAround(new PacketClientExplosion(fireball.posX, fireball.posY, fireball.posZ, 2, mobGriefing, explosion.getAffectedBlockPositions()), new TargetPoint(fireball.dimension, fireball.posX, fireball.posY, fireball.posZ, 64.0D));
			}
			return;
		}
		if (event.getEntity() instanceof EntityDragonFireball && !(event.getRayTraceResult().entityHit instanceof EntityDragon)) {
			EntityDragonFireball fireball = (EntityDragonFireball) event.getEntity();
			if (!fireball.world.isRemote && !fireball.isDead) {
				boolean mobGriefing = fireball.getEntityWorld().getGameRules().getBoolean("mobGriefing");
				Explosion explosion = ExplosionHelper.newExplosion(fireball.shootingEntity, fireball.getEntityWorld(), fireball.posX, fireball.posY, fireball.posZ, 1F, false, mobGriefing);
				DeltaHard.NET.sendToAllAround(new PacketClientExplosion(fireball.posX, fireball.posY, fireball.posZ, 2, mobGriefing, explosion.getAffectedBlockPositions()), new TargetPoint(fireball.dimension, fireball.posX, fireball.posY, fireball.posZ, 64.0D));
			}
			fireball.setDead();
		}
	}

	@SubscribeEvent
	public void onDamage(LivingDamageEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if ((entity instanceof EntitySpider || entity instanceof EntitySlime) && event.getSource() == DamageSource.FALL) {
			event.setCanceled(true);
			return;
		}
		if (entity instanceof EntityPlayer && event.getSource() instanceof EntityDamageSource && ((EntityDamageSource) event.getSource()).getTrueSource() instanceof EntityBlaze) {
			entity.setFire(4);
			return;
		}
	}

	@SubscribeEvent
	public void onExplosionStart(ExplosionEvent.Start event) {
		EntityLivingBase entity = event.getExplosion().getExplosivePlacedBy();
		if (entity == null)
			return;
		boolean mobGriefing = entity.getEntityWorld().getGameRules().getBoolean("mobGriefing");
		if (entity instanceof EntityCreeper) {
			event.setCanceled(true);
			EntityCreeper creeper = (EntityCreeper) entity;
			Explosion explosion = ExplosionHelper.newExplosion(creeper, event.getWorld(), creeper.posX, creeper.posY, creeper.posZ, 3 * (creeper.getPowered() ? 2 : 1), mobGriefing, mobGriefing);
			DeltaHard.NET.sendToAllAround(new PacketClientExplosion(creeper.posX, creeper.posY, creeper.posZ, 3 * (creeper.getPowered() ? 2 : 1), mobGriefing, explosion.getAffectedBlockPositions()), new TargetPoint(creeper.dimension, creeper.posX, creeper.posY, creeper.posZ, 64.0D));
			ExplosionHelper.spawnLingeringCloud(creeper);
			entity.setDead();
			return;
		}
		if (entity instanceof EntityWither) {
			event.setCanceled(true);
			EntityWither wither = (EntityWither) entity;
			Explosion explosion = ExplosionHelper.newExplosion(entity, event.getWorld(), entity.posX, entity.posY, entity.posZ, 10, false, mobGriefing);
			DeltaHard.NET.sendToAllAround(new PacketClientExplosion(wither.posX, wither.posY, wither.posZ, 10, mobGriefing, explosion.getAffectedBlockPositions()), new TargetPoint(wither.dimension, wither.posX, wither.posY, wither.posZ, 64.0D));
			return;
		}
	}

	@SubscribeEvent
	public void onEnterChunk(EntityEvent.EnteringChunk event) {
		Entity entity = event.getEntity();
		if (entity.world.isRemote || entity.ticksExisted > 5)
			return;
		if (hasTag(entity))
			return;
		if (entity instanceof EntityAnimal) {
			AnimalHelper.initAnimalTasks((EntityAnimal) entity);
			return;
		}
		tagEntity(entity);
		if (entity instanceof EntityCreeper) {
			EntityCreeper creeper = (EntityCreeper) entity;
			NBTTagCompound nbt = new NBTTagCompound();
			creeper.writeEntityToNBT(nbt);
			nbt.setShort("Fuse", (short) 25);
			creeper.readEntityFromNBT(nbt);
			return;
		}
		if (entity instanceof EntityPigZombie) {
			if (entity.world.rand.nextFloat() <= 0.05) {
				EntityCreeper creeper = new EntityCreeper(entity.world);
				creeper.setPosition(entity.posX, entity.posY, entity.posZ);
				tagEntity(creeper);
				entity.world.spawnEntity(creeper);
				entity.setDead();
			}
			return;
		}
		if (entity instanceof EntityWolf) {
			EntityWolf wolf = (EntityWolf) entity;
			wolf.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
			return;
		}
		if (entity instanceof EntitySquid) {
			if (entity.world.rand.nextFloat() <= 0.07) {
				EntityGuardian guardian = new EntityGuardian(entity.world);
				guardian.setPosition(entity.posX, entity.posY, entity.posZ);
				tagEntity(guardian);
				entity.world.spawnEntity(guardian);
				entity.setDead();
			}
		}
		if (entity instanceof EntityStray) {
			if (entity.dimension == -1) {
				EntityWitherSkeleton skeleton = new EntityWitherSkeleton(entity.world);
				skeleton.setPosition(entity.posX, entity.posY, entity.posZ);
				skeleton.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.BOW));
				entity.world.spawnEntity(skeleton);
				tagEntity(skeleton);
				entity.setDead();
				return;
			}
		}
		if (entity instanceof EntitySkeleton) {
			if (entity.dimension == -1) {
				EntityWitherSkeleton skeleton = new EntityWitherSkeleton(entity.world);
				skeleton.setPosition(entity.posX, entity.posY, entity.posZ);
				skeleton.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.BOW));
				entity.world.spawnEntity(skeleton);
				tagEntity(skeleton);
				entity.setDead();
				return;
			}
			if (entity.dimension == 0) {
				if (!(entity.world.getBiome(entity.getPosition()) == Biomes.ICE_MOUNTAINS || entity.world.getBiome(entity.getPosition()) == Biomes.ICE_PLAINS || entity.world.getBiome(entity.getPosition()) == Biomes.FROZEN_OCEAN || entity.world.getBiome(entity.getPosition()) == Biomes.FROZEN_RIVER)) {
					if (entity.world.rand.nextFloat() <= 0.07) {
						EntityStray stray = new EntityStray(entity.world);
						stray.setPosition(entity.posX, entity.posY, entity.posZ);
						stray.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.BOW));
						entity.world.spawnEntity(stray);
						tagEntity(stray);
						entity.setDead();
						return;
					}
				}
			}
			return;
		}
		if (entity instanceof EntityEvoker) {
			EntityEvoker evoker = (EntityEvoker) entity;
			evoker.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
			evoker.setHealth(evoker.getMaxHealth());
			return;
		}
		if (entity instanceof EntityVindicator) {
			if (entity.world.rand.nextFloat() <= 0.09) {
				EntityIllusionIllager illusionIllager = new EntityIllusionIllager(entity.world);
				illusionIllager.setPosition(entity.posX, entity.posY, entity.posZ);
				tagEntity(illusionIllager);
				entity.world.spawnEntity(illusionIllager);
				entity.setDead();
			}
			return;
		}
		if (entity instanceof EntitySpider) {
			EntitySpider spider = (EntitySpider) entity;
			spider.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
			if (entity.world.rand.nextFloat() <= 0.06 && !entity.isBeingRidden()) {
				EntityLiving entityskeleton = (entity.world.rand.nextFloat() > 0.09) ? new EntitySkeleton(entity.world) : new EntityStray(entity.world);
				entityskeleton.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, 0.0F);
				entityskeleton.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Items.BOW));
				tagEntity(entityskeleton);
				entity.world.spawnEntity(entityskeleton);
				entityskeleton.startRiding(entity, true);
				tagEntity(entity);
				return;
			}
		}
		if (entity instanceof EntityHusk) {
			EntityHusk husk = (EntityHusk) entity;
			husk.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25.0D);
			husk.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
			husk.setHealth(husk.getMaxHealth());
			/*
			 * if (entity.world.rand.nextFloat() <= 0.04) { EntityZombieHorse horse = new
			 * EntityZombieHorse(entity.world); horse.setPosition(entity.posX, entity.posY,
			 * entity.posZ); horse.setHorseTamed(true); entity.world.spawnEntity(horse);
			 * entity.startRiding(horse); }
			 */
			return;
		}
		if (entity instanceof EntityZombie) {
			EntityZombie zombie = (EntityZombie) entity;
			if (!zombie.isChild()) {
				zombie.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.27);
			}
			if (entity.world.rand.nextFloat() <= 0.19 && !(entity.world.getBiome(entity.getPosition()) == Biomes.DESERT || entity.world.getBiome(entity.getPosition()) == Biomes.DESERT_HILLS)) {
				EntityHusk husk = new EntityHusk(entity.world);
				husk.setPosition(entity.posX, entity.posY, entity.posZ);
				entity.world.spawnEntity(husk);
				entity.setDead();
				entity = husk;
			}
			/*
			 * if (entity.world.rand.nextFloat() <= 0.02) { EntityZombieHorse horse = new
			 * EntityZombieHorse(entity.world); horse.setPosition(entity.posX, entity.posY,
			 * entity.posZ); horse.setHorseTamed(true); tagEntity(horse);
			 * entity.world.spawnEntity(horse); entity.startRiding(horse); }
			 */
			return;
		}
		if (entity instanceof EntityGhast) {
			EntityGhast ghast = (EntityGhast) entity;
			ghast.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25.0D);
			ghast.setHealth(ghast.getMaxHealth());
			return;
		}
		if (entity instanceof EntityWitch) {
			EntityWitch witch = (EntityWitch) entity;
			witch.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
			witch.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.27D);
			witch.setHealth(witch.getMaxHealth());
			return;
		}
		if (entity instanceof EntityElderGuardian) {
			EntityElderGuardian elderGuardian = (EntityElderGuardian) entity;
			elderGuardian.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
			elderGuardian.setHealth(elderGuardian.getMaxHealth());
			return;
		}
		if (entity instanceof EntityWither) {
			EntityWither wither = (EntityWither) entity;
			wither.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.65D);
			wither.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(400.0D);
			wither.setHealth(wither.getMaxHealth());
			return;
		}
		if (entity instanceof EntityDragon) {
			EntityDragon dragon = (EntityDragon) entity;
			dragon.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(300.0D);
			dragon.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(7.5);
			dragon.setHealth(dragon.getMaxHealth());
			return;
		}
	}

	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		EntityLivingBase entity = event.getEntityLiving();
		if (!event.isCanceled() && !entity.world.isRemote) {
			if (entity instanceof EntityEnderman) {
				for (int i = 0; i < entity.world.rand.nextInt(3); i++) {
					EntityEndermite endermite = new EntityEndermite(entity.world);
					endermite.setPosition(entity.posX, entity.posY, entity.posZ);
					tagEntity(endermite);
					entity.world.spawnEntity(endermite);
				}
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
		// event.setResult(SleepResult.OTHER_PROBLEM);
		// event.getEntityPlayer().setSpawnPoint(event.getEntityPlayer().getPosition(),
		// true);
	}

	@SubscribeEvent
	public void onInteract(PlayerInteractEvent.EntityInteract event) {
		if (event.getTarget() instanceof EntityAnimal) {
			event.setCancellationResult(EnumActionResult.SUCCESS);
			if (AnimalHelper.interact((EntityAnimal) event.getTarget(), event.getEntityPlayer(), event.getItemStack())) {
				event.setCanceled(true);
			}
		}
	}

	/*
	 * @SubscribeEvent public void onInteract(PlayerInteractEvent.RightClickItem
	 * event) { }
	 * 
	 * @SubscribeEvent public void onItemUseStart(LivingEntityUseItemEvent.Start
	 * event) { }
	 * 
	 * @SubscribeEvent public void onItemUseStart(LivingEntityUseItemEvent.Stop
	 * event) { }
	 * 
	 * @SubscribeEvent public void onItemUseStart(LivingEntityUseItemEvent.Tick
	 * event) { }
	 * 
	 * @SubscribeEvent public void onItemUseStart(LivingEntityUseItemEvent.Finish
	 * event) { }
	 */

	private static boolean containsEntityItem(Iterator<EntityItem> iterator, Item item) {
		while (iterator.hasNext()) {
			if (iterator.next().getItem().getItem() == item)
				return true;
		}
		return false;
	}

	private static void tagEntity(Entity entity) {
		entity.getEntityData().setBoolean("deltahardmode_tagged", true);
	}

	private static boolean hasTag(Entity entity) {
		return entity.getEntityData().hasKey("deltahardmode_tagged");
	}

}
