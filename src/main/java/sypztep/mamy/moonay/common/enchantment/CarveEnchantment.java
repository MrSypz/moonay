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
import sypztep.mamy.moonay.common.MoonayMod;
import sypztep.mamy.moonay.common.init.ModEnchantments;
import sypztep.mamy.moonay.common.init.ModSoundEvents;
import sypztep.mamy.moonay.common.init.ModStatusEffects;
import sypztep.mamy.moonay.common.packetc2s.CarveSoulPacket;
import sypztep.mamy.moonay.common.util.*;

import static sypztep.mamy.moonay.common.util.MoonayHelper.checkIsItemCorrectUse;

public class CarveEnchantment extends OnHitApplyEnchantment implements SpecialEnchantment, CustomSpecial {
    private boolean soundPlayed = false;
    public CarveEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
        this.setName("carve");
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != Enchantments.SHARPNESS;
    }

    @Override
    public void onFinishUsing(ItemStack stack, World world, LivingEntity user) {
        int lvl = MoonayHelper.getEntLvl(this, stack);
        int amp = MoonayHelper.getStatusAmp(ModStatusEffects.STALWART, user);
        double value = user.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        if (MoonayHelper.stillHasThisStatusEffect(ModStatusEffects.STALWART, user)) {
            AbilityHelper.boxArea(user, user.getWorld().getDamageSources().playerAttack((PlayerEntity) user),amp, (float) (value * 1.5f),1.0f); //150% Damage base on player attack damage
            user.heal(amp + AbilityHelper.getMissingHealth(user,0.05f));
            if (user.getWorld().isClient())
                CarveSoulPacket.send(amp);
            carvesoulParticle(user,amp);
            user.removeStatusEffect(ModStatusEffects.STALWART);
            MoonayHelper.applyEffect(user,ModStatusEffects.STALWART_COOLDOWN, 240 - (lvl * 2));
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
    public int maxUseTime(ItemStack stack) {
        return 20;
    }

    @Override
    public UseAction useAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        MoonayHelper.WeaponType weaponType = checkIsItemCorrectUse(user, stack);
        if (MoonayHelper.hasEnt(this, stack) && MoonayHelper.dontHasThisStatusEffect(ModStatusEffects.STALWART_COOLDOWN, user) && weaponType == MoonayHelper.WeaponType.SWORD) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        } else if (weaponType == MoonayHelper.WeaponType.AXE)
            return TypedActionResult.pass(stack);
        return TypedActionResult.pass(stack);
    }

    @Override
    public void applyOnTarget(LivingEntity user, Entity target) {
        int carve = MoonayHelper.getEntLvl(ModEnchantments.CARVE, user.getMainHandStack());
        int carvecount = 0;

        if (carve != 0 && target.isAttackable() && !target.getWorld().isClient && user.distanceTo(target) <= 6 && target instanceof LivingEntity living) {
            if (living.getArmor() > 0) {
                StatusEffectInstance markInstance = living.getStatusEffect(ModStatusEffects.CARVE);

                if (markInstance != null) {
                    carvecount = Math.min(markInstance.getAmplifier() + 1, carve);

                    if (carvecount == carve && !soundPlayed) {
                        target.playSound(ModSoundEvents.ITEM_CARVE, 1, (float) (1 + living.getRandom().nextGaussian() / 10.0));
                        soundPlayed = true; // Set the flag to true once the sound is played
                        MoonayHelper.applyEffect(living, StatusEffects.SLOWNESS, 40 + carve * 4, 0);
                        ((ServerWorld) user.getWorld()).spawnParticles(ParticleTypes.ENCHANTED_HIT, target.getX(), target.getBodyY(0.5D), target.getZ(), 18, 0.3, 0.6, 0.3, 0.01D);
                    }
                } else soundPlayed = false;

                MoonayHelper.applyEffect(living, ModStatusEffects.CARVE, 20 + carve * 4, carvecount);
                ((ServerWorld) user.getWorld()).spawnParticles(ParticleTypes.SCULK_SOUL, target.getX(), target.getBodyY(0.5D), target.getZ(), 18, 0.3, 0.6, 0.3, 0.01D);
            }
        }
    }

    @Override
    public void applyOnUser(LivingEntity user) {
        int carvelvl = MoonayHelper.getEntLvl(ModEnchantments.CARVE, user.getMainHandStack());

        if (carvelvl != 0) {
            int carvecount = MoonayHelper.getStatusCount(user, ModStatusEffects.STALWART, carvelvl);
            user.addStatusEffect(new StatusEffectInstance(ModStatusEffects.STALWART, 20 + carvelvl * 12, carvecount));
        }
    }
}
