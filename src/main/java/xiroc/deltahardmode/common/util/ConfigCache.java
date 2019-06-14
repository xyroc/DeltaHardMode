package xiroc.deltahardmode.common.util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraftforge.fml.loading.FMLLoader;

public class ConfigCache {

	public static ArrayList<Block> gravityBlocks = new ArrayList<Block>();
	public static ArrayList<BlockState> gravityStates = new ArrayList<BlockState>();
	public static ArrayList<String> dropBlocks = new ArrayList<String>();
	public static ArrayList<BlockState> dropStates = new ArrayList<BlockState>();
	public static ArrayList<String> removedDrops = new ArrayList<String>();
	public static ArrayList<Item> removedItems = new ArrayList<Item>();

	public static void load() {
		try {
			GsonBuilder builder = new GsonBuilder();
			builder.setPrettyPrinting();
			Gson gson = builder.create();
			File dropBlockFile = new File(FMLLoader.getGamePath().toString(), "//DeltaHardMode//dropBlocks.json");
			File drops = new File(FMLLoader.getGamePath().toString(), "//DeltaHardMode//removedDrops.json");
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
			e.printStackTrace();
		}
	}

}
