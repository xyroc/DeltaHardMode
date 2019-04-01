package xiroc.deltahard.util;

import java.io.File;
import java.util.HashMap;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;
import xiroc.deltahard.DeltaHard;

public class ConfigHelper {

	private static Configuration config;

	private static HashMap<String, Boolean> toggles;

	private static boolean LOADED = false;

	public static boolean getProperty(String name) {
		if (!LOADED)
			loadConfig();
		return toggles.get(name);
	}

	public static void loadConfig() {
		DeltaHard.logger.info("Loading Config:");
		toggles = new HashMap();
		config = new Configuration(new File(Loader.instance().getConfigDir(), "DeltaHardMode.cfg"));
		toggles.put("GRAVITY", config.getBoolean("GRAVITY", "general", true, "Determines, if dirt, cobblestone, wood planks, wooden stairs and wooden slabs should be affected by gravity (true = affected by gravity"));
		toggles.put("NO_OBSIDIAN", config.getBoolean("RESTRICT_OBSIDIAN", "general", true, "Determines if the changes to obisidian should be applied (true = apply changes)"));
		toggles.put("LOOT_OBSIDIAN", config.getBoolean("LOOT_OBSIDIAN", "general", true, "Determines if obsidian should be found in dungeon chests. (true = generate loot)"));
		toggles.put("CHANGE_BONEMEAL", config.getBoolean("CHANGE_BONEMEAL", "general", true, "Determines if the changes to bonemeal should be applied. (true = apply changes)"));
		toggles.put("NO_SLEEP", config.getBoolean("NO_SLEEP", "general", true, "Determines if the player shouldnt be able to sleep. (true = no sleeping)"));
		toggles.put("SKELETON_NO_BOW_DROP", config.getBoolean("SKELETON_NO_BOW_DROP", "mobs", true, "Determines if skeletons drop bows or not. (true = no bow dropped)"));
		toggles.put("SPIDER_NO_STRING_DROP", config.getBoolean("SPIDER_NO_STRING_DROP", "mobs", true, "Determines if spiders drop strings or not. Affects both spiders and cave spiders. (true = no strings dropped)"));
		if (config.hasChanged())
			config.save();
		LOADED = true;
		DeltaHard.logger.info("GRAVITY = " + toggles.get("GRAVITY"));
		DeltaHard.logger.info("NO_OBSIDIAN = " + toggles.get("NO_OBSIDIAN"));
		DeltaHard.logger.info("LOOT_OBSIDIAN = " + toggles.get("LOOT_OBSIDIAN"));
		DeltaHard.logger.info("CHANGE_BONEMEAL = " + toggles.get("CHANGE_BONEMEAL"));
		DeltaHard.logger.info("NO_SLEEP = " + toggles.get("NO_SLEEP"));
		DeltaHard.logger.info("SKELETON_NO_BOW_DROP = " + toggles.get("SKELETON_NO_BOW_DROP"));
		DeltaHard.logger.info("SPIDER_NO_STRING_DROP = " + toggles.get("SPIDER_NO_STRING_DROP"));
	}

}
