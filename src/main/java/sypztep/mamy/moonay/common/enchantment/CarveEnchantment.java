package sypztep.mamy.moonay.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import sypztep.mamy.moonay.common.init.ModStatusEffects;
import sypztep.mamy.moonay.common.packetc2s.CarveSoulPacket;
import sypztep.mamy.moonay.common.util.AbilityHelper;
import sypztep.mamy.moonay.common.util.MoonayHelper;
import sypztep.mamy.moonay.common.util.SpecialEnchantment;

import static sypztep.mamy.moonay.common.util.MoonayHelper.checkIsItemCorrectUse;

public class CarveEnchantment extends OnHitApplyEnchantment implements SpecialEnchantment {
    public CarveEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
        this.setName("carve");
    }

    @Override
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        //Move to carve LivingEntityMixin
    }
    @Override
    protected boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != Enchantments.SHARPNESS;
    }

    @Override
    public void onFinishUsing(ItemStack stack, World world, LivingEntity living) {
        int lvl = MoonayHelper.getEntLvl(this, stack);
        if (MoonayHelper.stillHasThisStatusEffect(ModStatusEffects.STALWART, living)) {
            int j = MoonayHelper.getStatusAmp(ModStatusEffects.STALWART, living);
            living.heal(j + AbilityHelper.getMissingHealth(living,0.05f));
            if (living.getWorld().isClient())
                CarveSoulPacket.send();
            carvesoulParticle(living);
            living.removeStatusEffect(ModStatusEffects.STALWART);
            living.addStatusEffect(new StatusEffectInstance(ModStatusEffects.STALWART_COOLDOWN, 240 - (lvl * 2)));
        }
    }
    public static void carvesoulParticle(Entity entity) {
        for (int i = 0; i <= 360; i += 8) {
            double circle = Math.toRadians(i);
            double x = 3 * Math.cos(circle) * 1.5;
            double z = 3* Math.sin(circle) * 1.5;
            entity.getWorld().addParticle(ParticleTypes.COMPOSTER, entity.getX() + x, entity.getEyeY(), entity.getZ() + z, 0,0,0);
        }
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
}
