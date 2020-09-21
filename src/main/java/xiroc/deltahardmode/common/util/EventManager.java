package xiroc.deltahardmode.common.util;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.IGrowable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.*;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.SquidEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.*;
import net.minecraft.world.Explosion.Mode;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import xiroc.deltahardmode.DeltaHardMode;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class EventManager {

//    @SubscribeEvent
//    public void onLootTableLoad(LootTableLoadEvent event) {
//        DeltaHardMode.LOGGER.debug(event.getName().toString());
//        switch (event.getName().toString()) {
//            case "minecraft:chests/village/village_toolsmith":
//            case "minecraft:chests/village/village_weaponsmith":
//                event.getTable().addPool(new LootPool.Builder().name("deltahardmode:obsidian")
//                        .addEntry(ItemLootEntry.builder(Blocks.OBSIDIAN)
//                                .acceptFunction(SetCount.builder(new RandomValueRange(2, 4))))
//                        .acceptCondition(RandomChance.builder(0.412f)).build());
//                break;
//            case "minecraft:chests/village/village_armorer":
//                event.getTable().addPool(new LootPool.Builder().name("deltahardmode:obsidian")
//                        .addEntry(ItemLootEntry.builder(Blocks.OBSIDIAN)
//                                .acceptFunction(SetCount.builder(new RandomValueRange(2, 4))))
//                        .acceptCondition(RandomChance.builder(0.542f)).build());
//                break;
//            case "minecraft:chests/village/village_temple":
//                event.getTable().addPool(new LootPool.Builder().name("deltahardmode:obsidian")
//                        .addEntry(ItemLootEntry.builder(Blocks.OBSIDIAN)
//                                .acceptFunction(SetCount.builder(new RandomValueRange(1, 3))))
//                        .acceptCondition(RandomChance.builder(0.893f)).build());
//                break;
//            case "minecraft:chests/abandoned_mineshaft":
//                event.getTable().addPool(LootPool.builder().name("deltahardmode:chest_minecart")
//                        .addEntry(ItemLootEntry.builder(Items.WHEAT_SEEDS)
//                                .acceptFunction(SetCount.builder(new RandomValueRange(1, 12))).weight(3))
//                        .addEntry(ItemLootEntry.builder(Blocks.OAK_SAPLING)
//                                .acceptFunction(SetCount.builder(new RandomValueRange(1, 8))).weight(2)).build());
//                break;
//        }
//    }

    @SubscribeEvent
    public void onBonemeal(BonemealEvent event) {
        if (!(event.getBlock().getBlock() instanceof CropsBlock) || Config.isDisabled("bonemeal"))
            return;
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
                } else {
                    BoneMealItem.spawnBonemealParticles(worldIn, event.getPos(), 5);
                }
            }
            event.getPlayer().swingArm(Hand.MAIN_HAND);
        }
    }

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent event) {
        if (event.player.world.isRemote)
            return;
        if (event.player.getSleepTimer() >= 80 && !Config.isDisabled("no_sleep")) {
            event.player.wakeUp();
            event.player.sendStatusMessage(new StringTextComponent("I cant sleep now..."), true);
        }
    }

    @SubscribeEvent
    public void onFillBucket(FillBucketEvent event) {
        if (event.getTarget() != null && event.getWorld().getBlockState(new BlockPos(event.getTarget().getHitVec())).getMaterial() == Material.LAVA) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onNeighborChange(BlockEvent.NeighborNotifyEvent event) {
        if (event.getWorld().isRemote() || Config.isDisabled("gravity")
                || !(event.getWorld() instanceof ServerWorld))
            return;
        ServerWorld server = (ServerWorld) event.getWorld();
        if (!server.getChunkProvider().isChunkLoaded(new ChunkPos(event.getPos())) || server.getGameTime() < 20)
            return;
        event.getWorld().getBlockState(event.getPos()).updateNeighbours(event.getWorld(), event.getPos(), 4);
        FallingBlockHelper.checkFallable(server, event.getPos());
        FallingBlockHelper.updateNeighbors(server, event.getPos());
    }

    @SubscribeEvent
    public void onBlockAdded(BlockEvent.EntityPlaceEvent event) {
        if (event.getWorld().isRemote() || Config.isDisabled("gravity")
                || !(event.getWorld() instanceof ServerWorld))
            return;
        ServerWorld server = (ServerWorld) event.getWorld();
        if (!server.getChunkProvider().isChunkLoaded(new ChunkPos(event.getPos())) || server.getGameTime() < 20)
            return;
        event.getWorld().getBlockState(event.getPos()).updateNeighbours(event.getWorld(), event.getPos(), 4);
        FallingBlockHelper.checkFallable(server, event.getPos());
        FallingBlockHelper.updateNeighbors(server, event.getPos());
    }

    @SubscribeEvent
    public void onDrop(LivingDropsEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity instanceof SkeletonEntity || entity instanceof StrayEntity) {
            event.getDrops().removeIf(item -> item.getItem().getItem() == Items.BOW);
            return;
        }
        if (entity instanceof EndermanEntity) {
            for (ItemEntity item : event.getDrops()) {
                if (item.getItem().getItem() == Items.ENDER_PEARL) {
                    int count = item.getItem().getCount();
                    item.setItem(new ItemStack(item.getItem().getItem(),
                            count >= 16 ? count : count + entity.world.rand.nextInt(2)));
                }
            }
        }
    }

    @SubscribeEvent
    public void onAttack(AttackEntityEvent event) {
        if (event.getTarget() instanceof FireballEntity) {
            FireballEntity fireball = (FireballEntity) event.getTarget();
            event.setCanceled(true);
            if (!fireball.world.isRemote) {
                boolean mobGriefing = fireball.getEntityWorld().getGameRules().getBoolean(GameRules.MOB_GRIEFING);

                fireball.world.createExplosion(null, fireball.getPosX(), fireball.getPosY(), fireball.getPosZ(),
                        fireball.explosionPower, true, mobGriefing ? Mode.DESTROY : Mode.NONE);

                DeltaHardMode.LOGGER.debug("Fireball {} {} {}", fireball.getPosX(), fireball.getPosY(),
                        fireball.getPosZ());

            }
            fireball.remove();
        }
    }

    @SubscribeEvent
    public void onArrowImpact(ProjectileImpactEvent.Arrow event) {
        if (event.getEntity().world.isRemote)
            return;

        if (event.getRayTraceResult() instanceof EntityRayTraceResult) {
            EntityRayTraceResult result = (EntityRayTraceResult) event.getRayTraceResult();

            if (result.getEntity() instanceof FireballEntity) {
                DeltaHardMode.LOGGER.debug("Arrow Impact - Explosion");

                FireballEntity fireball = (FireballEntity) result.getEntity();

                boolean mobGriefing = fireball.getEntityWorld().getGameRules().getBoolean(GameRules.MOB_GRIEFING);

                fireball.world.createExplosion(null, fireball.getPosX(), fireball.getPosY(), fireball.getPosZ(),
                        fireball.explosionPower, true, mobGriefing ? Mode.DESTROY : Mode.NONE);

                fireball.remove();
            }
        }

    }

