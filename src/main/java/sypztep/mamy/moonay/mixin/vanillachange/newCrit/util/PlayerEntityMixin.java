package sypztep.mamy.moonay.mixin.vanillachange.newCrit.util;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.mamy.moonay.common.init.ModConfig;
import sypztep.mamy.moonay.common.init.ModEntityAttributes;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(method = "attack", at = @At("STORE"), ordinal = 2)
    private boolean docrit(boolean crit) {
        if (ModConfig.CONFIG.newCritOverhaul) {
            boolean iscrit = this.moonay$isCritical();
            if (iscrit)
                crit = true;
             else if (crit)
                this.moonay$setCritical(true);
            return crit;
        }
        return crit;
    }
    /**
     * Is add player entity this attribute
     */
    @Inject(method = "createPlayerAttributes",at = @At("RETURN"))
    private static void initAttributes(CallbackInfoReturnable<DefaultAttributeContainer.Builder> ci) {
        ci.getReturnValue().add(ModEntityAttributes.GENERIC_CRIT_CHANCE);
        ci.getReturnValue().add(ModEntityAttributes.GENERIC_CRIT_DAMAGE);
    }
}
