package xiroc.deltahardmode.common.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class RayTrace {

	public static RayTraceResult rayTrace(World worldIn, PlayerEntity playerIn) {
		float f = playerIn.rotationPitch;
		float f1 = playerIn.rotationYaw;
		double d0 = playerIn.getPosX();
		double d1 = playerIn.getPosY() + (double) playerIn.getEyeHeight();
		double d2 = playerIn.getPosZ();
		Vec3d vec3d = new Vec3d(d0, d1, d2);
		float f2 = MathHelper.cos(-f1 * 0.017453292F - (float) Math.PI);
		float f3 = MathHelper.sin(-f1 * 0.017453292F - (float) Math.PI);
		float f4 = -MathHelper.cos(-f * 0.017453292F);
		float f5 = MathHelper.sin(-f * 0.017453292F);
		float f6 = f3 * f4;
		float f7 = f2 * f4;
		double d3 = playerIn.getAttribute(PlayerEntity.REACH_DISTANCE).getValue();
		Vec3d vec3d1 = vec3d.add((double) f6 * d3, (double) f5 * d3, (double) f7 * d3);
//		return worldIn.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
		return worldIn.rayTraceBlocks(new RayTraceContext(vec3d, vec3d1, BlockMode.COLLIDER, FluidMode.SOURCE_ONLY, playerIn));
	}

}
