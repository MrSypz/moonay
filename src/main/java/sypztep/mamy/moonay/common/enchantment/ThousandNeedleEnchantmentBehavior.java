package sypztep.mamy.moonay.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.UseAction;

public class ThousandNeedleEnchantmentBehavior extends OnHitApplyEnchantment{
    public ThousandNeedleEnchantmentBehavior(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
        this.name = "thousandneedle";
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }
    @Override
    public Enchantment getEnchantment() {
        return this;
    }

    @Override
    public UseAction useAction(ItemStack stack) {
        return UseAction.BOW;
    }
}
