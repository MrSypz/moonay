package sypztep.mamy.moonay.common.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;

public interface CustomSpecial {
    void applyOnTarget(LivingEntity user, Entity target);
    void applyOnUser(LivingEntity user);
}
