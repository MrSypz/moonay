package sypztep.mamy.moonay.common.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import sypztep.mamy.moonay.common.MoonayMod;

public class HadesSpineEnchantment extends EmptyEnchantment {
    private static final String HADES_SPINE_KEY = MoonayMod.MODID + "HadesSpineCount";
    public HadesSpineEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
        this.setName("hadesspine");
    }

    private void addHadesSpineCount(ItemStack stack, int amount) {
        NbtCompound tag = stack.getOrCreateNbt();
        int currentCount = tag.getInt(HADES_SPINE_KEY);
        tag.putInt(HADES_SPINE_KEY, currentCount + amount);
    }

    private int getHadesSpineCount(ItemStack stack) {
        NbtCompound tag = stack.getOrCreateNbt();
        return tag.getInt(HADES_SPINE_KEY);
    }
}
