package sypztep.mamy.moonay.common.util;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

import java.util.List;

public class MoonayHelper {
    public static boolean hasEnchantment(Enchantment enchantment, ItemStack stack) {
        return EnchantmentHelper.getLevel(enchantment, stack) > 0;
    }
    public static int getEnchantmentLvl(Enchantment enchantment, ItemStack stack) {
        return EnchantmentHelper.getLevel(enchantment, stack);
    }
    public static boolean dontHaveThisStatusEffect(StatusEffect statusEffect, LivingEntity user) {
        return user.getStatusEffect(statusEffect) == null;
    }
    public static boolean stillHaveThisStatusEffect(StatusEffect statusEffect, LivingEntity user) {
        return user.getStatusEffect(statusEffect) != null;
    }
    public static void addStatus(LivingEntity user, StatusEffect effect, int time) {
        user.addStatusEffect(new StatusEffectInstance(effect,time));
    }
    public static void addStatus(LivingEntity user, StatusEffect effect, int time, int amp) {
        user.addStatusEffect(new StatusEffectInstance(effect,time,amp));
    }
    public static boolean hasStatusWithAmpValue$lessthan(StatusEffect statusEffect, LivingEntity user, int lessthan) {
        StatusEffectInstance instance = user.getStatusEffect(statusEffect);
        if (instance != null)
            return instance.getAmplifier() < lessthan;
        return false;
    }
    public static boolean hasStatusWithAmpValue$greather(StatusEffect statusEffect, LivingEntity user, int greathan) {
        StatusEffectInstance instance = user.getStatusEffect(statusEffect);
        if (instance != null)
            return instance.getAmplifier() > greathan;
        return false;
    }
    public static int getStatusAmp(StatusEffect statusEffect,LivingEntity user) {
        int amp = 0;
        StatusEffectInstance instance = user.getStatusEffect(statusEffect);
        if (instance != null)
            amp = instance.getAmplifier();
        return amp;
    }
    public static boolean hasEnchantWithRangeDistance(Enchantment enchantment, LivingEntity user, Entity target, double lessthan) {
        return !hasEnchantment(enchantment, user.getMainHandStack()) && !(user.distanceTo(target) >= lessthan) && target instanceof LivingEntity;
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
    public static boolean hasCustomSpecial(ItemStack stack) {
        return getCustomSpecial(stack) != null;
    }
    public static boolean hasDamageHandler(ItemStack stack) {
        return getDamageHandler(stack) != null;
    }

    public static ItemEnchantmentBehavior getSpecialEnchantment(ItemStack stack) {
        for (Enchantment enchantment : EnchantmentHelper.get(stack).keySet()) {
            if (enchantment instanceof ItemEnchantmentBehavior) {
                return (ItemEnchantmentBehavior) enchantment;
            }
        }
        return null;
    }
    public static EnchantmentSpecialEffect getCustomSpecial(ItemStack stack) {
        for (Enchantment enchantment : EnchantmentHelper.get(stack).keySet()) {
            if (enchantment instanceof EnchantmentSpecialEffect) {
                return (EnchantmentSpecialEffect) enchantment;
            }
        }
        return null;
    }
    public static DamageHandler getDamageHandler(ItemStack stack) {
        for (Enchantment enchantment : EnchantmentHelper.get(stack).keySet()) {
            if (enchantment instanceof DamageHandler) {
                return (DamageHandler) enchantment;
            }
        }
        return null;
    }
    public static boolean shouldHurt(Entity attacker, Entity hitEntity) {
        if (attacker == null || hitEntity == null) {
            return true;
        }
        if (attacker == hitEntity) {
            return false;
        }
        if (hitEntity instanceof PlayerEntity hitPlayer && attacker instanceof PlayerEntity attackingPlayer) {
            return attackingPlayer.shouldDamagePlayer(hitPlayer);
        } else if (hitEntity instanceof Ownable ownable) {
            return shouldHurt(attacker, ownable.getOwner());
        }
        return true;
    }
    public static void drawtextcustom(DrawContext context, TextRenderer textRenderer, String text, int x, int y , int color, int board, boolean shadow){
        context.drawText(textRenderer,text,x + 1,y,board,shadow);
        context.drawText(textRenderer,text,x - 1,y,board,shadow);
        context.drawText(textRenderer,text,x ,y + 1,board,shadow);
        context.drawText(textRenderer,text,x ,y - 1,board,shadow);
        context.drawText(textRenderer,text,x,y,color,shadow);
    }
    public static void drawtextcustom(DrawContext context, TextRenderer textRenderer, String text, int x, int y , int color, boolean shadow){
        context.drawText(textRenderer,text,x + 1,y,color,shadow);
        context.drawText(textRenderer,text,x - 1,y,color,shadow);
        context.drawText(textRenderer,text,x ,y + 1,color,shadow);
        context.drawText(textRenderer,text,x ,y - 1,color,shadow);
        context.drawText(textRenderer,text,x,y,color,shadow);
    }
    public static WeaponType checkIsItemCorrectUse(LivingEntity user) {
        var heldItem = user.getMainHandStack().getItem();
        if (heldItem instanceof AxeItem) return WeaponType.AXE;
        if (heldItem instanceof SwordItem) return WeaponType.SWORD;
        if (heldItem instanceof HoeItem) return WeaponType.HOE;
        return null; // No need for a default branch here
    }


    public enum WeaponType {
        AXE(),
        SWORD(),
        HOE();

        WeaponType() {
        }
    }

}
