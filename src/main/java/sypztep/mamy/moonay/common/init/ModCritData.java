package sypztep.mamy.moonay.common.init;

import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import sypztep.mamy.moonay.common.data.CritOverhaulConfig;
import sypztep.mamy.moonay.common.data.CritOverhaulEntry;

import java.util.List;

import static java.util.List.of;
import static net.minecraft.item.ArmorMaterials.*;

public class ModCritData {
    private static final String MC = "minecraft:";
    private static final CritOverhaulConfig CRIT_OVERHAUL_CONFIG = new CritOverhaulConfig();

    public static void initItemData() {

        List<CritOverhaulEntry> itemsToManualAdd = of(
                new CritOverhaulEntry(MC + "trident", 12.0f, 15.0f),
                new CritOverhaulEntry(MC + "bow", 12.0f, 15.0f),
                new CritOverhaulEntry(MC + "crossbow", 12.0f, 15.0f),
                new CritOverhaulEntry(MC + "turtle_helmet", 4.0f, 6.0f),
                new CritOverhaulEntry(MC + "elytra", -20.0f, -12.0f) // I hate elytra :) nothing personal
        );

        for (Item item : Registries.ITEM) {
            String itemId = Registries.ITEM.getId(item).toString();

            if (item instanceof SwordItem swordItem) {
                ToolMaterial axeItemMaterial = swordItem.getMaterial();
                float critChance = getToolCritChance(axeItemMaterial) * 2;
                float critDamage = 3.0f;
                CRIT_OVERHAUL_CONFIG.addItems(of(new CritOverhaulEntry(itemId, critChance, critDamage)));
            }

            if (item instanceof ArmorItem armorItem) {
                ArmorMaterial armorMaterial = armorItem.getMaterial();
                float critChance = getArmorCritChance(armorMaterial);
                float critDamage = 2.0f;
                CRIT_OVERHAUL_CONFIG.addItems(of(new CritOverhaulEntry(itemId, critChance, critDamage)));
            }

            if (item instanceof AxeItem axeItem) {
                ToolMaterial axeItemMaterial = axeItem.getMaterial();
                float critChance = getToolCritChance(axeItemMaterial);
                float critDamage = 12.0f;
                CRIT_OVERHAUL_CONFIG.addItems(of(new CritOverhaulEntry(itemId, critChance, critDamage)));
            }

            if (item instanceof HoeItem hoeItem) {
                ToolMaterial hoeItemMaterial = hoeItem.getMaterial();
                float critChance = getToolCritChance(hoeItemMaterial) * 3;
                float critDamage = 3.0f;
                CRIT_OVERHAUL_CONFIG.addItems(of(new CritOverhaulEntry(itemId, critChance, critDamage)));
            }

            if (item instanceof PickaxeItem pickaxeItem) {
                ToolMaterial pickaxeItemMaterial = pickaxeItem.getMaterial();
                float critChance = getToolCritChance(pickaxeItemMaterial) * 1.5f;
                float critDamage = 3.0f;
                CRIT_OVERHAUL_CONFIG.addItems(of(new CritOverhaulEntry(itemId, critChance, critDamage)));
            }

            if (item instanceof ShovelItem shovelItem) {
                ToolMaterial shovelItemMaterial = shovelItem.getMaterial();
                float critChance = getToolCritChance(shovelItemMaterial) * 1.25f;
                float critDamage = 3.0f;
                CRIT_OVERHAUL_CONFIG.addItems(of(new CritOverhaulEntry(itemId, critChance, critDamage)));
            }
        }
        CRIT_OVERHAUL_CONFIG.addItems(itemsToManualAdd);
    }
    private static float getArmorCritChance(ArmorMaterial armorMaterial) {
        if (armorMaterial.equals(LEATHER)) {
            return 2.25f; // 9
        } else if (armorMaterial.equals(IRON)) {
            return 3.5f; // 14
        } else if (armorMaterial.equals(GOLD)) {
            return 2.75f; // 11
        } else if (armorMaterial.equals(CHAIN)) {
            return 5.25f; // 21
        } else if (armorMaterial.equals(DIAMOND)) {
            return 4.25f; // 17
        } else if (armorMaterial.equals(NETHERITE)) {
            return 4.75f; // 18
        }
        return 0f;  // Set a default value for other armor materials
    }
    private static float getToolCritChance(ToolMaterial toolMaterial) {
        if (toolMaterial == ToolMaterials.WOOD) {
            return 2.0f;
        } else if (toolMaterial == ToolMaterials.STONE) {
            return 2.5f;
        } else if (toolMaterial == ToolMaterials.IRON) {
            return 3.0f;
        } else if (toolMaterial == ToolMaterials.GOLD) {
            return 3.5f;
        } else if (toolMaterial == ToolMaterials.DIAMOND) {
            return 4.0f;
        } else if (toolMaterial == ToolMaterials.NETHERITE) {
            return 4.5f;
        } else {
            return 0f;
        }
    }
}
