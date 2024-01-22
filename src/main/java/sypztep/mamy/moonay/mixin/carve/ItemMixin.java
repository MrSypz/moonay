package sypztep.mamy.moonay.mixin.carve;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
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
            int lvl = MoonayHelper.getEntLvl(ModEnchantments.CARVE,stack);
            if (MoonayHelper.stillHasThisStatusEffect(ModStatusEffects.STALWART,user)) {
                int j = MoonayHelper.getStatusAmp(ModStatusEffects.STALWART,user);
                user.heal(j);
                for (int phi = 0; phi <= 180; phi += 8) {
                    for (int theta = 0; theta < 360; theta += 8) {
                        double phiRad = Math.toRadians(phi);
                        double thetaRad = Math.toRadians(theta);

                        double radius = j * 0.3;
                        double x = radius * Math.sin(phiRad) * Math.cos(thetaRad) * 1.5;
                        double y = radius * Math.cos(phiRad) * 1.5;
                        double z = radius * Math.sin(phiRad) * Math.sin(thetaRad) * 1.5;

                        double velocityMultiplier = 0.3;
                        double vx = x * velocityMultiplier;
                        double vy = y * velocityMultiplier;
                        double vz = z * velocityMultiplier;
                        user.getWorld().addParticle(
                                ParticleTypes.SOUL,
                                user.getX() + x,
                                user.getY() + y + 0.5,
                                user.getZ() + z,
                                vx, vy, vz
                        );
                    }
                }
                user.removeStatusEffect(ModStatusEffects.STALWART);
                user.addStatusEffect(new StatusEffectInstance(ModStatusEffects.STALWART_COOLDOWN,240 - (lvl * 2)));
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
            cir.setReturnValue(UseAction.SPEAR);
        }
    }

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void use(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        ItemStack stack = user.getStackInHand(hand);
        if (MoonayHelper.hasEnt(ModEnchantments.CARVE, stack) && MoonayHelper.dontHasThisStatusEffect(ModStatusEffects.STALWART_COOLDOWN,user)) {
            user.setCurrentHand(hand);
            cir.setReturnValue(TypedActionResult.consume(stack));
        }
    }
}
