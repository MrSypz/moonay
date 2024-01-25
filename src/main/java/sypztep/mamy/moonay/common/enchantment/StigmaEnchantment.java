package sypztep.mamy.moonay.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import sypztep.mamy.moonay.common.init.ModEnchantments;
import sypztep.mamy.moonay.common.init.ModParticles;
import sypztep.mamy.moonay.common.init.ModStatusEffects;
import sypztep.mamy.moonay.common.packetc2s.StigmaPacket;
import sypztep.mamy.moonay.common.util.AbilityHelper;
import sypztep.mamy.moonay.common.util.MoonayHelper;
import sypztep.mamy.moonay.common.util.SpecialEnchantment;

import static sypztep.mamy.moonay.common.util.MoonayHelper.checkIsItemCorrectUse;

public class StigmaEnchantment extends AxeEnchantment implements SpecialEnchantment {
    public StigmaEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
        this.setName("stigma");
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != ModEnchantments.CARVE;
    }

    @Override
    public int getMaxLevel() {
        return 5;
    }

    @Override
    public void onFinishUsing(ItemStack stack, World world, LivingEntity living) {
        int lvl = MoonayHelper.getEntLvl(this, stack);
        double value = living.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        AbilityHelper.boxDamage(living,living.getWorld().getDamageSources().playerAttack((PlayerEntity) living),3, (float) value * 1.5f); //150% Damage base on player attack damage
        if (living.getWorld().isClient())
            StigmaPacket.send();
        stigmaParticle(living);
        living.heal(((float) value * 0.25f + AbilityHelper.getMissingHealth(living,0.12f)) * AbilityHelper.getHitAmount());
        living.addStatusEffect(new StatusEffectInstance(ModStatusEffects.STIGMA_COOLDOWN, 600 - (lvl * 20)));
    }
    public static void stigmaParticle(Entity entity) {
        entity.getWorld().addParticle(ModParticles.BLOODWAVE, entity.getX(), entity.getY() + 0.1, entity.getZ(), 0,0,0);
    }

    @Override
    public int maxUseTime(ItemStack stack) {
        return 20;
    }

    @Override
    public UseAction useAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        MoonayHelper.WeaponType weaponType = checkIsItemCorrectUse(user, stack);
        if (MoonayHelper.hasEnt(this, stack) && MoonayHelper.dontHasThisStatusEffect(ModStatusEffects.STIGMA_COOLDOWN, user) && weaponType == MoonayHelper.WeaponType.AXE) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        } else if (weaponType == MoonayHelper.WeaponType.SWORD)
            return TypedActionResult.pass(stack);
        return TypedActionResult.pass(stack);
    }
}
