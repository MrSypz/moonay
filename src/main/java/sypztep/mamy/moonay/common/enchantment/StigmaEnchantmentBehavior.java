package sypztep.mamy.moonay.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import sypztep.mamy.moonay.common.init.*;
import sypztep.mamy.moonay.common.packetc2s.StigmaPacket;
import sypztep.mamy.moonay.common.util.*;

import static sypztep.mamy.moonay.common.util.MoonayHelper.checkIsItemCorrectUse;

public class StigmaEnchantmentBehavior extends AxeEnchantment implements ItemEnchantmentBehavior, EnchantmentSpecialEffect, DamageHandler {
    private static boolean shouldTriggerAdditionalDamage = false;
    public StigmaEnchantmentBehavior(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
        this.setName("stigma");
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != ModEnchantments.CARVE;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }
    @Override
    public void onFinishUsing(ItemStack stack, World world, LivingEntity user, int level) {
        double damage = user.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        double amount = ((0.25f + AbilityHelper.getMissingHealth(user, ModConfig.CONFIG.stigmahealratio)) * AbilityHelper.getHitAmount());
        AbilityHelper.boxArea(user, user.getWorld().getDamageSources().playerAttack((PlayerEntity) user), ModConfig.CONFIG.stigmarange, (float) damage * 1.5f,1.0f); //150% Damage base on player attack damage
        if (user.getWorld().isClient())
            StigmaPacket.send();
        stigmaParticle(user);
        user.heal((float) amount);
        MoonayHelper.addStatus(user,ModStatusEffects.STIGMA_COOLDOWN, 600 - (level * 80));
    }
    public static void stigmaParticle(Entity entity) {
        entity.getWorld().addParticle(ModParticles.BLOODWAVE, entity.getX(), entity.getY() + 0.1, entity.getZ(), 0, 0, 0);
        entity.getWorld().playSound(null,entity.getBlockPos(), ModSoundEvents.ITEM_STIGMA, SoundCategory.PLAYERS,1,1f);
    }

    @Override
    public TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        MoonayHelper.WeaponType weaponType = checkIsItemCorrectUse(user);
        if (MoonayHelper.hasEnchantment(this, stack) && MoonayHelper.dontHaveThisStatusEffect(ModStatusEffects.STIGMA_COOLDOWN, user) && weaponType == MoonayHelper.WeaponType.AXE) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        } else if (weaponType == MoonayHelper.WeaponType.SWORD)
            return TypedActionResult.pass(stack);
        return TypedActionResult.pass(stack);
    }

    @Override
    public void applyOnTarget(LivingEntity user, Entity target, int level) {
        if (MoonayHelper.hasEnchantWithRangeDistance(this, user, target, 6)) {
            return;
        }
        if (target instanceof LivingEntity livingTarget && checkIsItemCorrectUse(user) == MoonayHelper.WeaponType.AXE) {
            if (AbilityHelper.targetMissingHealthPercentBelow(livingTarget,0.015f * level) && !target.equals(user))
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
    @Override
    public void applyOnUser(LivingEntity user, int level) {
    }
    @Override
    public int maxUseTime(ItemStack stack) {
        return 10;
    }

    @Override
    public UseAction useAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public Enchantment getEnchantment() {
        return this;
    }


}
