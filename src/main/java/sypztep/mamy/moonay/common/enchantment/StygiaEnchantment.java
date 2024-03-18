package sypztep.mamy.moonay.common.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class StygiaEnchantment extends EmptyEnchantment {
    @Override
    public int getMaxLevel() {
        return 1;
    }

    public StygiaEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
        this.setName("stygia");
    }
    /*
    Part onUserDamage Move to Mixin now for more useful
     */
}
