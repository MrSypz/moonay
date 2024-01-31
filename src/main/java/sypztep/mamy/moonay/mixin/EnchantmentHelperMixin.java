package sypztep.mamy.moonay.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.moonay.common.init.ModConfig;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {
    @Inject(method = "onTargetDamaged", at = @At("HEAD"), cancellable = true)
    private static void CooldownRequirement(LivingEntity user, Entity target, CallbackInfo ci) {
        if (ModConfig.CONFIG.shouldcancelattack) {
            ModConfig.CONFIG.shouldcancelattack = false;
            ci.cancel();
        }
    }
}
