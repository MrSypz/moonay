package sypztep.mamy.moonay.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import sypztep.mamy.moonay.common.init.ModSoundEvents;
import sypztep.mamy.moonay.common.init.ModStatusEffects;
import sypztep.mamy.moonay.common.util.DamageHandler;
import sypztep.mamy.moonay.common.util.MoonayHelper;

import static sypztep.mamy.moonay.common.util.MoonayHelper.checkIsItemCorrectUse;

public class PraminaxEnchantment extends OnHitApplyEnchantment implements DamageHandler {
    private static boolean shouldTriggerAdditionalDamage = false;
    public PraminaxEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
        this.setName("praminax");
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }
    @Override
    public void applyOnTarget(LivingEntity user, Entity target, int level) {
        if (MoonayHelper.hasEnchantWithRangeDistance(this, user, target, 6)) {
            return;
        }
        if (target instanceof LivingEntity living && this.weaponType == MoonayHelper.WeaponType.SWORD) {
            StatusEffectInstance instance = user.getStatusEffect(ModStatusEffects.PRAMINAX);
            if (instance != null && instance.getAmplifier() == 2) {
                if (!living.equals(user)) {
                    target.getWorld().playSound(null,target.getX(),target.getY(),target.getZ(),ModSoundEvents.ITEM_PRAMINAX, SoundCategory.PLAYERS,1,1);
                    setShouldTriggerAdditionalDamage(true);
                    user.removeStatusEffect(ModStatusEffects.PRAMINAX);
                    praminaxParticle(user, target);
                }
            }
        }
    }
    private void praminaxParticle(LivingEntity user, Entity target) {
        if (user.getWorld() instanceof ServerWorld) {
            double startX = target.getX();
            double startY = target.getY() + 1;
            double startZ = target.getZ();

            double endX = user.getX();
            double endY = user.getEyeY();
            double endZ = user.getZ();

            int particleNumConstant = 50;

            double xdif = (endX - startX) / particleNumConstant;
            double ydif = (endY - startY) / particleNumConstant;
            double zdif = (endZ - startZ) / particleNumConstant;

            for (int i = 0; i <= particleNumConstant; i++) {
                double t = (double) i / particleNumConstant;

                double x = startX + xdif * i + 1 * Math.sin(2 * Math.PI * t);
                double y = startY + ydif * i + 2.5 * Math.sin(2 * Math.PI * t);
                double z = startZ + zdif * i * Math.cos(4* Math.PI * t);

                ((ServerWorld) user.getWorld()).spawnParticles(ParticleTypes.REVERSE_PORTAL, x, y, z, 0, 0, 0, 0, 1);
            }
        }
    }

    @Override
    public void applyOnUser(LivingEntity user,int level) {
        int praminaxcount = MoonayHelper.getStatusCount(user, ModStatusEffects.PRAMINAX, 1);
        if (praminaxcount < 3)
            MoonayHelper.addStatus(user, ModStatusEffects.PRAMINAX, 40 + level * 20, praminaxcount);
    }
    @Override
    public void setShouldTriggerAdditionalDamage(boolean value) {
        shouldTriggerAdditionalDamage = value;
    }

    @Override
    public boolean isShouldTriggerAdditionalDamage() {
        return shouldTriggerAdditionalDamage;
    }
}
