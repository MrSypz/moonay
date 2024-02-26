package sypztep.mamy.moonay.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import sypztep.mamy.moonay.common.init.ModConfig;
import sypztep.mamy.moonay.common.init.ModSoundEvents;
import sypztep.mamy.moonay.common.init.ModStatusEffects;
import sypztep.mamy.moonay.common.packetc2s.CarveSoulPacket;
import sypztep.mamy.moonay.common.util.*;

import static sypztep.mamy.moonay.common.util.MoonayHelper.checkIsItemCorrectUse;

public class CarveEnchantmentBehavior extends OnHitApplyEnchantment implements ItemEnchantmentBehavior, EnchantmentSpecialEffect {
    private boolean soundPlayed = false;
    public CarveEnchantmentBehavior(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
        this.setName("carve");
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != Enchantments.SHARPNESS;
    }

    @Override
    public void onFinishUsing(ItemStack stack, World world, LivingEntity user, int level) {
        int amp = MoonayHelper.getStatusAmp(ModStatusEffects.STALWART, user);
        double value = user.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        if (MoonayHelper.stillHaveThisStatusEffect(ModStatusEffects.STALWART, user)) {
            AbilityHelper.boxArea(user, user.getWorld().getDamageSources().playerAttack((PlayerEntity) user),amp, (float) (value * 1.5f),1.0f); //150% Damage base on player attack damage
            user.heal(amp + AbilityHelper.getMissingHealth(user, ModConfig.CONFIG.carvehealratio));
            if (user.getWorld().isClient())
                CarveSoulPacket.send(amp);
            carvesoulParticle(user,amp);
            user.removeStatusEffect(ModStatusEffects.STALWART);
            MoonayHelper.addStatus(user,ModStatusEffects.STALWART_COOLDOWN, 240 - (level * 2));
        }
    }
    public static void carvesoulParticle(Entity entity,int radius) {
        for (int i = 0; i <= 360; i += 8) {
            double circle = Math.toRadians(i);
            double x = radius * 0.2 * Math.cos(circle) * 1.5d;
            double z = radius * 0.2 * Math.sin(circle) * 1.5d;

            double xVec = x * 0.25d;
            double zVec = z * 0.25d;

            entity.getWorld().addParticle(ParticleTypes.SOUL, entity.getX() + x, entity.getEyeY() + z , entity.getZ() + z, xVec,0,zVec);
            entity.getWorld().addParticle(ParticleTypes.SOUL, entity.getX() + x, entity.getEyeY() + (z * -1) , entity.getZ() + z, xVec,0,zVec);
            entity.getWorld().addParticle(ParticleTypes.SOUL, entity.getX() + x, entity.getEyeY() + ((z * 2) * -1) , entity.getZ() + z, xVec,0,zVec);
            entity.getWorld().addParticle(ParticleTypes.SOUL, entity.getX() + x, entity.getEyeY() + ((z * 2)) , entity.getZ() + z, xVec,0,zVec);
        }
        entity.playSound(ModSoundEvents.ITEM_STALWART, 1, (float) (1 + ((LivingEntity) entity).getRandom().nextGaussian() / 20.0));
    }

    @Override
    public UseAction useAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        this.weaponType = checkIsItemCorrectUse(user);
        if (MoonayHelper.hasEnchantment(this, stack)
                && MoonayHelper.dontHaveThisStatusEffect(ModStatusEffects.STALWART_COOLDOWN, user)
                && weaponType == MoonayHelper.WeaponType.SWORD
                && MoonayHelper.stillHaveThisStatusEffect(ModStatusEffects.STALWART,user)) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        } else if (weaponType == MoonayHelper.WeaponType.AXE)
            return TypedActionResult.pass(stack);
        return TypedActionResult.pass(stack);
    }

    @Override
    public void applyOnTarget(LivingEntity user, Entity target, int level) {
        int carvecount = 0;

        if (level != 0 && target.isAttackable() && !target.getWorld().isClient && user.distanceTo(target) <= 6 && target instanceof LivingEntity living) {
            if (living.getArmor() > 0) {
                StatusEffectInstance markInstance = living.getStatusEffect(ModStatusEffects.CARVE);

                if (markInstance != null) {
                    carvecount = Math.min(markInstance.getAmplifier() + 1, level);

                    if (carvecount == level && !soundPlayed) {
                        target.playSound(ModSoundEvents.ITEM_CARVE, 1, (float) (1 + living.getRandom().nextGaussian() / 10.0));
                        soundPlayed = true; // Set the flag to true once the sound is played
                        MoonayHelper.addStatus(living, StatusEffects.SLOWNESS, 40 + level * 4, 0);
                        ((ServerWorld) user.getWorld()).spawnParticles(ParticleTypes.ENCHANTED_HIT, target.getX(), target.getBodyY(0.5D), target.getZ(), 18, 0.3, 0.6, 0.3, 0.01D);
                    }
                } else soundPlayed = false;

                MoonayHelper.addStatus(living, ModStatusEffects.CARVE, 20 + level * 4, carvecount);
                ((ServerWorld) user.getWorld()).spawnParticles(ParticleTypes.SCULK_SOUL, target.getX(), target.getBodyY(0.5D), target.getZ(), 18, 0.3, 0.6, 0.3, 0.01D);
            }
        }
    }

    @Override
    public void applyOnUser(LivingEntity user, int level) {
        if (level != 0) {
            int hitcount = MoonayHelper.getStatusCount(user, ModStatusEffects.STALWART, level);
            MoonayHelper.addStatus(user, ModStatusEffects.STALWART,20 + level * 12, hitcount);
        }
    }
}
