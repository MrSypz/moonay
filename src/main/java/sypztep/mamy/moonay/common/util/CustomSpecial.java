package sypztep.mamy.moonay.common.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public interface CustomSpecial {
    void applyOnTarget(LivingEntity user, Entity target, int level);
    void applyOnUser(LivingEntity user, int level);
    Enchantment getEnchantment();
}
