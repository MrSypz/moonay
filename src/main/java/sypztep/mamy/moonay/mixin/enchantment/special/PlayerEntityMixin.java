package sypztep.mamy.moonay.mixin.enchantment.special;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.moonay.common.util.CustomSpecial;
import sypztep.mamy.moonay.common.util.MoonayHelper;

import java.util.Objects;

@Mixin(PlayerEntity.class)

public abstract class PlayerEntityMixin extends LivingEntity {
    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    @Inject(at = @At("HEAD"), method = "attack")
    private void attack(Entity target, CallbackInfo ci) {
        ItemStack mainHandStack = this.getMainHandStack();

        if (MoonayHelper.hasCustomSpecial(this.getMainHandStack()) && !target.handleAttack(this) && !(this).handSwinging) {
            int lvl = MoonayHelper.getEnchantmentLvl(Objects.requireNonNull(MoonayHelper.getCustomSpecial(mainHandStack)).getEnchantment(), mainHandStack);
            CustomSpecial customSpecial = MoonayHelper.getCustomSpecial(mainHandStack);
            assert customSpecial != null;
            customSpecial.applyOnUser(this, lvl);
            customSpecial.applyOnTarget(this,target,lvl);
        }
    }
}
