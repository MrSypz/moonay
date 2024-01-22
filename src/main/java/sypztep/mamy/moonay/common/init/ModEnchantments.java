package sypztep.mamy.moonay.common.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.mamy.moonay.common.MoonayMod;
import sypztep.mamy.moonay.common.enchantment.CarveEnchantment;
import sypztep.mamy.moonay.common.enchantment.EmptyEnchantment;

public class ModEnchantments {
    public static EmptyEnchantment CARVE = new CarveEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND);
    public static void init() {
        reg("carve", CARVE);
    }
    public static void reg(String name, EmptyEnchantment enchantment){
        Registry.register(Registries.ENCHANTMENT,MoonayMod.id(name), enchantment);
    }
}
