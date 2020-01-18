package xiroc.deltahardmode.util;

import java.util.HashMap;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

public class ConfigHelper {

	private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec CONFIG;

	private static final HashMap<String, BooleanValue> TOGGLES;

	private static final String CONFIG_MAIN = "configuration";

	static {
		TOGGLES = new HashMap<String, BooleanValue>();

		BUILDER.comment("Settings").push(CONFIG_MAIN);
		TOGGLES.put("gravity", BUILDER.comment(
				"Determines, if dirt, cobblestone, wood planks, wooden stairs and wooden slabs should be affected by gravity (true = affected by gravity)")
				.define("gravity", true));
		TOGGLES.put("obsidian", BUILDER.comment("If set to true, obsidian will not be mineable with a diamond pickaxe.").define("hard_obsidian", true));
		TOGGLES.put("bonemeal", BUILDER.comment("Determines if the bone meal changes should be applied.").define("change_bonemeal", true));
		TOGGLES.put("no_sleeping", BUILDER.comment("Determines if the player should not be able to sleep.").define("no_sleeping", true));


		BUILDER.pop();

		CONFIG = BUILDER.build();
	}

	public static boolean getProperty(String name) {
		return TOGGLES.get(name).get();
	}

	public static void loadConfig() {

//		toggles.put("GRAVITY", config.getBoolean("GRAVITY", "general", true,
//				"Determines, if dirt, cobblestone, wood planks, wooden stairs and wooden slabs should be affected by gravity (true = affected by gravity)"));
//		toggles.put("NO_OBSIDIAN", config.getBoolean("RESTRICT_OBSIDIAN", "general", true,
//				"Determines if the changes to obisidian should be applied (true = apply changes)"));
//		toggles.put("LOOT", config.getBoolean("LOOT", "general", true,
//				"If set to true, obsidian, wheat seeds and tree saplings can be found in  loot chests. Also obsidian cant be found anymore in blacksmith chests."));
//		toggles.put("CHANGE_BONEMEAL", config.getBoolean("CHANGE_BONEMEAL", "general", true,
//				"Determines if the changes to bonemeal should be applied. (true = apply changes)"));
//		toggles.put("NO_SLEEP", config.getBoolean("NO_SLEEP", "general", true,
//				"Determines if the player shouldnt be able to sleep. (true = no sleeping)"));
//		toggles.put("SKELETON_NO_BOW_DROP", config.getBoolean("SKELETON_NO_BOW_DROP", "mobs", true,
//				"Determines if skeletons drop bows or not. (true = no bow dropped)"));
//		toggles.put("SPIDER_NO_STRING_DROP", config.getBoolean("SPIDER_NO_STRING_DROP", "mobs", true,
//				"Determines if spiders drop strings or not. Affects both spiders and cave spiders. (true = no strings dropped)"));

//		DeltaHard.logger.info("GRAVITY = " + toggles.get("GRAVITY"));
//		DeltaHard.logger.info("NO_OBSIDIAN = " + toggles.get("NO_OBSIDIAN"));
//		DeltaHard.logger.info("LOOT = " + toggles.get("LOOT"));
//		DeltaHard.logger.info("CHANGE_BONEMEAL = " + toggles.get("CHANGE_BONEMEAL"));
//		DeltaHard.logger.info("NO_SLEEP = " + toggles.get("NO_SLEEP"));
//		DeltaHard.logger.info("SKELETON_NO_BOW_DROP = " + toggles.get("SKELETON_NO_BOW_DROP"));
//		DeltaHard.logger.info("SPIDER_NO_STRING_DROP = " + toggles.get("SPIDER_NO_STRING_DROP"));
	}

}
