package sypztep.mamy.moonay.common.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import sypztep.mamy.moonay.common.init.ModDamageTypes;

import java.util.List;

public class AbilityHelper {
    private static int counts;
    public static void boxDamage(LivingEntity user, DamageSource source, double range, float amount) {
        List<LivingEntity> entities = user.getWorld().getNonSpectatingEntities(LivingEntity.class, user.getBoundingBox().expand(range,3, range));
        counts = 0;
        for (LivingEntity target : entities) {
            if (target != user) {
                double distanceToEntity = target.squaredDistanceTo(user.getX(), user.getY(), user.getZ());
                double normalizedDistance = Math.sqrt(distanceToEntity) / range; // Adjust as needed for your range
                if (normalizedDistance > 0.0f)
                    counts++;
                else counts = 0;
                float damagebyArea = amount - (float) (normalizedDistance * (amount - 1.0f));
                target.damage(source, damagebyArea);
            }
        }
    }
    public static float getMissingHealth(LivingEntity living) {
        return living.getMaxHealth() - living.getHealth();
    }
    public static float getMissingHealth(LivingEntity living, float percentofmissinghealth) {
        return (living.getMaxHealth() - living.getHealth()) * percentofmissinghealth;
    }
    public static int getHitAmount(){
        return counts;
    }
}
