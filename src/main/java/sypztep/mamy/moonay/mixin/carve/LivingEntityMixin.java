package sypztep.mamy.moonay.mixin.carve;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.moonay.common.init.ModEnchantments;
import sypztep.mamy.moonay.common.init.ModStatusEffects;
import sypztep.mamy.moonay.common.util.MoonayHelper;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Shadow public abstract ItemStack getMainHandStack();

    @Shadow public abstract @Nullable StatusEffectInstance getStatusEffect(StatusEffect effect);

    @Shadow public boolean handSwinging;

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    @Inject(at = @At("HEAD"), method = "onAttacking")
    public void onAttacking(Entity target, CallbackInfo ci) {
        int carvelvl = MoonayHelper.getEntLvl(ModEnchantments.CARVE, this.getMainHandStack());
        LivingEntity living = LivingEntity.class.cast(this);
        if (carvelvl != 0) {
            int carvecount = MoonayHelper.getStatusCount(living, ModStatusEffects.STALWART, carvelvl);
            this.addStatusEffect(new StatusEffectInstance(ModStatusEffects.STALWART, 20 + carvelvl * 12, carvecount));
        }
    }
}
