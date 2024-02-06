package sypztep.mamy.moonay.mixin.enchantment.special;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.mamy.moonay.common.util.MoonayHelper;

import java.util.Objects;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "finishUsing", at = @At("HEAD"))
    private void moonay$finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (MoonayHelper.hasSpecialEnchantment(stack)) {
            ItemStack mainHandStack = user.getMainHandStack();
            int lvl = MoonayHelper.getEnchantmentLvl(Objects.requireNonNull(MoonayHelper.getCustomSpecial(mainHandStack)).getEnchantment(), mainHandStack);
            Objects.requireNonNull(MoonayHelper.getSpecialEnchantment(stack)).onFinishUsing(stack,world,user,lvl);
        }
    }

    @Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable = true)
    private void moonay$getMaxUseTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (MoonayHelper.hasSpecialEnchantment(stack)) {
            cir.setReturnValue(Objects.requireNonNull(MoonayHelper.getSpecialEnchantment(stack)).maxUseTime(stack));
        }
    }

    @Inject(method = "getUseAction", at = @At("HEAD"), cancellable = true)
    private void moonay$getUseAction(ItemStack stack, CallbackInfoReturnable<UseAction> cir) {
        if (MoonayHelper.hasSpecialEnchantment(stack)) {
            cir.setReturnValue(Objects.requireNonNull(MoonayHelper.getSpecialEnchantment(stack)).useAction(stack));
        }
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack stack = user.getStackInHand(hand);
        if (MoonayHelper.hasSpecialEnchantment(stack)) {
            cir.setReturnValue(Objects.requireNonNull(MoonayHelper.getSpecialEnchantment(stack)).onUse(world, user, hand, stack));
        }
    }
}