//    @SubscribeEvent
//    public void onFireballImpact(ProjectileImpactEvent.Fireball event) {
//        if (event.getEntity().world.isRemote)
//            return;
////
//        if (event.getFireball() instanceof DragonFireballEntity) {
//            DragonFireballEntity fireball = (DragonFireballEntity) event.getFireball();
//
//            DeltaHardMode.LOGGER.debug("Dragon Fireball Explosion {}", event.getFireball());
//
//            boolean mobGriefing = fireball.getEntityWorld().getGameRules().getBoolean(GameRules.MOB_GRIEFING);
//
//            fireball.world.createExplosion(fireball, fireball.getPosX(), fireball.getPosY(), fireball.getPosZ(), 1.3F,
//                    mobGriefing ? Mode.DESTROY : Mode.NONE);
//
//            fireball.remove();
//        }
//    }

    @SubscribeEvent
    public void onDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if ((entity instanceof SpiderEntity)
                && event.getSource() == DamageSource.FALL) {
            event.setCanceled(true);
            return;
        }
        if (entity instanceof PlayerEntity && event.getSource() instanceof EntityDamageSource
                && ((EntityDamageSource) event.getSource()).getTrueSource() instanceof BlazeEntity) {
            entity.setFire(4);
        }
    }

    @SubscribeEvent
    public void onExplosionStart(ExplosionEvent.Start event) {
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
            if (entity instanceof GhastEntity) {
                try {
                    Field size = Explosion.class
                            .getDeclaredField(DeltaHardMode.OBFUSCATED_VALUES ? "field_77280_f" : "size");
                    size.setAccessible(true);

                    Field finalField = Field.class.getDeclaredField("modifiers");
                    finalField.setAccessible(true);

                    finalField.setInt(size, size.getModifiers() & ~Modifier.FINAL);

                    size.setFloat(event.getExplosion(), 1.4F);
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
            }

        }
    }

