package xiroc.deltahardmode.common.util;

import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;

public class ExplosionHelper {

    public static Explosion newExplosion(@Nullable Entity entityIn, World world, double x, double y, double z,
                                         float radius, boolean isFlaming, boolean damage) {
        Explosion explosion = new Explosion(world, entityIn, x, y, z, radius, isFlaming,
                damage ? Explosion.Mode.DESTROY : Explosion.Mode.NONE);
        if (!world.isRemote)
            explosion.doExplosionA();
        explosion.doExplosionB(true);
        return explosion;
    }

    public static void spawnLingeringCloud(CreeperEntity creeper) {
        Collection<EffectInstance> collection = creeper.getActivePotionEffects();

        if (!collection.isEmpty()) {
            AreaEffectCloudEntity entityareaeffectcloud = new AreaEffectCloudEntity(creeper.world, creeper.getPosX(),
                    creeper.getPosY(), creeper.getPosZ());
            entityareaeffectcloud.setRadius(2.5F);
            entityareaeffectcloud.setRadiusOnUse(-0.5F);
            entityareaeffectcloud.setWaitTime(10);
            entityareaeffectcloud.setDuration(entityareaeffectcloud.getDuration() / 2);
            entityareaeffectcloud
                    .setRadiusPerTick(-entityareaeffectcloud.getRadius() / (float) entityareaeffectcloud.getDuration());

            for (EffectInstance potioneffect : collection) {
                entityareaeffectcloud.addEffect(new EffectInstance(potioneffect));
            }

            creeper.world.addEntity(entityareaeffectcloud);
        }
    }

}
