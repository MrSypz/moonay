package sypztep.mamy.moonay.common.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import sypztep.mamy.moonay.common.entity.projectile.NeedleEntity;
import sypztep.mamy.moonay.common.init.ModStatusEffects;
import sypztep.mamy.moonay.common.util.MoonayHelper;

import static sypztep.mamy.moonay.common.util.MoonayHelper.checkIsItemCorrectUse;

public class ThousandNeedleEnchantment extends OnHitApplyEnchantment {
    public ThousandNeedleEnchantment(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
        this.name = "thousandneedle";
    }
    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof HoeItem;
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public UseAction useAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public void applyOnUser(LivingEntity user, int level) {
        if (level != 0) {
            int hitcount = MoonayHelper.getStatusCount(user, ModStatusEffects.THOUSANDNEEDLE, level);
            MoonayHelper.addStatus(user,ModStatusEffects.THOUSANDNEEDLE, 30 + level * 12, hitcount);
        }
    }

    @Override
    public void onFinishUsing(ItemStack stack, World world, LivingEntity user, int level) {
        float amount = 6.0f;
        int amp = MoonayHelper.getStatusCount(user, ModStatusEffects.THOUSANDNEEDLE, level);
        if (amp <= 0)
            return;

        for (int i = 0; i < Math.pow(3, amp); i++) {
            NeedleEntity needleEntity = new NeedleEntity(world, user, amount);
            needleEntity.setOwner(user);
            needleEntity.setPosition(user.getX(), user.getEyeY() + 2, user.getZ());

            double x = -Math.sin(Math.toRadians(user.getYaw())) * Math.cos(Math.toRadians(user.getPitch()));
            double y = -Math.sin(Math.toRadians(user.getPitch() + 15));
            double z = Math.cos(Math.toRadians(user.getYaw())) * Math.cos(Math.toRadians(user.getPitch()));

            double length = Math.sqrt(x * x + y * y + z * z);
            x /= length;
            y /= length;
            z /= length;

            needleEntity.setVelocity(new Vec3d(x + user.getRandom().nextGaussian(), y, z + user.getRandom().nextGaussian() / 2).multiply(level * 0.9f));
            user.getWorld().spawnEntity(needleEntity);
        }
    }

    @Override
    public TypedActionResult<ItemStack> onUse(World world, PlayerEntity user, Hand hand, ItemStack stack) {
        int amp = MoonayHelper.getStatusCount(user, ModStatusEffects.THOUSANDNEEDLE, level);
        this.weaponType = checkIsItemCorrectUse(user);
        if (MoonayHelper.hasEnchantment(this, stack) && this.weaponType == MoonayHelper.WeaponType.HOE && amp > 0) {
            user.setCurrentHand(hand);
            return TypedActionResult.consume(stack);
        } else if (this.weaponType != MoonayHelper.WeaponType.HOE)
            return TypedActionResult.pass(stack);
        return TypedActionResult.pass(stack);
    }

    @Override
    public int maxUseTime(ItemStack stack) {
        return 10;
    }
}
