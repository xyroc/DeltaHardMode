package xiroc.deltahard.common.util;

import java.util.List;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Sets;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentProtection;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class ExplosionHelper {

	public static Explosion newExplosion(@Nullable Entity entityIn, World world, double x, double y, double z, float radius, boolean isFlaming, boolean damage) {
		Explosion explosion = new Explosion(world, entityIn, x, y, z, radius, isFlaming, damage);
		explosion.doExplosionA();
		explosion.doExplosionB(true);
		return explosion;
	}

}
