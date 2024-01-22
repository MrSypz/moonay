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
import sypztep.mamy.moonay.common.init.ModEnchantments;
import sypztep.mamy.moonay.common.init.ModStatusEffects;
import sypztep.mamy.moonay.common.util.MoonayHelper;

@Mixin(Item.class)
public class ItemMixin
    {@Inject(method = "finishUsing", at = @At("HEAD"))
    private void finishUsing(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if (MoonayHelper.hasEnt(ModEnchantments.CARVE, stack)) {
            if (MoonayHelper.stillHasStatus(ModStatusEffects.STALWART,user)) {
                user.heal(MoonayHelper.getStatusAmp(ModStatusEffects.STALWART,user));
                user.removeStatusEffect(ModStatusEffects.STALWART);
            }
        }
    }

    @Inject(method = "getMaxUseTime", at = @At("HEAD"), cancellable = true)
    private void getMaxUseTime(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (MoonayHelper.hasEnt(ModEnchantments.CARVE, stack)) {
            cir.setReturnValue(20);
        }
    }

    @Inject(method = "getUseAction", at = @At("HEAD"), cancellable = true)
    private void getUseAction(ItemStack stack, CallbackInfoReturnable<UseAction> cir) {
        if (MoonayHelper.hasEnt(ModEnchantments.CARVE, stack)) {
            cir.setReturnValue(UseAction.CROSSBOW);
        }
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack stack = user.getStackInHand(hand);
        if (MoonayHelper.hasEnt(ModEnchantments.CARVE, stack)) {
            user.setCurrentHand(hand);
            cir.setReturnValue(TypedActionResult.consume(stack));
        }
    }

}
