package sypztep.mamy.moonay.mixin.vanillachange.newCrit;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.moonay.common.init.ModConfig;
import sypztep.mamy.moonay.common.util.NewCriticalOverhaul;

import java.util.Random;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements NewCriticalOverhaul {
    @Unique
    private static final TrackedData<Float> CRIT_RATE;
    @Unique
    private static final TrackedData<Float> CRIT_DMG;
    @Unique
    private final Random critRateRandom = new Random();

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    @Shadow @Nullable public abstract EntityAttributeInstance getAttributeInstance(EntityAttribute attribute);

    protected LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }


    @Inject(method = {"initDataTracker"},at = {@At("TAIL")})
    protected void initDataTracker(CallbackInfo ci) {
        this.dataTracker.startTracking(CRIT_RATE, 0.0F); //Start With 0% that was default vanilla
        this.dataTracker.startTracking(CRIT_DMG, 50.0F); //Start With 50% that was default vanilla
    }

    @Inject(method = {"writeCustomDataToNbt"},at = {@At("TAIL")})
    private void write(NbtCompound nbt, CallbackInfo ci) {
        nbt.putFloat("CritRate", this.moonay$getCritRate());
        nbt.putFloat("CritDamage", this.moonay$getCritDamage());
    }

    @Inject(method = {"readCustomDataFromNbt"},at = {@At("TAIL")})
    private void read(NbtCompound nbt, CallbackInfo ci) {
        if (nbt.contains("CritRate"))
            this.moonay$setCritRate(nbt.getFloat("CritRate"));
        if (nbt.contains("CritDamage"))
            this.moonay$setCritDamage(nbt.getFloat("CritDamage"));
    }
    /**
     * Modifies the damage amount before applying it, considering the new crit overhaul configuration.
     *
     * @param amount The original damage amount.
     * @param source The source of the damage.
     * @return The modified damage amount.
     */
    @ModifyVariable(method = "applyDamage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float applyDamageFirst(float amount, DamageSource source) {
        if (ModConfig.CONFIG.shouldDoCrit() && !this.getWorld().isClient()) {
            Entity attacker = source.getAttacker();

            if (attacker instanceof NewCriticalOverhaul invoker) {
                Entity projectileSource = source.getSource();

                if (projectileSource instanceof PersistentProjectileEntity projectile) {
                    invoker.storeCrit().moonay$setCritical(projectile.isCritical());
                    amount = invoker.calculateCritDamage(amount);
                    return amount;
                }
            }

            if (!(source.getAttacker() instanceof PlayerEntity)) {
                if (attacker instanceof NewCriticalOverhaul invoker) {
                    amount = invoker.calculateCritDamage(amount);
                    return amount;
                }
            }
        }
        return amount;
    }

    public Random moonay$getRand() {
        return this.critRateRandom;
    }
    public void moonay$setCritRate(float critRate) {
        this.dataTracker.set(CRIT_RATE, critRate);
    }

    public void moonay$setCritDamage(float critDamage) {
        this.dataTracker.set(CRIT_DMG, critDamage);
    }

    public float moonay$getCritRate() {
        return this.dataTracker.get(CRIT_RATE);
    }
    public float moonay$getCritDamage() {
        return this.dataTracker.get(CRIT_DMG);
    }

    static {
        CRIT_RATE = DataTracker.registerData(LivingEntityMixin.class, TrackedDataHandlerRegistry.FLOAT);
        CRIT_DMG = DataTracker.registerData(LivingEntityMixin.class, TrackedDataHandlerRegistry.FLOAT);
    }
}
