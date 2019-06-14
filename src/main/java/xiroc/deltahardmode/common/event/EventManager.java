package xiroc.deltahardmode.common.event;

import net.minecraft.block.Block;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.BonemealEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import xiroc.deltahardmode.DeltaHardMode;

@Mod.EventBusSubscriber(modid = DeltaHardMode.MODID)
public class EventManager {

	@SubscribeEvent
	public static void registerBlocks(final RegistryEvent.Register<Block> event) {

	}

	@SubscribeEvent
	public static void onLootTableLoad(final LootTableLoadEvent event) {

	}

	@SubscribeEvent
	public static void onBonemealUse(final BonemealEvent event) {

	}

	@SubscribeEvent
	public static void onPlayerTick(final TickEvent.PlayerTickEvent event) {

	}

	@SubscribeEvent
	public static void onItemRightClick(final PlayerInteractEvent.RightClickItem event) {

	}

	@SubscribeEvent
	public static void onNeighborNotify(final BlockEvent.NeighborNotifyEvent event) {

	}

	@SubscribeEvent
	public static void onBlockPlaced(final BlockEvent.EntityPlaceEvent event) {

	}

	@SubscribeEvent
	public static void onLivingDrop(final LivingDropsEvent event) {

	}

	@SubscribeEvent
	public static void onAttack(final AttackEntityEvent event) {

	}

	@SubscribeEvent
	public static void onDamage(final LivingDamageEvent event) {

	}

	@SubscribeEvent
	public static void onLivingDeath(final LivingDeathEvent event) {

	}

	@SubscribeEvent
	public static void onEntityInteract(final PlayerInteractEvent.EntityInteract event) {

	}

	@SubscribeEvent
	public static void onExplosionStart(final ExplosionEvent.Start event) {

	}

	@SubscribeEvent
	public static void onEnterChunk(final EntityEvent.EnteringChunk event) {

	}

	@SubscribeEvent
	public static void onHarvestDrops(final BlockEvent.HarvestDropsEvent event) {

	}

	@SubscribeEvent
	public static void onArrowImpact(final ProjectileImpactEvent.Arrow event) {

	}

	@SubscribeEvent
	public static void onFireballImpact(final ProjectileImpactEvent.Fireball event) {

	}

	@SubscribeEvent
	public static void onSleep(final PlayerSleepInBedEvent event) {

	}

}
