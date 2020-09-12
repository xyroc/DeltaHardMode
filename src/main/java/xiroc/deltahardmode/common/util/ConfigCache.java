package xiroc.deltahardmode.common.util;

import com.google.gson.JsonParser;
import net.minecraft.block.Block;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import xiroc.deltahardmode.DeltaHardMode;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ConfigCache {

    public static ArrayList<Block> gravityBlocks = new ArrayList<>();

    public static void load(IResourceManager resourceManager) {
        try {
            loadGravityBlocks(resourceManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void loadGravityBlocks(IResourceManager resourceManager) throws IOException {
        DeltaHardMode.LOGGER.info("Loading gravity blocks");
        ResourceLocation resource = DeltaHardMode.locate("gravity.json");
        if (resourceManager.hasResource(resource)) {
            JsonParser parser = new JsonParser();
            parser.parse(new InputStreamReader(resourceManager.getResource(resource).getInputStream())).getAsJsonArray().forEach((jsonElement -> {
                ResourceLocation block = new ResourceLocation(jsonElement.getAsString());
                if (ForgeRegistries.BLOCKS.containsKey(block)) {
                    ConfigCache.gravityBlocks.add(ForgeRegistries.BLOCKS.getValue(block));
                } else {
                    DeltaHardMode.LOGGER.error("Unknown gravity block: {}", block);
                }
            }));
        } else {
            throw new FileNotFoundException("Missing file " + resource.toString());
        }
    }

}
