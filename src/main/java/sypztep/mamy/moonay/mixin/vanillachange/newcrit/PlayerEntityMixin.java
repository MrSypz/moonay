package sypztep.mamy.moonay.mixin.vanillachange.newcrit;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.mamy.moonay.common.init.ModConfig;
import sypztep.mamy.moonay.common.init.ModEntityAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    @Unique
    private boolean alreadyCalculated;

    protected PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    /**
     * Retrieves the total crit chance from equipped items, attributes, and other sources.
     * Takes into account the new crit overhaul configuration.
     *
     * @return The total crit chance.
     */
    public float moonay$getCritRateFromEquipped() {
        //TODO: Have it mor configable
        if (ModConfig.CONFIG.shouldDoCrit()) {
            MutableFloat critRate = new MutableFloat();

            //ATTRIBUTE
            //CritChance
            critRate.add(Objects.requireNonNull(this.getAttributeInstance(ModEntityAttributes.GENERIC_CRIT_CHANCE)).getValue()); //Get From attribute
            //Luck
            critRate.add(Objects.requireNonNull(this.getAttributeInstance(EntityAttributes.GENERIC_LUCK)).getValue() * 5); //Get From attribute
            return critRate.floatValue();
        }
        return 0;
    }

    /**
     * Retrieves the total crit damage from equipped items and attributes, considering the new crit overhaul configuration.
     *
     * @return The total crit damage.
     */
    public float moonay$getCritDamageFromEquipped() {
        //TODO: Have it mor configable
        if (ModConfig.CONFIG.shouldDoCrit()) {
            MutableFloat critDamage = new MutableFloat();
            //ATTRIBUTE
            //CritDamage
            critDamage.add(Objects.requireNonNull(this.getAttributeInstance(ModEntityAttributes.GENERIC_CRIT_DAMAGE)).getValue()); //Get From attribute
            return critDamage.floatValue();
        }
        return 0;
    }

    /**
     * Modifies the damage value before it's stored, considering the new crit overhaul configuration.
     *
     * @param original The original damage value.
     * @return The modified damage value.
     */
    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 1), ordinal = 0)
    private float storedamage(float original) {
        if (ModConfig.CONFIG.shouldDoCrit()) {
            float modifiedDamage = this.calculateCritDamage(original);
            this.alreadyCalculated = original != modifiedDamage;
            return modifiedDamage;
        }
        return original;
    }

    /**
     * Modifies the constant representing the default critical damage value.
     *
     * @param defaultcritdmg The default critical damage value.
     * @return The modified critical damage value.
     */
    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 1.5F))
    private float storevanillacritdmg(float defaultcritdmg) {
        if (ModConfig.CONFIG.shouldDoCrit()) {
            float modifiedCritDamage = this.alreadyCalculated ? 1.0F : (this.storeCrit().moonay$isCritical() ? this.getTotalCritDamage() / 100.0F + 1.0F : defaultcritdmg);
            this.alreadyCalculated = false;
            return modifiedCritDamage;
        }
        return defaultcritdmg;
    }
}
