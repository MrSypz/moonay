package sypztep.mamy.moonay.mixin.carve;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.moonay.common.init.ModEnchantments;
import sypztep.mamy.moonay.common.init.ModSoundEvents;
import sypztep.mamy.moonay.common.init.ModStatusEffects;
import sypztep.mamy.moonay.common.util.MoonayHelper;

@Mixin(PlayerEntity.class)

public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    @Inject(at = @At("HEAD"), method = "attack")
    private void attack(Entity target, CallbackInfo ci) {
        int carve = MoonayHelper.getEntLvl(ModEnchantments.CARVE, this.getMainHandStack());
        if(carve != 0 && target.isAttackable() && !target.getWorld().isClient && this.distanceTo(target) <= 6) {
            if (!target.handleAttack((PlayerEntity) (Object) this) && target instanceof LivingEntity living && !((PlayerEntity) (Object) this).handSwinging) {
                int carvecount = 0;
                if (living.getArmor() > 0) {
                    StatusEffectInstance markInstance = ((LivingEntity) target).getStatusEffect(ModStatusEffects.CARVE);
                    if (markInstance != null) {
                        carvecount = markInstance.getAmplifier();
                        if (carvecount < carve) {
                            carvecount++;
                            if (carvecount == carve)
                                target.playSound(ModSoundEvents.ITEM_CARVE, 1, (float) (1 + ((LivingEntity) target).getRandom().nextGaussian() / 10.0));
                        }
                    }
                    ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(ModStatusEffects.CARVE, 20 + carve * 4, carvecount));
                    ((ServerWorld) ((PlayerEntity) (Object) this).getWorld()).spawnParticles(ParticleTypes.SCULK_SOUL, target.getX(), target.getBodyY(0.5D), target.getZ(), 18, 0.3, 0.6, 0.3, 0.01D);
                }
            }
        }
    }
}
