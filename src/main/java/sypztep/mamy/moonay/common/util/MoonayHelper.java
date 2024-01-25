package sypztep.mamy.moonay.common.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import sypztep.mamy.moonay.common.init.ModStatusEffects;
import sypztep.mamy.moonay.common.packetc2s.CarveSoulPacket;

public class MoonayHelper {
    public static boolean hasEnt(Enchantment enchantment, ItemStack stack) {
        return EnchantmentHelper.getLevel(enchantment, stack) > 0;
    }
    public static int getEntLvl(Enchantment enchantment, ItemStack stack) {
        return EnchantmentHelper.getLevel(enchantment, stack);
    }
    public static boolean dontHasThisStatusEffect(StatusEffect statusEffect, LivingEntity user) {
        return user.getStatusEffect(statusEffect) == null;
    }
    public static boolean stillHasThisStatusEffect(StatusEffect statusEffect, LivingEntity user) {
        return user.getStatusEffect(statusEffect) != null;
    }
    public static boolean hasStatusWithAmpValue(StatusEffect statusEffect, LivingEntity user, int lessthan) {
        StatusEffectInstance instance = user.getStatusEffect(statusEffect);
        if (instance != null)
            return instance.getAmplifier() < lessthan;
        return false;
    }
    public static int getStatusAmp(StatusEffect statusEffect,LivingEntity user) {
        int amp = 0;
        StatusEffectInstance instance = user.getStatusEffect(statusEffect);
        if (instance != null)
            amp = instance.getAmplifier();
        return amp;
    }
    public static boolean hasEntWithLimitDistance(Enchantment enchantment,LivingEntity user, Entity target,double lessthan) {
        return hasEnt(enchantment, user.getMainHandStack()) || user.distanceTo(target) >= lessthan || !(target instanceof LivingEntity);
    }
    public static int getStatusCount(LivingEntity user, StatusEffect statusEffect, int i) {
        StatusEffectInstance cooldownInstance = user.getStatusEffect(statusEffect);
        if (cooldownInstance != null) {
            int amplifier = cooldownInstance.getAmplifier();
            if (amplifier < i + 1 && !user.handSwinging)
                amplifier++;
            return amplifier;
        }
        return 0; // Default value if the status effect is not present
    }
    public static void carvesoulParticle(Entity entity) {
        for (int phi = 0; phi <= 180; phi += 8) {
            for (int theta = 0; theta < 360; theta += 8) {
                double _3D = Math.toRadians(phi);
                double _4D = Math.toRadians(theta);
                double radius = 3 * 0.3;
                double x = radius * Math.sin(_3D) * Math.cos(_4D) * 1.5;
                double y = radius * Math.cos(_3D) * 1.5;
                double z = radius * Math.sin(_3D) * Math.sin(_4D) * 1.5;
                double vx = x * 0.3;
                double vy = y * 0.3;
                double vz = z * 0.3;
                entity.getWorld().addParticle(ParticleTypes.SOUL, entity.getX() + x, entity.getY() + y + 0.5, entity.getZ() + z, vx, vy, vz);
            }
        }
    }
}
