package sypztep.mamy.moonay.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class EmptyEnchantment extends Enchantment {
    protected String name;
    protected int level;
    public EmptyEnchantment(String name,Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
        this.name = name;
    }
    public EmptyEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
    }
    public EmptyEnchantment(String name, int lvl, Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
        this.name = name;
        this.level = lvl;
    }
    protected void setName(String name) {
        this.name = name;
    }

    @Override
    public int getMaxLevel() {
        return this.level;
    }

    @Override
    public int getMinPower(int level) {
        return level * 25;
    }

    @Override
    public int getMaxPower(int level) {
        return this.getMinPower(level) + 50;
    }
    public String getName() {
        return name;
    }
}
