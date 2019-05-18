package xiroc.deltahard.common.util;

import java.net.URL;

import org.jline.utils.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import net.minecraft.util.text.TextFormatting;
import xiroc.deltahard.DeltaHard;

public class Reference {

	public static final String NAME = "DeltaHard";
	public static final String MODID = "deltahard";
	public static final String VERSION = "beta";
	public static final String VERSION_STRING = "0.5.3";
	public static final String AUTHOR = TextFormatting.GREEN + "XIROC";
	public static final String CREDITS = "Idea by " + TextFormatting.GREEN + "ladallas_texas";
	public static final String MINECRAFTVERSIONS = "[1.12],[1.12.1],[1.12.2]";

	public static String newVersionString;

	private static final String adress = "https://onedrive.live.com/download?cid=38549A55717AFBAB&resid=38549A55717AFBAB%21115&authkey=AMcXfen-vwoCftc";

	public static void checkForNewVersion() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					URL url = new URL(adress);
					Gson gson = new GsonBuilder().setPrettyPrinting().create();
					JsonObject object = gson.fromJson(new InputStreamReader(url.openStream()), JsonObject.class);
					if (versionNewerThan(object.get("version").getAsString(), VERSION_STRING)) {
						newVersionString = object.get("name").getAsString() + " - " + object.get("minecraft_version").getAsString() + " - " + object.get("version").getAsString() + " (" + object.get("description").getAsString() + ")";
						DeltaHard.logger.info("A new version of Delta Hard Mode is out: " + newVersionString);
					}
				} catch (Exception e) {
					DeltaHard.logger.error("Version check failed: " + e.getMessage());
				}

			}
		}).start();
	}

	private static boolean versionNewerThan(String version1, String version2) {
		// 1 newer than 2 (only 1 digit version numbers supported ex. 1.1.1 or 0.4.2)
		try {
			if (Integer.parseInt(version1.replace(".", "")) > Integer.parseInt(version2.replace(".", "")))
				return true;
			return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

}
