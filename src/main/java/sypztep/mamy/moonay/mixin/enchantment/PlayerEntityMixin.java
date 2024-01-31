package sypztep.mamy.moonay.mixin.enchantment;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.moonay.common.util.CustomSpecial;
import sypztep.mamy.moonay.common.util.MoonayHelper;

@Mixin(PlayerEntity.class)

public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    @Inject(at = @At("HEAD"), method = "attack")
    private void attack(Entity target, CallbackInfo ci) {
        if (MoonayHelper.hasCustomSpecial(this.getMainHandStack()) && !target.handleAttack(this) && !(this).handSwinging) {
            CustomSpecial customSpecial = MoonayHelper.getCustomSpecial(this.getMainHandStack());
            assert customSpecial != null;
            customSpecial.applyOnUser(this);
            customSpecial.applyOnTarget(this,target);
        }
    }
}
