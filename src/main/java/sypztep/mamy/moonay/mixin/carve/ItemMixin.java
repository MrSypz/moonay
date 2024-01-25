package sypztep.mamy.moonay.mixin.carve;

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

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "finishUsing", at = @At("HEAD"))
    private void finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (MoonayHelper.hasSpecialEnchantment(stack)) {
            MoonayHelper.getSpecialEnchantment(stack).onFinishUsing(stack,world,user);
        }
    }

    @Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable = true)
    private void getMaxUseTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (MoonayHelper.hasSpecialEnchantment(stack)) {
            cir.setReturnValue(MoonayHelper.getSpecialEnchantment(stack).maxUseTime(stack));
        }
    }

    @Inject(method = "getUseAction", at = @At("HEAD"), cancellable = true)
    private void getUseAction(ItemStack stack, CallbackInfoReturnable<UseAction> cir) {
        if (MoonayHelper.hasSpecialEnchantment(stack)) {
            cir.setReturnValue(MoonayHelper.getSpecialEnchantment(stack).useAction(stack));
        }
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack stack = user.getStackInHand(hand);
        if (MoonayHelper.hasSpecialEnchantment(stack)) {
            cir.setReturnValue(MoonayHelper.getSpecialEnchantment(stack).onUse(world, user, hand, stack));
        }
    }
}
