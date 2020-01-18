package xiroc.deltahardmode;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.Explosion;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import xiroc.deltahardmode.common.event.EventManager;
import xiroc.deltahardmode.network.PacketClientExplosion;

@Mod(DeltaHardMode.MODID)
public class DeltaHardMode {

	public static final String MODID = "deltahardmode";
	public static final Logger LOGGER = LogManager.getLogger();

	public static final String NETWORK_PROTOCOL_VERSION = "1.0";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(locate("network_channel"),
			() -> NETWORK_PROTOCOL_VERSION, NETWORK_PROTOCOL_VERSION::equals, NETWORK_PROTOCOL_VERSION::equals);

	public DeltaHardMode() {
		LOGGER.info("Challenge awaits...");
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
		MinecraftForge.EVENT_BUS.register(new EventManager());
	}

	private void commonSetup(FMLCommonSetupEvent event) {
		int packetID = 0;
		CHANNEL.registerMessage(packetID++, PacketClientExplosion.class, (msg, buf) -> {
			msg.toBytes(buf);
		}, (buf) -> new PacketClientExplosion(buf), (msg, ctx) -> {
			DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
				LOGGER.info("Client explosion!");
				new Explosion(Minecraft.getInstance().world, null, msg.x, msg.y, msg.z, msg.size, msg.affectedBlockPositions).doExplosionB(true);
			});
		});
		
		try {
			Field hardness = Block.class.getDeclaredField("field_149782_v ");
			hardness.setAccessible(true);
			Field finalField = Field.class.getDeclaredField("modifiers");
			finalField.setAccessible(true);
			finalField.setInt(hardness, hardness.getModifiers() & ~Modifier.FINAL);
			
			hardness.setFloat(Blocks.OBSIDIAN, 6.0F);
		} catch (Exception e) {
			LOGGER.error("Failed to change the obsidian hardness.");
			e.printStackTrace();
		}
	}

	public static ResourceLocation locate(String path) {
		return new ResourceLocation(MODID, path);
	}

}
