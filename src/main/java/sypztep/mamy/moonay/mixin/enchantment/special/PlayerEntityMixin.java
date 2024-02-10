package sypztep.mamy.moonay.mixin.enchantment.special;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.moonay.common.init.ModEnchantments;
import sypztep.mamy.moonay.common.util.EnchantmentSpecialEffect;
import sypztep.mamy.moonay.common.util.DamageHandler;
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
            EnchantmentSpecialEffect enchantmentSpecialEffect = MoonayHelper.getCustomSpecial(mainHandStack);
            assert enchantmentSpecialEffect != null;
            enchantmentSpecialEffect.applyOnUser(this, lvl);
            enchantmentSpecialEffect.applyOnTarget(this,target,lvl);
        }
    }
    @ModifyVariable(method = "attack", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getAttackDamage(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityGroup;)F"), ordinal = 1)
    private float moonay$modifyattackdmg(float baseDamage, Entity target) {
        ItemStack mainHandStack = this.getMainHandStack();
        DamageHandler handler = MoonayHelper.getDamageHandler(mainHandStack);
        float attackDamage = (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        int praminaxLevel = MoonayHelper.getEnchantmentLvl(ModEnchantments.PRAMINAX, mainHandStack);

        if (!MoonayHelper.hasDamageHandler(mainHandStack)) {
            return baseDamage;
        }

        if (handler != null && handler.isShouldTriggerAdditionalDamage()) {
            if (MoonayHelper.hasEnchantment(ModEnchantments.PRAMINAX, mainHandStack) && target instanceof LivingEntity) {
                handler.setShouldTriggerAdditionalDamage(false);
                return baseDamage + (attackDamage * (0.2f * praminaxLevel));
            }

            if (MoonayHelper.hasEnchantment(ModEnchantments.STIGMA, mainHandStack) && target instanceof LivingEntity) {
                handler.setShouldTriggerAdditionalDamage(false);
                return baseDamage + ((LivingEntity) target).getMaxHealth() * 10f;
            }
        }

        return baseDamage;
    }
}
