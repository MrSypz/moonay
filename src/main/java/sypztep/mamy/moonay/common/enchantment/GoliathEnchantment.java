package sypztep.mamy.moonay.common.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import sypztep.mamy.moonay.common.MoonayMod;
import sypztep.mamy.moonay.common.util.DamageHandler;
import sypztep.mamy.moonay.common.util.MoonayHelper;

import static sypztep.mamy.moonay.common.util.MoonayHelper.checkIsItemCorrectUse;

public class GoliathEnchantment extends OnHitApplyEnchantment implements DamageHandler {
    private static boolean shouldTriggerAdditionalDamage = false;
    public GoliathEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
        this.name =  "goliath";
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public void applyOnTarget(LivingEntity user, Entity target, int level) {
        if (MoonayHelper.hasEnchantWithRangeDistance(this, user, target, 6)) {
            return;
        }
        this.weaponType = checkIsItemCorrectUse(user);
        if (target instanceof LivingEntity && this.weaponType == MoonayHelper.WeaponType.SWORD) {
            setShouldTriggerAdditionalDamage(true);
        }
    }

    @Override
    public void setShouldTriggerAdditionalDamage(boolean value) {
        shouldTriggerAdditionalDamage = value;
    }

    @Override
    public boolean isShouldTriggerAdditionalDamage() {
        return shouldTriggerAdditionalDamage;
    }
}
