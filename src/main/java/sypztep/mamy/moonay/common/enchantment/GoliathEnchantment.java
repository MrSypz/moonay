package sypztep.mamy.moonay.common.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import sypztep.mamy.moonay.common.init.ModSoundEvents;
import sypztep.mamy.moonay.common.init.ModStatusEffects;
import sypztep.mamy.moonay.common.util.DamageHandler;
import sypztep.mamy.moonay.common.util.MoonayHelper;

public class GoliathEnchantment extends OnHitApplyEnchantment implements DamageHandler {
    private static boolean shouldTriggerAdditionalDamage = false;
    public GoliathEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
        this.setName("goliath");
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public void applyOnTarget(LivingEntity user, Entity target, int level) {
        if (MoonayHelper.hasEnchantWithRangeDistance(this, user, target, 7)) {
            return;
        }
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
