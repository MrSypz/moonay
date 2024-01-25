package sypztep.mamy.moonay.common.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;

public interface SpecialEnchantment {
    void onFinishUsing(ItemStack stack, World world, LivingEntity user);

    int getMaxUseTime(ItemStack stack);

    UseAction getUseAction(ItemStack stack);

    TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand, ItemStack stack);
}