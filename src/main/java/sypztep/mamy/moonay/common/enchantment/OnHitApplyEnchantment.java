package sypztep.mamy.moonay.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import sypztep.mamy.moonay.common.util.EnchantmentSpecialEffect;
import sypztep.mamy.moonay.common.util.ItemEnchantmentBehavior;

public class OnHitApplyEnchantment extends EmptyEnchantment implements EnchantmentSpecialEffect, ItemEnchantmentBehavior {
    public OnHitApplyEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
    }
    @Override
    public int getMaxLevel() {
        return 5;
    }
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof SwordItem;
    }

    @Override
    public void applyOnTarget(LivingEntity user, Entity target, int level) {

    }

    @Override
    public void applyOnUser(LivingEntity user, int level) {

    }

    @Override
    public Enchantment getEnchantment() {
        return this;
    }

    @Override
    public void onFinishUsing(ItemStack stack, World world, LivingEntity user, int level) {

    }

    @Override
    public int maxUseTime(ItemStack stack) {
        return 20;
    }

    @Override
    public UseAction useAction(ItemStack stack) {
        return null;
    }

    @Override
    public TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        return TypedActionResult.pass(stack);
    }
}
