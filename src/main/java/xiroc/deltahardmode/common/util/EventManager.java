package xiroc.deltahardmode.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.BlazeEntity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.ElderGuardianEntity;
import net.minecraft.entity.monster.EndermanEntity;
import net.minecraft.entity.monster.EndermiteEntity;
import net.minecraft.entity.monster.EvokerEntity;
import net.minecraft.entity.monster.GhastEntity;
import net.minecraft.entity.monster.GuardianEntity;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.monster.IllusionerEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.monster.SpiderEntity;
import net.minecraft.entity.monster.StrayEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.WitherSkeletonEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.monster.ZombiePigmanEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import xiroc.deltahardmode.DeltaHardMode;

public class EventManager {

	private static final ArrayList<Biome> ICE_BIOMES = Lists.newArrayList(Biomes.ICE_SPIKES, Biomes.SNOWY_BEACH,
			Biomes.SNOWY_MOUNTAINS, Biomes.SNOWY_TAIGA, Biomes.SNOWY_TAIGA_HILLS, Biomes.SNOWY_TAIGA_MOUNTAINS,
			Biomes.SNOWY_TUNDRA, Biomes.FROZEN_OCEAN, Biomes.DEEP_FROZEN_OCEAN, Biomes.FROZEN_RIVER);

	@SubscribeEvent
	public void onBonemeal(BonemealEvent event) {
		if (!(event.getBlock().getBlock() instanceof CropsBlock))
			return;
//		if (!ConfigHelper.getProperty("CHANGE_BONEMEAL"))
//			return;
		event.setCanceled(true);
		BlockState state = event.getBlock();
		World worldIn = event.getWorld();
		CropsBlock block = (CropsBlock) state.getBlock();
		if (state.getBlock() instanceof IGrowable) {
			IGrowable growable = (IGrowable) state.getBlock();
			if (growable.canGrow(worldIn, event.getPos(), state, worldIn.isRemote)) {
				if (!worldIn.isRemote) {
					if (growable.canUseBonemeal(worldIn, worldIn.rand, event.getPos(), state)) {
						int i = state.get(BlockStateProperties.AGE_0_7) + 1;
						int j = block.getMaxAge();
						if (i > j)
							i = j;
						worldIn.setBlockState(event.getPos(), block.withAge(i), 2);
						event.getStack().shrink(1);
						event.getPlayer().swingArm(event.getPlayer().getActiveHand());
					}
				} else
					BoneMealItem.spawnBonemealParticles(worldIn, event.getPos(), 5);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if (event.player.world.isRemote)
			return;
		if (event.player.getSleepTimer() >= 80 /* && ConfigHelper.getProperty("NO_SLEEP") */) {
			event.player.wakeUpPlayer(true, false, true);
			event.player.sendStatusMessage(new StringTextComponent("I cant sleep now..."), true);
		}
	}

	@SubscribeEvent
	public void onRightClickItem(PlayerInteractEvent.RightClickItem event) {
		if (event.getItemStack().getItem() == Items.BUCKET /* && ConfigHelper.getProperty("NO_OBSIDIAN") */) {
			RayTraceResult raytraceresult = RayTrace.rayTrace(event.getWorld(), event.getPlayer());
			if (raytraceresult != null && event.getWorld().getBlockState(new BlockPos(raytraceresult.getHitVec()))
					.getMaterial() == Material.LAVA)
				event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public void onNeighborChange(BlockEvent.NeighborNotifyEvent event) {
		if (event.getWorld().isRemote() || !ConfigHelper.getProperty("gravity")
				|| !(event.getWorld() instanceof ServerWorld))
			return;
		ServerWorld server = (ServerWorld) event.getWorld();
		if (!server.getChunkProvider().isChunkLoaded(new ChunkPos(event.getPos())) || server.getGameTime() < 20)
			return;
		DeltaHardMode.LOGGER.debug("NeighborChange: {}", event.getPos());
		event.getWorld().getBlockState(event.getPos()).updateNeighbors(event.getWorld(), event.getPos(), 4);
		FallingBlockHelper.checkFallable(event.getWorld().getWorld(), event.getPos());
		FallingBlockHelper.updateNeighbors(event.getWorld().getWorld(), event.getPos());
	}

//
	@SubscribeEvent
	public void onBlockAdded(BlockEvent.EntityPlaceEvent event) {
		if (event.getWorld().isRemote() || !ConfigHelper.getProperty("gravity") || !(event.getWorld() instanceof ServerWorld))
			return;
		ServerWorld server = (ServerWorld) event.getWorld();
		if (!server.getChunkProvider().isChunkLoaded(new ChunkPos(event.getPos())) || server.getGameTime() < 20)
			return;
		DeltaHardMode.LOGGER.debug("Checking if fallable: {}", event.getPos());
		event.getWorld().getBlockState(event.getPos()).updateNeighbors(event.getWorld(), event.getPos(), 4);
		FallingBlockHelper.checkFallable(event.getWorld().getWorld(), event.getPos());
		FallingBlockHelper.updateNeighbors(event.getWorld().getWorld(), event.getPos());
	}

	@SubscribeEvent
	public void onDrop(LivingDropsEvent event) {
		LivingEntity entity = event.getEntityLiving();
		if (entity instanceof CowEntity) {
			if (!containsEntityItem(event.getDrops().iterator(), Items.LEATHER))
				event.getDrops().add(new ItemEntity(entity.getEntityWorld(), entity.posX, entity.posY, entity.posZ,
						new ItemStack(Items.LEATHER)));
			return;
		}
		if ((entity instanceof SkeletonEntity || entity instanceof StrayEntity)
		/* && ConfigHelper.getProperty("SKELETON_NO_BOW_DROP") */) {
			Iterator<ItemEntity> iterator = event.getDrops().iterator();
			while (iterator.hasNext()) {
				ItemEntity item = iterator.next();
				if (item.getItem().getItem() == Items.BOW)
					iterator.remove();
			}
			return;
		}
		if (entity instanceof SpiderEntity /* && ConfigHelper.getProperty("SPIDER_NO_STRING_DROP") */) {
			Iterator<ItemEntity> iterator = event.getDrops().iterator();
			while (iterator.hasNext()) {
				ItemEntity item = iterator.next();
				if (item.getItem().getItem() == Items.STRING)
					iterator.remove();
			}
			return;
		}
		if (entity instanceof EndermanEntity) {
			Iterator<ItemEntity> iterator = event.getDrops().iterator();
			while (iterator.hasNext()) {
				ItemEntity item = iterator.next();
				if (item.getItem().getItem() == Items.ENDER_PEARL) {
					int count = item.getItem().getCount();
					item.setItem(new ItemStack(item.getItem().getItem(),
							count >= 16 ? count : count + entity.world.rand.nextInt(2)));
				}
			}
			return;
		}
		if (entity instanceof BlazeEntity) {
			Iterator<ItemEntity> iterator = event.getDrops().iterator();
			while (iterator.hasNext()) {
				ItemEntity item = iterator.next();
				if (item.getItem().getItem() == Items.BLAZE_ROD) {
					int count = item.getItem().getCount();
					item.setItem(new ItemStack(item.getItem().getItem(),
							count >= 16 ? count : count + entity.world.rand.nextInt(2)));
				}
			}
			return;
		}
	}

//	@SubscribeEvent
//	public void onAttack(AttackEntityEvent event) {
//		if (event.getTarget() instanceof FireballEntity) {
//			FireballEntity fireball = (FireballEntity) event.getTarget();
//			event.setCanceled(true);
//			fireball.remove();
//			if (!fireball.world.isRemote) {
//				boolean mobGriefing = fireball.getEntityWorld().getGameRules().getBoolean(GameRules.MOB_GRIEFING);
//				Explosion explosion = ExplosionHelper.newExplosion(fireball, fireball.getEntityWorld(), fireball.posX,
//						fireball.posY, fireball.posZ, 2, mobGriefing, mobGriefing);
//				for (Entity e : fireball.world
//						.getEntitiesWithinAABB(
//								EntityType.PLAYER, new AxisAlignedBB(fireball.posX - 32, fireball.posY - 32,
//										fireball.posZ - 32, fireball.posX + 32, fireball.posY + 32, fireball.posZ + 32),
//								(t) -> true)) {
//					ServerPlayerEntity player = (ServerPlayerEntity) e;
//					DeltaHardMode.CHANNEL.send(!?, new PacketClientExplosion(x, y, z, size, terrainDamage, affectedBlockPositions));
//				}
//			}
//			return;
//		}
//	}

//	@SubscribeEvent
//	public void onArrowImpact(ProjectileImpactEvent.Arrow event) {
//		if (event.getEntity().world.isRemote)
//			return;
//		if (event.getRayTraceResult().entityHit instanceof EntityLargeFireball) {
//			EntityLargeFireball fireball = (EntityLargeFireball) event.getRayTraceResult().entityHit;
//			event.setCanceled(true);
//			fireball.setDead();
//			if (!fireball.world.isRemote) {
//				boolean mobGriefing = fireball.getEntityWorld().getGameRules().getBoolean("mobGriefing");
//				Explosion explosion = ExplosionHelper.newExplosion(fireball.shootingEntity, fireball.getEntityWorld(),
//						fireball.posX, fireball.posY, fireball.posZ, 1.5F, mobGriefing, mobGriefing);
//				DeltaHard.NET.sendToAllAround(
//						new PacketClientExplosion(fireball.posX, fireball.posY, fireball.posZ, 2, mobGriefing,
//								explosion.getAffectedBlockPositions()),
//						new TargetPoint(fireball.dimension, fireball.posX, fireball.posY, fireball.posZ, 64.0D));
//			}
//			return;
//		}
//	}
//
//	@SubscribeEvent
//	public void onFireballImpact(ProjectileImpactEvent.Fireball event) {
//		if (event.getEntity().world.isRemote)
//			return;
//		if (event.getEntity() instanceof EntityLargeFireball) {
//			EntityLargeFireball fireball = (EntityLargeFireball) event.getEntity();
//			// event.setCanceled(true);
//			fireball.setDead();
//			if (!fireball.world.isRemote) {
//				boolean mobGriefing = fireball.getEntityWorld().getGameRules().getBoolean("mobGriefing");
//				Explosion explosion = ExplosionHelper.newExplosion(fireball.shootingEntity, fireball.getEntityWorld(),
//						fireball.posX, fireball.posY, fireball.posZ, 1.5F, mobGriefing, mobGriefing);
//				DeltaHard.NET.sendToAllAround(
//						new PacketClientExplosion(fireball.posX, fireball.posY, fireball.posZ, 2, mobGriefing,
//								explosion.getAffectedBlockPositions()),
//						new TargetPoint(fireball.dimension, fireball.posX, fireball.posY, fireball.posZ, 64.0D));
//			}
//			return;
//		}
//		if (event.getEntity() instanceof EntityDragonFireball
//				&& !(event.getRayTraceResult().entityHit instanceof EntityDragon)) {
//			EntityDragonFireball fireball = (EntityDragonFireball) event.getEntity();
//			if (!fireball.world.isRemote && !fireball.isDead) {
//				boolean mobGriefing = fireball.getEntityWorld().getGameRules().getBoolean("mobGriefing");
//				Explosion explosion = ExplosionHelper.newExplosion(fireball.shootingEntity, fireball.getEntityWorld(),
//						fireball.posX, fireball.posY, fireball.posZ, 1.3F, false, mobGriefing);
//				DeltaHard.NET.sendToAllAround(
//						new PacketClientExplosion(fireball.posX, fireball.posY, fireball.posZ, 1.3F, mobGriefing,
//								explosion.getAffectedBlockPositions()),
//						new TargetPoint(fireball.dimension, fireball.posX, fireball.posY, fireball.posZ, 64.0D));
//				fireball.setDead();
//			}
//		}
//	}
//
//	@SubscribeEvent
//	public void onDamage(LivingDamageEvent event) {
//		EntityLivingBase entity = event.getEntityLiving();
//		if ((entity instanceof EntitySpider || entity instanceof EntitySlime)
//				&& event.getSource() == DamageSource.FALL) {
//			event.setCanceled(true);
//			return;
//		}
//		if (entity instanceof EntityPlayer && event.getSource() instanceof EntityDamageSource
//				&& ((EntityDamageSource) event.getSource()).getTrueSource() instanceof EntityBlaze) {
//			entity.setFire(4);
//			return;
//		}
//	}
//
//	@SubscribeEvent
//	public void onExplosionStart(ExplosionEvent.Start event) {
//		EntityLivingBase entity = event.getExplosion().getExplosivePlacedBy();
//		if (entity == null)
//			return;
//		boolean mobGriefing = entity.getEntityWorld().getGameRules().getBoolean("mobGriefing");
//		if (entity instanceof EntityCreeper) {
//			event.setCanceled(true);
//			EntityCreeper creeper = (EntityCreeper) entity;
//			Explosion explosion = ExplosionHelper.newExplosion(creeper, event.getWorld(), creeper.posX, creeper.posY,
//					creeper.posZ, 3 * (creeper.getPowered() ? 2 : 1), mobGriefing, mobGriefing);
//			DeltaHard.NET.sendToAllAround(
//					new PacketClientExplosion(creeper.posX, creeper.posY, creeper.posZ,
//							3 * (creeper.getPowered() ? 2 : 1), mobGriefing, explosion.getAffectedBlockPositions()),
//					new TargetPoint(creeper.dimension, creeper.posX, creeper.posY, creeper.posZ, 64.0D));
//			ExplosionHelper.spawnLingeringCloud(creeper);
//			entity.setDead();
//			return;
//		}
//		if (entity instanceof EntityWither) {
//			event.setCanceled(true);
//			EntityWither wither = (EntityWither) entity;
//			Explosion explosion = ExplosionHelper.newExplosion(entity, event.getWorld(), entity.posX, entity.posY,
//					entity.posZ, 10, false, mobGriefing);
//			DeltaHard.NET.sendToAllAround(
//					new PacketClientExplosion(wither.posX, wither.posY, wither.posZ, 10, mobGriefing,
//							explosion.getAffectedBlockPositions()),
//					new TargetPoint(wither.dimension, wither.posX, wither.posY, wither.posZ, 64.0D));
//			return;
//		}
//	}

	@SubscribeEvent
	public void onExplosionStart(ExplosionEvent.Start event) {
		DeltaHardMode.LOGGER.debug("Explosion Start");
		LivingEntity entity = event.getExplosion().getExplosivePlacedBy();
		if (entity != null) {
			if (entity instanceof CreeperEntity) {

				try {
					Field fire = Explosion.class
							.getDeclaredField(DeltaHardMode.OBFUSCATED_VALUES ? "field_77286_a" : "causesFire");
					fire.setAccessible(true);

					Field finalField = Field.class.getDeclaredField("modifiers");
					finalField.setAccessible(true);

					finalField.setInt(fire, fire.getModifiers() & ~Modifier.FINAL);

					fire.setBoolean(event.getExplosion(), true);
				} catch (Exception e) {
					e.printStackTrace();
				}

				return;
			}
			if (entity instanceof WitherEntity) {
				try {
					Field size = Explosion.class
							.getDeclaredField(DeltaHardMode.OBFUSCATED_VALUES ? "field_77280_f" : "size");
					size.setAccessible(true);

					Field finalField = Field.class.getDeclaredField("modifiers");
					finalField.setAccessible(true);

					finalField.setInt(size, size.getModifiers() & ~Modifier.FINAL);

					size.setFloat(event.getExplosion(), 10F);
				} catch (Exception e) {
					e.printStackTrace();
				}

				return;
			}

		}
	}

	@SubscribeEvent
	public void onEnterChunk(EntityEvent.EnteringChunk event) {
		Entity entity = event.getEntity();
		if (entity.world.isRemote || entity.ticksExisted > 5
				|| !event.getEntity().world.getChunkProvider().isChunkLoaded(entity))
			return;
		if (hasTag(entity))
			return;
		if (entity instanceof AnimalEntity) {
			AnimalHelper.initAnimalTasks((AnimalEntity) entity);
			return;
		}
		tagEntity(entity);
		if (entity instanceof CreeperEntity) {
			CreeperEntity creeper = (CreeperEntity) entity;
			CompoundNBT nbt = new CompoundNBT();
			creeper.writeAdditional(nbt);
			nbt.putShort("Fuse", (short) 25);
			creeper.readAdditional(nbt);
			return;
		}
		if (entity instanceof ZombiePigmanEntity) {
			if (entity.world.rand.nextFloat() <= 0.05) {
				CreeperEntity creeper = new CreeperEntity(EntityType.CREEPER, entity.world);
				creeper.setPosition(entity.posX, entity.posY, entity.posZ);
				tagEntity(creeper);
				entity.world.addEntity(creeper);
				entity.remove();
			}
			return;
		}
//		if (entity instanceof WolfEntity) {
//			WolfEntity wolf = (WolfEntity) entity;
//			wolf.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
//			return;
//		}
		if (entity instanceof SquidEntity) {
			if (entity.world.rand.nextFloat() <= 0.07) {
				GuardianEntity guardian = new GuardianEntity(EntityType.GUARDIAN, entity.world);
				guardian.setPosition(entity.posX, entity.posY, entity.posZ);
				tagEntity(guardian);
				entity.world.addEntity(guardian);
				entity.remove();
			}
		}
		if (entity instanceof StrayEntity) {
			if (entity.dimension == DimensionType.THE_NETHER) {
				WitherSkeletonEntity skeleton = new WitherSkeletonEntity(EntityType.WITHER_SKELETON, entity.world);
				skeleton.setPosition(entity.posX, entity.posY, entity.posZ);
				skeleton.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.BOW));
				entity.world.addEntity(skeleton);
				tagEntity(skeleton);
				entity.remove();
				return;
			}
		}
		if (entity instanceof SkeletonEntity) {
			if (entity.dimension == DimensionType.THE_NETHER) {
				WitherSkeletonEntity skeleton = new WitherSkeletonEntity(EntityType.WITHER_SKELETON, entity.world);
				skeleton.setPosition(entity.posX, entity.posY, entity.posZ);
				skeleton.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.BOW));
				entity.world.addEntity(skeleton);
				tagEntity(skeleton);
				entity.remove();
				return;
			}
			if (entity.dimension == DimensionType.OVERWORLD) {
				if (!ICE_BIOMES.contains(entity.world.getBiome(entity.getPosition()))) {
					if (entity.world.rand.nextFloat() <= 0.07) {
						StrayEntity stray = new StrayEntity(EntityType.STRAY, entity.world);
						stray.setPosition(entity.posX, entity.posY, entity.posZ);
						stray.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.BOW));
						entity.world.addEntity(stray);
						tagEntity(stray);
						entity.remove();
						return;
					}
				}
			}
			return;
		}
		if (entity instanceof EvokerEntity) {
			EvokerEntity evoker = (EvokerEntity) entity;
			evoker.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(50.0D);
			evoker.setHealth(evoker.getMaxHealth());
			return;
		}
		if (entity instanceof VindicatorEntity) {
			if (entity.world.rand.nextFloat() <= 0.09) {
				IllusionerEntity illusionIllager = new IllusionerEntity(EntityType.ILLUSIONER, entity.world);
				illusionIllager.setPosition(entity.posX, entity.posY, entity.posZ);
				tagEntity(illusionIllager);
				entity.world.addEntity(illusionIllager);
				entity.remove();
			}
			return;
		}
		if (entity instanceof SpiderEntity) {
			SpiderEntity spider = (SpiderEntity) entity;
			spider.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
			if (entity.world.rand.nextFloat() <= 0.06 && !entity.isBeingRidden()) {
				LivingEntity entityskeleton = (entity.world.rand.nextFloat() > 0.09)
						? new SkeletonEntity(EntityType.SKELETON, entity.world)
						: new StrayEntity(EntityType.STRAY, entity.world);
				entityskeleton.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, entity.rotationYaw, 0.0F);
				entityskeleton.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.BOW));
				tagEntity(entityskeleton);
				entity.world.addEntity(entityskeleton);
				entityskeleton.startRiding(entity, true);
				tagEntity(entity);
				return;
			}
		}
		if (entity instanceof HuskEntity) {
			HuskEntity husk = (HuskEntity) entity;
			husk.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25.0D);
			husk.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
			husk.setHealth(husk.getMaxHealth());

