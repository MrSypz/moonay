package sypztep.mamy.moonay.common.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.mamy.moonay.common.MoonayMod;
import sypztep.mamy.moonay.common.enchantment.CarveEnchantment;
import sypztep.mamy.moonay.common.enchantment.EmptyEnchantment;

import java.util.ArrayList;
import java.util.List;

public class ModEnchantments {
    public static EmptyEnchantment CARVE = new CarveEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND);
    public static void init() {
        init(CARVE);
    }
    private static void init(EmptyEnchantment enchantment) {
        String name = enchantment.getName(); // Assuming the getName method is implemented in your EmptyEnchantment class
        Registry.register(Registries.ENCHANTMENT, MoonayMod.id(name), enchantment);
    }
}
