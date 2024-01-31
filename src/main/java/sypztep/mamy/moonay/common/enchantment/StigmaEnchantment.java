package sypztep.mamy.moonay.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import sypztep.mamy.moonay.common.init.ModEnchantments;
import sypztep.mamy.moonay.common.init.ModParticles;
import sypztep.mamy.moonay.common.init.ModSoundEvents;
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
    public void onTargetDamaged(LivingEntity user, Entity target, int level) {
        if (!target.getWorld().isClient()) {
            if (target instanceof LivingEntity livingTarget) {
                double value = user.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
                float missingHealthPercentage = (float) (0.014 * level);
                if (livingTarget.getHealth() < livingTarget.getMaxHealth() * missingHealthPercentage) {
                    target.timeUntilRegen = 0;
                    target.damage(target.getWorld().getDamageSources().playerAttack((PlayerEntity) user), (float) (value * 1.25f));
                }
            }
        }
    }

    @Override
    public void onFinishUsing(ItemStack stack, World world, LivingEntity user) {
        int lvl = MoonayHelper.getEntLvl(this, stack);
        double value = user.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        AbilityHelper.boxArea(user, user.getWorld().getDamageSources().playerAttack((PlayerEntity) user),3, (float) value * 1.5f,1.0f); //150% Damage base on player attack damage
        if (user.getWorld().isClient())
            StigmaPacket.send();
        stigmaParticle(user);
        user.heal(((float) value * 0.25f + AbilityHelper.getMissingHealth(user,0.12f)) * AbilityHelper.getHitAmount());
        MoonayHelper.applyEffect(user,ModStatusEffects.STIGMA_COOLDOWN, 600 - (lvl * 20));
    }
    public static void stigmaParticle(Entity entity) {
        entity.getWorld().addParticle(ModParticles.BLOODWAVE, entity.getX(), entity.getY() + 0.1, entity.getZ(), 0,0,0);
        entity.getWorld().playSound(null,entity.getBlockPos(), ModSoundEvents.ITEM_STIGMA, SoundCategory.PLAYERS,1,1f);
    }

    @Override
    public int maxUseTime(ItemStack stack) {
        return 10;
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
