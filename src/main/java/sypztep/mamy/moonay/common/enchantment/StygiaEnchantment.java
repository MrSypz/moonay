package sypztep.mamy.moonay.common.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.world.World;
import sypztep.mamy.moonay.common.entity.projectile.StygiaEntity;
import sypztep.mamy.moonay.common.init.ModStatusEffects;
import sypztep.mamy.moonay.common.util.AbilityHelper;
import sypztep.mamy.moonay.common.util.MoonayHelper;

public class StygiaEnchantment extends EmptyEnchantment {
    @Override
    public int getMaxLevel() {
        return 1;
    }

    public StygiaEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
        this.setName("stygia");
    }

    @Override
    public void onUserDamaged(LivingEntity user, Entity attacker, int level) {
        if (!MoonayHelper.hasEnchantment(this, user) || user.isDead()) {
            return;
        }

        if (!AbilityHelper.targetMissingHealthPercentBelow(user, 0.3f) || !MoonayHelper.dontHaveThisStatusEffect(ModStatusEffects.STYGIA_COOLDOWN, user)) {
            return;
        }
        World world = user.getWorld();

        // Spawn your custom entity
        StygiaEntity stygiaEntity = new StygiaEntity(world,user);
        stygiaEntity.setPosition(user.getX(), user.getY(), user.getZ());
        world.spawnEntity(stygiaEntity);

        MoonayHelper.addStatus(user, StatusEffects.REGENERATION, 100, 3);
        MoonayHelper.addStatus(user, StatusEffects.ABSORPTION, 100, 3);
        MoonayHelper.addStatus(user, StatusEffects.NAUSEA, 160, 3);
        MoonayHelper.addStatus(user, StatusEffects.DARKNESS, 100, 3);
        MoonayHelper.addStatus(user, StatusEffects.BLINDNESS, 40, 3);
        MoonayHelper.addStatus(user, StatusEffects.SPEED, 100, 1);
        MoonayHelper.addStatus(user, ModStatusEffects.STYGIA_COOLDOWN, 1800, 0);
    }
}
