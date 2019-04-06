package xiroc.deltahard.common.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.fml.common.Loader;
import xiroc.deltahard.DeltaHard;

public class ConfigCache {

	public static ArrayList<Block> gravityBlocks = new ArrayList();
	public static ArrayList<IBlockState> gravityStates = new ArrayList();
	public static ArrayList<String> dropBlocks = new ArrayList();
	public static ArrayList<String> removedDrops = new ArrayList();

	public static void load() {
		DeltaHard.logger.info("Loading Config Cache");
		try {
			GsonBuilder builder = new GsonBuilder();
			builder.setPrettyPrinting();
			Gson gson = builder.create();
			File dropBlockFile = new File(Loader.instance().getConfigDir(), "//DeltaHardMode//dropBlocks.json");
			File drops = new File(Loader.instance().getConfigDir(), "//DeltaHardMode//removedDrops.json");
			if (dropBlockFile.exists()) {
				for (String s : gson.fromJson(new FileReader(dropBlockFile), String[].class))
					dropBlocks.add(s);
			} else {
				dropBlockFile.createNewFile();
				FileWriter writer = new FileWriter(dropBlockFile);
				gson.toJson(new String[] { "minecraft:leaves", "minecraft:leaves2", "minecraft:tallgrass" }, writer);
				writer.close();
				dropBlocks.add("minecraft:leaves");
				dropBlocks.add("minecraft:leaves2");
				dropBlocks.add("minecraft:tallgrass");
			}
			if (drops.exists()) {
				for (String s : gson.fromJson(new FileReader(drops), String[].class))
					removedDrops.add(s);
			} else {
				drops.createNewFile();
				FileWriter writer = new FileWriter(drops);
				gson.toJson(new String[] { "minecraft:sapling", "minecraft:wheat_seeds" }, writer);
				writer.close();
				removedDrops.add("minecraft:sapling");
				removedDrops.add("minecraft:wheat_seeds");
			}
		} catch (Exception e) {
			DeltaHard.logger.error("An error occured while loading the config cache");
			e.printStackTrace();
		}
	}

}
