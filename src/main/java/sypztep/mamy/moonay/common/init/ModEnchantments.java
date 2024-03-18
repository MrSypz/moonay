package sypztep.mamy.moonay.common.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.mamy.moonay.common.MoonayMod;
import sypztep.mamy.moonay.common.enchantment.*;

public class ModEnchantments {
    /**
     * <Strong>Sword
     */
    public static EmptyEnchantment CARVE = new CarveEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND);
    public static EmptyEnchantment APINOX = new EmptyEnchantment("apinox",3,Enchantment.Rarity.RARE,EnchantmentTarget.WEAPON,EquipmentSlot.MAINHAND);
    public static EmptyEnchantment PRAMINAX = new PraminaxEnchantment(Enchantment.Rarity.RARE,EnchantmentTarget.WEAPON,EquipmentSlot.MAINHAND);
    public static EmptyEnchantment GOLIATH = new GoliathEnchantment(Enchantment.Rarity.RARE,EnchantmentTarget.WEAPON,EquipmentSlot.MAINHAND);
    /**
     * <Strong>Hoe
     */
    public static EmptyEnchantment THOUSAND_NEEDLE = new ThousandNeedleEnchantment(Enchantment.Rarity.VERY_RARE,EnchantmentTarget.WEAPON,EquipmentSlot.MAINHAND);
    /**
     * <Strong>Axe
     */
    public static EmptyEnchantment STIGMA = new StigmaEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND);
    public static EmptyEnchantment STYGIA = new StygiaEnchantment(Enchantment.Rarity.RARE,EnchantmentTarget.WEARABLE, EquipmentSlot.LEGS);
    public static void init() {
        init(CARVE);
        init(APINOX);
        init(PRAMINAX);
        init(GOLIATH);
        init(THOUSAND_NEEDLE);
        init(STIGMA);
        init(STYGIA);
    }
    private static void init(EmptyEnchantment enchantment) {
        String name = enchantment.getName();
        Registry.register(Registries.ENCHANTMENT, MoonayMod.id(name), enchantment);
    }
}