//			if (entity.world.rand.nextFloat() <= 0.04) { EntityZombieHorse horse = new
//			EntityZombieHorse(entity.world); horse.setPosition(entity.posX, entity.posY,
//			entity.posZ); horse.setHorseTamed(true); entity.world.spawnEntity(horse);
//			entity.startRiding(horse); }
			return;
		}
		if (entity instanceof ZombieEntity) {
			ZombieEntity zombie = (ZombieEntity) entity;
			if (!zombie.isChild()) {
				zombie.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.27);
			}
			if (entity.world.rand.nextFloat() <= 0.19 && !(entity.world.getBiome(entity.getPosition()) == Biomes.DESERT
					|| entity.world.getBiome(entity.getPosition()) == Biomes.DESERT_HILLS)) {
				HuskEntity husk = new HuskEntity(EntityType.HUSK, entity.world);
				husk.setPosition(entity.posX, entity.posY, entity.posZ);
				entity.world.addEntity(husk);
				entity.remove();
				entity = husk;
			}

//			if (entity.world.rand.nextFloat() <= 0.02) { EntityZombieHorse horse = new
//			EntityZombieHorse(entity.world); horse.setPosition(entity.posX, entity.posY,
//			entity.posZ); horse.setHorseTamed(true); tagEntity(horse);
//			entity.world.spawnEntity(horse); entity.startRiding(horse); }
			return;
		}
		if (entity instanceof GhastEntity) {
			GhastEntity ghast = (GhastEntity) entity;
			ghast.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(25.0D);
			ghast.setHealth(ghast.getMaxHealth());
			return;
		}
		if (entity instanceof WitchEntity) {
			WitchEntity witch = (WitchEntity) entity;
			witch.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(30.0D);
			witch.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.27D);
			witch.setHealth(witch.getMaxHealth());
			return;
		}
		if (entity instanceof ElderGuardianEntity) {
			ElderGuardianEntity elderGuardian = (ElderGuardianEntity) entity;
			elderGuardian.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(100.0D);
			elderGuardian.setHealth(elderGuardian.getMaxHealth());
			return;
		}
		if (entity instanceof WitherEntity) {
			WitherEntity wither = (WitherEntity) entity;
			wither.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.65D);
			wither.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(350.0D);
			wither.setHealth(wither.getMaxHealth());
			return;
		}
		if (entity instanceof EnderDragonEntity) {
			EnderDragonEntity dragon = (EnderDragonEntity) entity;
			dragon.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(250.0D);
			dragon.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(7.5);
			dragon.setHealth(dragon.getMaxHealth());
			return;
		}
	}

	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		LivingEntity entity = event.getEntityLiving();
		if (!event.isCanceled() && !entity.world.isRemote) {
			if (entity instanceof EndermanEntity) {
				for (int i = 0; i < entity.world.rand.nextInt(3); i++) {
					EndermiteEntity endermite = new EndermiteEntity(EntityType.ENDERMITE, entity.world);
					endermite.setPosition(entity.posX, entity.posY, entity.posZ);
					tagEntity(endermite);
					entity.world.addEntity(endermite);
				}
			}
		}
	}

