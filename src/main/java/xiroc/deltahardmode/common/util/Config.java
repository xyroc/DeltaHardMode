package xiroc.deltahardmode.common.util;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;

import java.nio.file.Path;
import java.util.HashMap;

public class Config {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec CONFIG;

    private static final HashMap<String, BooleanValue> TOGGLES;

    private static final String CONFIG_MAIN = "configuration";

    static {
        TOGGLES = new HashMap<>();

        BUILDER.comment("Settings").push(CONFIG_MAIN);
        TOGGLES.put("gravity", BUILDER.comment(
                "If enabled, all blocks listed in the datapack file data/deltahardmode/gravity.json (\"gravity_blocks\") will be affected by gravity.")
                .define("gravity", true));
        TOGGLES.put("obsidian", BUILDER.comment("If set to true, obsidian will not be mineable with a diamond pickaxe.").define("hard_obsidian", true));
        TOGGLES.put("bonemeal", BUILDER.comment("Determines if the bone meal changes should be applied.").define("change_bonemeal", true));
        TOGGLES.put("no_sleeping", BUILDER.comment("Determines whether the player should not be able to sleep.").define("no_sleeping", true));
        BUILDER.pop();

        CONFIG = BUILDER.build();
    }

    public static void load(Path path) {
        final CommentedFileConfig config = CommentedFileConfig.builder(path).sync().autosave()
                .writingMode(WritingMode.REPLACE).build();
        config.load();
        CONFIG.setConfig(config);
    }

    public static boolean isDisabled(String name) {
        return !TOGGLES.get(name).get();
    }

}
