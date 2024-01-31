package sypztep.mamy.moonay.common.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.mamy.moonay.common.MoonayMod;
import sypztep.mamy.moonay.common.enchantment.CarveEnchantment;
import sypztep.mamy.moonay.common.enchantment.EmptyEnchantment;
import sypztep.mamy.moonay.common.enchantment.StigmaEnchantment;
import sypztep.mamy.moonay.common.enchantment.SwordEnchantment;

public class ModEnchantments {
    /**
     * <Strong>Sword
     */
    public static EmptyEnchantment CARVE = new CarveEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND);
    public static EmptyEnchantment HEXA_EXPERIMENT = new SwordEnchantment("hexa_experiment", Enchantment.Rarity.VERY_RARE,EnchantmentTarget.WEAPON,EquipmentSlot.MAINHAND);
    /**
     * <Strong>Axe
     */
    public static EmptyEnchantment STIGMA = new StigmaEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND);
    public static void init() {
        init(CARVE);
        init(HEXA_EXPERIMENT);
        init(STIGMA);
    }
    private static void init(EmptyEnchantment enchantment) {
        String name = enchantment.getName();
        Registry.register(Registries.ENCHANTMENT, MoonayMod.id(name), enchantment);
    }
}
