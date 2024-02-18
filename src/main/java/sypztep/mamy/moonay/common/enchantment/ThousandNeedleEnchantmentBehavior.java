package sypztep.mamy.moonay.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public class ThousandNeedleEnchantmentBehavior extends OnHitApplyEnchantment {
    public ThousandNeedleEnchantmentBehavior(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
        this.name = "thousandneedle";
    }
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof HoeItem;
    }

    @Override
    public int getMaxLevel() {
        return 10;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }
    @Override
    public UseAction useAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        return TypedActionResult.pass(stack);
    }
}