//    @SubscribeEvent
//    public void onConstruct(EntityEvent event) {
//        Entity entity = event.getEntity();
//        if (entity instanceof AnimalEntity) {
//            DeltaHardMode.LOGGER.info("init animal tasks on {}", entity);
//            AnimalHelper.initAnimalTasks((AnimalEntity) entity);
//        }
//    }

    @SubscribeEvent
    public void onSpecialSpawn(LivingSpawnEvent.SpecialSpawn event) {
        LivingEntity entity = event.getEntityLiving();
        if (entity instanceof CreeperEntity) {
            CreeperEntity creeper = (CreeperEntity) entity;
            CompoundNBT nbt = new CompoundNBT();
            creeper.writeAdditional(nbt);
            nbt.putShort("Fuse", (short) 25);
            creeper.readAdditional(nbt);
            return;
        }
        if (entity instanceof ZombifiedPiglinEntity || entity instanceof PiglinEntity) {
            if (entity.world.rand.nextFloat() <= 0.05) {
                CreeperEntity creeper = new CreeperEntity(EntityType.CREEPER, entity.world);
                creeper.setPosition(entity.getPosX(), entity.getPosY(), entity.getPosZ());
                entity.world.addEntity(creeper);
                event.setCanceled(true);
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
                guardian.setPosition(entity.getPosX(), entity.getPosY(), entity.getPosZ());
                entity.world.addEntity(guardian);
                event.setCanceled(true);
            }
            return;
        }
        if (entity instanceof StrayEntity) {
            if (entity.world.getDimensionType() == DynamicRegistries.func_239770_b_().getRegistry(Registry.DIMENSION_TYPE_KEY).getValueForKey(DimensionType.THE_NETHER)) {
                WitherSkeletonEntity skeleton = new WitherSkeletonEntity(EntityType.WITHER_SKELETON, entity.world);
                skeleton.setPosition(entity.getPosX(), entity.getPosY(), entity.getPosZ());
                skeleton.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.BOW));
                entity.world.addEntity(skeleton);
                event.setCanceled(true);
                return;
            }
        }
        if (entity instanceof SkeletonEntity) {
            if (entity.world.getDimensionType() == DynamicRegistries.func_239770_b_().getRegistry(Registry.DIMENSION_TYPE_KEY).getValueForKey(DimensionType.THE_NETHER)) {
                WitherSkeletonEntity skeleton = new WitherSkeletonEntity(EntityType.WITHER_SKELETON, entity.world);
                skeleton.setPosition(entity.getPosX(), entity.getPosY(), entity.getPosZ());
                skeleton.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.BOW));
                entity.world.addEntity(skeleton);
                event.setCanceled(true);
                return;
            }
            if (!(entity.world.getBiome(new BlockPos(entity.getPosX(), entity.getPosY(), entity.getPosZ())).getCategory() == Biome.Category.ICY)) {
                if (entity.world.rand.nextFloat() <= 0.07) {
                    StrayEntity stray = new StrayEntity(EntityType.STRAY, entity.world);
                    stray.setPosition(entity.getPosX(), entity.getPosY(), entity.getPosZ());
                    stray.setHeldItem(Hand.MAIN_HAND, new ItemStack(Items.BOW));
                    entity.world.addEntity(stray);
                    event.setCanceled(true);
                    return;
                }
            }
            return;
        }
        if (entity instanceof EvokerEntity) {
            EvokerEntity evoker = (EvokerEntity) entity;
            evoker.getAttribute(Attributes.MAX_HEALTH).setBaseValue(50.0D);
            evoker.setHealth(evoker.getMaxHealth());
            return;
        }
        if (entity instanceof VindicatorEntity) {
            if (entity.world.rand.nextFloat() <= 0.09) {
                IllusionerEntity illusionIllager = new IllusionerEntity(EntityType.ILLUSIONER, entity.world);
                illusionIllager.setPosition(entity.getPosX(), entity.getPosY(), entity.getPosZ());
                entity.world.addEntity(illusionIllager);
                event.setCanceled(true);
            }
            return;
        }
        if (entity.getType() == EntityType.SPIDER && entity instanceof SpiderEntity) {
            SpiderEntity spider = (SpiderEntity) entity;
            spider.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.3D);
            if (entity.world.rand.nextFloat() < 0.03 && !entity.isBeingRidden()) {
                DeltaHardMode.LOGGER.info("Spider Jockey!");
                AbstractSkeletonEntity skeleton = (entity.world.rand.nextFloat() > 0.07)
                        ? EntityType.STRAY.create(entity.world)
                        : EntityType.SKELETON.create(entity.world);
                if (skeleton != null && entity.world instanceof IServerWorld) {
                    skeleton.setLocationAndAngles(entity.getPosX(), entity.getPosY(), entity.getPosZ(), entity.rotationYaw, 0.0F);
                    skeleton.onInitialSpawn((IServerWorld) entity.world, entity.world.getDifficultyForLocation(new BlockPos(entity.getPositionVec())),
                            SpawnReason.JOCKEY, null, null);
                    skeleton.startRiding(entity);
                }
                return;
            }
        }
        if (entity instanceof HuskEntity) {
            HuskEntity husk = (HuskEntity) entity;
            //DeltaHardMode.LOGGER.debug("Husk base attack damage: {}", husk.getAttribute(Attributes.field_233823_f_).getValue());
            husk.getAttribute(Attributes.MAX_HEALTH).setBaseValue(25.0D);
            //husk.getAttribute(Attributes.field_233823_f_).setBaseValue(3.0D);
            husk.setHealth(husk.getMaxHealth());
            return;
        }
        if (entity instanceof ZombieEntity) {
            ZombieEntity zombie = (ZombieEntity) entity;
            if (!zombie.isChild()) {
                zombie.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.27);
            }
            if (entity.world.rand.nextFloat() <= 0.19 && !(entity.world.getBiome(new BlockPos(entity.getPosX(), entity.getPosY(), entity.getPosZ())).getCategory() == Biome.Category.DESERT)) {
                HuskEntity husk = new HuskEntity(EntityType.HUSK, entity.world);
                husk.setPosition(entity.getPosX(), entity.getPosY(), entity.getPosZ());
                entity.world.addEntity(husk);
                event.setCanceled(true);
            }
            return;
        }
        if (entity instanceof GhastEntity) {
            GhastEntity ghast = (GhastEntity) entity;
            ghast.getAttribute(Attributes.MAX_HEALTH).setBaseValue(25.0D);
            ghast.setHealth(ghast.getMaxHealth());
            return;
        }
        if (entity instanceof WitchEntity) {
            WitchEntity witch = (WitchEntity) entity;
            witch.getAttribute(Attributes.MAX_HEALTH).setBaseValue(30.0D);
            witch.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.27D);
            witch.setHealth(witch.getMaxHealth());
            return;
        }
        if (entity instanceof ElderGuardianEntity) {
            ElderGuardianEntity elderGuardian = (ElderGuardianEntity) entity;
            elderGuardian.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100.0D);
            elderGuardian.setHealth(elderGuardian.getMaxHealth());
            return;
        }
        if (entity instanceof WitherEntity) {
            WitherEntity wither = (WitherEntity) entity;
            wither.getAttribute(Attributes.MAX_HEALTH).setBaseValue(350.0D);
            wither.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.65D);
            wither.setHealth(wither.getMaxHealth());
            return;
        }
        if (entity instanceof EnderDragonEntity) {
            EnderDragonEntity dragon = (EnderDragonEntity) entity;
            dragon.getAttribute(Attributes.MAX_HEALTH).setBaseValue(250.0D);
            dragon.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(7.2);
            dragon.setHealth(dragon.getMaxHealth());
        }
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (!event.isCanceled() && !entity.world.isRemote) {
            if (entity instanceof EndermanEntity) {
                for (int i = 0; i < entity.world.rand.nextInt(3); i++) {
                    EndermiteEntity endermite = new EndermiteEntity(EntityType.ENDERMITE, entity.world);
                    endermite.setPosition(entity.getPosX(), entity.getPosY(), entity.getPosZ());
                    entity.world.addEntity(endermite);
                }
            }
        }
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getTarget() instanceof AnimalEntity && AnimalHelper.interact((AnimalEntity) event.getTarget(), event.getPlayer(), event.getItemStack())) {
            event.setCanceled(true);
        }

    }

}
