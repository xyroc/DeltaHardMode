package xiroc.deltahardmode.network;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;

public class PacketClientExplosion {

	public double x;
	public double y;
	public double z;
	public float size;
	public boolean terrainDamage;
	public List<BlockPos> affectedBlockPositions;

	public PacketClientExplosion() {
	}

	public PacketClientExplosion(ByteBuf buf) {
		fromBytes(buf);
	}

	public PacketClientExplosion(double x, double y, double z, float size, boolean terrainDamage,
			List<BlockPos> affectedBlockPositions) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.size = size;
		this.affectedBlockPositions = affectedBlockPositions;
		this.terrainDamage = terrainDamage;
		// DeltaHard.logger.info("Number of Affected Block Positions: " +
		// affectedBlockPositions.size());
	}

	public void fromBytes(ByteBuf buf) {
		terrainDamage = buf.readBoolean();
		x = buf.readDouble();
		y = buf.readDouble();
		z = buf.readDouble();
		size = buf.readFloat();
		affectedBlockPositions = new ArrayList<BlockPos>();
		int blocks = buf.readInt();
		// DeltaHard.logger.info("Client Number of Affected Block Positions: " +
		// blocks);
		for (int i = 0; i < blocks; i++) {
			affectedBlockPositions.add(new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()));
		}
	}

	public void toBytes(ByteBuf buf) {
		buf.writeBoolean(terrainDamage);
		buf.writeDouble(x);
		buf.writeDouble(y);
		buf.writeDouble(z);
		buf.writeFloat(size);
		buf.writeInt(affectedBlockPositions.size());
		for (BlockPos pos : affectedBlockPositions) {
			buf.writeInt(pos.getX());
			buf.writeInt(pos.getY());
			buf.writeInt(pos.getZ());
		}
	}

//	public static class MessageHandlerClientExplosion implements IMessageHandler<PacketClientExplosion, IMessage> {
//
//		@Override
//		public IMessage onMessage(PacketClientExplosion message, MessageContext ctx) {
//			Minecraft minecraft = Minecraft.getMinecraft();
//			minecraft.addScheduledTask(new Runnable() {
//				@Override
//				public void run() {
//					Explosion explosion = new Explosion(minecraft.world, (Entity) null, message.x, message.y, message.z, message.size, false, message.terrainDamage, message.affectedBlockPositions);
//					explosion.doExplosionB(true);
//				}
//			});
//			return null;
//		}
//
//	}

}
