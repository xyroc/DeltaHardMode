package xiroc.deltahardmode;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import xiroc.deltahardmode.common.util.ConfigCache;
import xiroc.deltahardmode.common.util.EventManager;

/*
 * Delta Hard Mode - 1.0.0
 * May 07 2020
 */

@Mod(DeltaHardMode.MODID)
public class DeltaHardMode {

	public static final String MODID = "deltahardmode";
	public static final Logger LOGGER = LogManager.getLogger();

	public static final boolean OBFUSCATED_VALUES = false;

	public DeltaHardMode() {
		LOGGER.info("Challenge awaits...");
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
		MinecraftForge.EVENT_BUS.register(new EventManager());
	}

	private void commonSetup(FMLCommonSetupEvent event) {

		try {
			// Blocks
			Field harvestLevel = Block.class.getDeclaredField(OBFUSCATED_VALUES ? "field_149782_v " : "harvestLevel");
			harvestLevel.setAccessible(true);

			Field finalField = Field.class.getDeclaredField("modifiers");
			finalField.setAccessible(true);

			finalField.setInt(harvestLevel, harvestLevel.getModifiers() & ~Modifier.FINAL);

			harvestLevel.setInt(Blocks.OBSIDIAN, 6);
		} catch (Exception e) {
			LOGGER.error("Failed to change the obsidian harvest level.");
			e.printStackTrace();
		}
	}

	@EventBusSubscriber(modid = MODID, bus = Bus.MOD)
	public static class ModEvents {

		@SubscribeEvent
		public static void onBlockRegistry(final RegistryEvent.Register<Block> event) {
			LOGGER.info("Loading gravity blocks");
			ConfigCache.gravityBlocks.add(Blocks.GRASS_BLOCK);
			ConfigCache.gravityBlocks.add(Blocks.COARSE_DIRT);
			ConfigCache.gravityBlocks.add(Blocks.DIRT);
			ConfigCache.gravityBlocks.add(Blocks.COBBLESTONE);

			ConfigCache.gravityBlocks.add(Blocks.ACACIA_STAIRS);
			ConfigCache.gravityBlocks.add(Blocks.BIRCH_STAIRS);
			ConfigCache.gravityBlocks.add(Blocks.DARK_OAK_STAIRS);
			ConfigCache.gravityBlocks.add(Blocks.JUNGLE_STAIRS);
			ConfigCache.gravityBlocks.add(Blocks.OAK_STAIRS);
			ConfigCache.gravityBlocks.add(Blocks.SPRUCE_STAIRS);

			ConfigCache.gravityBlocks.add(Blocks.ACACIA_SLAB);
			ConfigCache.gravityBlocks.add(Blocks.BIRCH_SLAB);
			ConfigCache.gravityBlocks.add(Blocks.DARK_OAK_SLAB);
			ConfigCache.gravityBlocks.add(Blocks.JUNGLE_SLAB);
			ConfigCache.gravityBlocks.add(Blocks.OAK_SLAB);
			ConfigCache.gravityBlocks.add(Blocks.SPRUCE_SLAB);

			ConfigCache.gravityBlocks.add(Blocks.STONE_STAIRS);
			ConfigCache.gravityBlocks.add(Blocks.COBBLESTONE_SLAB);

			ConfigCache.gravityBlocks.add(Blocks.ACACIA_LOG);
			ConfigCache.gravityBlocks.add(Blocks.BIRCH_LOG);
			ConfigCache.gravityBlocks.add(Blocks.DARK_OAK_LOG);
			ConfigCache.gravityBlocks.add(Blocks.JUNGLE_LOG);
			ConfigCache.gravityBlocks.add(Blocks.OAK_LOG);
			ConfigCache.gravityBlocks.add(Blocks.SPRUCE_LOG);

			ConfigCache.gravityBlocks.add(Blocks.ACACIA_PLANKS);
			ConfigCache.gravityBlocks.add(Blocks.BIRCH_PLANKS);
			ConfigCache.gravityBlocks.add(Blocks.DARK_OAK_PLANKS);
			ConfigCache.gravityBlocks.add(Blocks.JUNGLE_PLANKS);
			ConfigCache.gravityBlocks.add(Blocks.OAK_PLANKS);
			ConfigCache.gravityBlocks.add(Blocks.SPRUCE_PLANKS);
		}

	}

	public static ResourceLocation locate(String path) {
		return new ResourceLocation(MODID, path);
	}

}
