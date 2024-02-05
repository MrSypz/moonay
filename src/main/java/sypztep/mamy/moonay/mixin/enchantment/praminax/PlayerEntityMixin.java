package sypztep.mamy.moonay.mixin.enchantment.praminax;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.mamy.moonay.common.enchantment.PraminaxEnchantment;
import sypztep.mamy.moonay.common.init.ModEnchantments;
import sypztep.mamy.moonay.common.util.MoonayHelper;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {
    private PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }
    @ModifyVariable(method = "attack", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getAttackDamage(Lnet/minecraft/item/ItemStack;Lnet/minecraft/entity/EntityGroup;)F"), ordinal = 1)
    private float getAttackDamage(float baseDamage, Entity target) {
        int level = MoonayHelper.getEnchantmentLvl(ModEnchantments.PRAMINAX, this.getMainHandStack());
        float damage = (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE) * (0.2f * level);

        if (PraminaxEnchantment.isShouldTriggerAdditionalDamage()) {
            PraminaxEnchantment.setAdditionalDamageTrigger(false);
            return baseDamage + damage;
        }
        return baseDamage;
    }
}
