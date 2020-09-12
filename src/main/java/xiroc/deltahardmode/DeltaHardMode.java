package xiroc.deltahardmode;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xiroc.deltahardmode.common.util.Config;
import xiroc.deltahardmode.common.util.DataReloadListener;
import xiroc.deltahardmode.common.util.EventManager;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/*
 * Delta Hard Mode - 1.0.2
 * Sep 12 2020
 */

@Mod(DeltaHardMode.MODID)
public class DeltaHardMode {

    public static final String MODID = "deltahardmode";
    public static final Logger LOGGER = LogManager.getLogger();

    public static final boolean OBFUSCATED_VALUES = false;

    public DeltaHardMode() {
        LOGGER.info("Challenge awaits...");
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new EventManager());
    }

    private void commonSetup(FMLCommonSetupEvent event) {
		Config.load(FMLPaths.CONFIGDIR.get().resolve("deltahardmode.toml"));
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

    @SubscribeEvent
    public void onAddDataReloadListener(AddReloadListenerEvent event) {
        event.addListener(new DataReloadListener());
    }

    public static ResourceLocation locate(String path) {
        return new ResourceLocation(MODID, path);
    }

}
