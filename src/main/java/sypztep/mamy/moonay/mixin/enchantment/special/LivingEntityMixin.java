package sypztep.mamy.moonay.mixin.enchantment.special;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import sypztep.mamy.moonay.common.entity.projectile.StygiaEntity;
import sypztep.mamy.moonay.common.init.ModEnchantments;
import sypztep.mamy.moonay.common.init.ModStatusEffects;
import sypztep.mamy.moonay.common.util.AbilityHelper;
import sypztep.mamy.moonay.common.util.MoonayHelper;

import java.util.Map;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract void setHealth(float health);

    @Shadow public abstract float getHealth();

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    @Inject(method = "getEquipmentChanges", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void mamy$LivingEntityOnEquipmentChange(CallbackInfoReturnable<Map<EquipmentSlot, ItemStack>> cir, Map<EquipmentSlot, ItemStack> changes, EquipmentSlot[] slots, int slotsSize, int slotIndex, EquipmentSlot equipmentSlot, ItemStack previousStack, ItemStack currentStack) {
        MoonayHelper.onEquipmentChange((LivingEntity) (Object) this, equipmentSlot, previousStack, currentStack);
    }

    @Inject(at = @At("HEAD"),method = "damage")
    public void moonay$damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (!MoonayHelper.hasEnchantment(ModEnchantments.STYGIA,this))
            return;
        if ((AbilityHelper.targetMissingHealthPercentBelow(this, 0.3f) || fetalDamage(amount)) && MoonayHelper.dontHaveThisStatusEffect(ModStatusEffects.STYGIA_COOLDOWN, this)) {
            this.setHealth(Math.max(1,amount - 3.5f));
            useStygia();
        }
    }
    @Unique
    private boolean fetalDamage(float amount) {
        return amount >= this.getHealth();
    }
    @Unique
    private void useStygia() {
        int i = Math.max(1,Math.min(5,AbilityHelper.getEntityByArea(this,10)));

        World world = this.getWorld();

        StygiaEntity stygiaEntity = new StygiaEntity(world,this);
        stygiaEntity.setPosition(this.getX(), this.getY(), this.getZ());
        world.spawnEntity(stygiaEntity);

        MoonayHelper.addStatus(this, StatusEffects.REGENERATION, 100 + i * 20, i);
        MoonayHelper.addStatus(this, StatusEffects.ABSORPTION, 100  + i * 20, i);
        MoonayHelper.addStatus(this, StatusEffects.NAUSEA, 160  + i * 20, i);
        MoonayHelper.addStatus(this, StatusEffects.DARKNESS, 100  + i * 20, i);
        MoonayHelper.addStatus(this, StatusEffects.BLINDNESS, 40, i);
        MoonayHelper.addStatus(this, StatusEffects.SPEED, 100  + i * 20, i);
        MoonayHelper.addStatus(this, ModStatusEffects.STYGIA_COOLDOWN, 1200 + i * 100, 0);
    }
    @Shadow
    protected void initDataTracker() {
    }

    @Shadow
    protected void readCustomDataFromNbt(NbtCompound nbt) {
    }

    @Shadow
    protected void writeCustomDataToNbt(NbtCompound nbt) {
    }
}
