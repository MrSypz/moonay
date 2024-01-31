package sypztep.mamy.moonay.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sypztep.mamy.moonay.common.init.ModConfig;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin {
    @Unique
    private float attackCooldown = 0;
    @ModifyVariable(method = "attack", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getFireAspect(Lnet/minecraft/entity/LivingEntity;)I"), ordinal = 1)
    private int CooldownRequirement(int value) {
        if (attackCooldown < ModConfig.CONFIG.weaponCooldownProgess) {
            return 0;
        }
        return value;
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;resetLastAttackedTicks()V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void CooldownRequirement(Entity target, CallbackInfo ci, float attackDamage, float extraDamage, float attackCooldown) {
        this.attackCooldown = attackCooldown;
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;onTargetDamaged(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/Entity;)V"))
    private void CooldownRequirementOnTargetDamaged(Entity target, CallbackInfo ci) {
        if (attackCooldown < ModConfig.CONFIG.weaponCooldownProgess) {
            ModConfig.CONFIG.shouldcancelattack = true;
        }
    }

    @Inject(method = "attack", at = @At("TAIL"))
    private void CooldownRequirement(Entity target, CallbackInfo ci) {
        attackCooldown = 0;
    }
}
