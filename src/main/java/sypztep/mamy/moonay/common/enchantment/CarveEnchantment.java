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
import net.minecraft.item.SwordItem;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.world.World;
import sypztep.mamy.moonay.common.MoonayMod;
import sypztep.mamy.moonay.common.init.ModEnchantments;
import sypztep.mamy.moonay.common.init.ModStatusEffects;
import sypztep.mamy.moonay.common.packetc2s.CarveSoulPacket;
import sypztep.mamy.moonay.common.util.MoonayHelper;
import sypztep.mamy.moonay.common.util.SpecialEnchantment;

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
        return super.canAccept(other) && !canAccept(Enchantments.SHARPNESS);
    }

    @Override
    public void onFinishUsing(ItemStack stack, World world, LivingEntity user) {
        System.out.println("onFinishUsing called");
        int lvl = MoonayHelper.getEntLvl(this, stack);
        if (MoonayHelper.stillHasThisStatusEffect(ModStatusEffects.STALWART, user)) {
            int j = MoonayHelper.getStatusAmp(ModStatusEffects.STALWART, user);
            user.heal(j + ((user.getHealth() - user.getMaxHealth()) * 0.5f));
            if (user.getWorld().isClient()) {
                CarveSoulPacket.send();
            }
            MoonayHelper.carvesoulParticle(user);
            user.removeStatusEffect(ModStatusEffects.STALWART);
            user.addStatusEffect(new StatusEffectInstance(ModStatusEffects.STALWART_COOLDOWN, 240 - (lvl * 2)));
        }
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 20;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }

    @Override
    public TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        if (MoonayHelper.hasEnt(ModEnchantments.CARVE, stack) && MoonayHelper.dontHasThisStatusEffect(ModStatusEffects.STALWART_COOLDOWN, user)) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        }
        return TypedActionResult.pass(stack);
    }
}