//	@SubscribeEvent
//	public void onSleep(PlayerSleepInBedEvent event) {
//		if (event.getPlayer().world.isRemote)
//			return;
//		if (event.getPlayer().getEntityWorld().dimension.getType() != DimensionType.OVERWORLD)
//			return;
//		if (!ConfigHelper.getProperty("NO_SLEEP"))
//			return;
//		// event.setResult(SleepResult.OTHER_PROBLEM);
//		// event.getEntityPlayer().setSpawnPoint(event.getEntityPlayer().getPosition(),
//		// true);
//	}

	@SubscribeEvent
	public void onInteract(PlayerInteractEvent.EntityInteract event) {
		if (event.getTarget() instanceof AnimalEntity) {
			event.setCancellationResult(ActionResultType.SUCCESS);
			if (AnimalHelper.interact((AnimalEntity) event.getTarget(), event.getPlayer(), event.getItemStack())) {
				event.setCanceled(true);
			}
		}
	}

	private static boolean containsEntityItem(Iterator<ItemEntity> iterator, Item item) {
		while (iterator.hasNext()) {
			if (iterator.next().getItem().getItem() == item)
				return true;
		}
		return false;
	}

	private static void tagEntity(Entity entity) {
		entity.getPersistentData().putBoolean("deltahardmode_tagged", true);
	}

	private static boolean hasTag(Entity entity) {
		return entity.getPersistentData().contains("deltahardmode_tagged");
	}

}
