package sypztep.mamy.moonay.common.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

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
    public static WeaponType checkIsItemCorrectUse(LivingEntity user, ItemStack stack) {
        if (stack.getItem() instanceof AxeItem)
            return WeaponType.AXE;
         else if (stack.getItem()instanceof SwordItem)
            return WeaponType.SWORD;
        // Default case or unknown item type
        return null;
    }
    public enum WeaponType {
        AXE(AxeItem.class),
        SWORD(SwordItem.class);

        private final Class<? extends Item> itemClass;

        WeaponType(Class<? extends Item> itemClass) {
            this.itemClass = itemClass;
        }

        public Class<? extends Item> getItemClass() {
            return itemClass;
        }
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

    public static boolean hasSpecialEnchantment(ItemStack stack) {
        return getSpecialEnchantment(stack) != null;
    }

    public static SpecialEnchantment getSpecialEnchantment(ItemStack stack) {
        for (Enchantment enchantment : EnchantmentHelper.get(stack).keySet()) {
            if (enchantment instanceof SpecialEnchantment) {
                return (SpecialEnchantment) enchantment;
            }
        }
        return null;
    }
}
