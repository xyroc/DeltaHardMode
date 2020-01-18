package xiroc.deltahard.common.util;

import java.util.Collection;

import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAreaEffectCloud;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class ExplosionHelper {

	public static Explosion newExplosion(@Nullable Entity entityIn, World world, double x, double y, double z, float radius, boolean isFlaming, boolean damage) {
		Explosion explosion = new Explosion(world, entityIn, x, y, z, radius, isFlaming, damage);
		if (!world.isRemote)
			explosion.doExplosionA();
		explosion.doExplosionB(true);
		return explosion;
	}

	public static void spawnLingeringCloud(EntityCreeper creeper) {
		Collection<PotionEffect> collection = creeper.getActivePotionEffects();

		if (!collection.isEmpty()) {
			EntityAreaEffectCloud entityareaeffectcloud = new EntityAreaEffectCloud(creeper.world, creeper.posX, creeper.posY, creeper.posZ);
			entityareaeffectcloud.setRadius(2.5F);
			entityareaeffectcloud.setRadiusOnUse(-0.5F);
			entityareaeffectcloud.setWaitTime(10);
			entityareaeffectcloud.setDuration(entityareaeffectcloud.getDuration() / 2);
			entityareaeffectcloud.setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float) entityareaeffectcloud.getDuration());

			for (PotionEffect potioneffect : collection) {
				entityareaeffectcloud.addEffect(new PotionEffect(potioneffect));
			}

			creeper.world.spawnEntity(entityareaeffectcloud);
		}
	}

}
