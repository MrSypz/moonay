package sypztep.mamy.moonay.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;

public class EmptyEnchantment extends Enchantment {
    /**
     * This <strong>String</strong> is define a name of enchantment.
     */
    protected String name;
    protected EmptyEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
    }
    protected EmptyEnchantment(String name, Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
        this.name = name;
    }
    /**
     * When <strong>Construce Create</strong>.
     *
     * <p>protected <strong>method name</strong> (param) {<p>
     *  @Param  super(weight, target, slotTypes);<p>
     *  @Param setName("Write name here");
     *  <p>
     * }
     */
    protected void setName(String name) {
        this.name = name;
    }
    @Override
    public int getMinPower(int level) {
        return level * 25;
    }

    @Override
    public int getMaxPower(int level) {
        return this.getMinPower(level) + 50;
    }
    /**
     * Use in ModEnchantment by get name for each enchantment
     */

    public String getName() {
        return name;
    }
}
