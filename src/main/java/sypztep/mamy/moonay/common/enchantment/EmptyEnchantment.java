package sypztep.mamy.moonay.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class EmptyEnchantment extends Enchantment {
    protected String name;
    protected int level;
    protected boolean REQUIRES_PREFERRED_SLOT = true;
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
    public boolean requiresPreferredSlot(){
        return REQUIRES_PREFERRED_SLOT;
    }
    public void onEquipmentChange(int oldLevel, int newLevel, ItemStack oldItem, ItemStack newItem, LivingEntity entity){

    }

}
