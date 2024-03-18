package sypztep.mamy.moonay.common.util;

import net.minecraft.entity.player.PlayerEntity;
import sypztep.mamy.moonay.common.init.ModEnchantments;

import java.util.Random;

public interface NewCriticalOverhaul {

    void moonay$setCritical(boolean setCrit);

    boolean moonay$isCritical();
    default float calculateCritDamage(float amount) {
        float totalCritRate = this.getTotalCritRate();
        float totalCritDMG = this.getTotalCritDamage();
        if (!this.storeCrit().moonay$isCritical() && (!(totalCritDMG > 0.0F) || !(totalCritRate > 0.0F) || !(totalCritRate >= 100.0F) && !(this.moonay$getRand().nextFloat() < totalCritRate / 100.0F))) {
            return amount;
        } else {
            this.storeCrit().moonay$setCritical(true);
            return amount * (100.0F + totalCritDMG) / 100.0F;
        }
    }

    Random moonay$getRand();

    default NewCriticalOverhaul storeCrit() {
        return this;
    }
    default void moonay$setCritRate(float critRate) {
    }
    default void moonay$setCritDamage(float critDamage) {
    }
    default float moonay$getCritRate() { //Crit rate = 0
        return 0.0F;
    }
    default float moonay$getCritRateFromEquipped() {
        return 0.0F; //Crit rate = 0%
    }
    default float moonay$getCritDamage() {
        return 50.0F; //Crit damage = 50%
    }
    default float moonay$getCritDamageFromEquipped() {
        return 0.0F;
    }
    default float getTotalCritRate() {
        float totalCritRate = this.moonay$getCritRate() + this.moonay$getCritRateFromEquipped();
        if (!(this instanceof PlayerEntity player)) {
            return totalCritRate;
        } else {
            if (MoonayHelper.hasEnchantment(ModEnchantments.APINOX, player.getMainHandStack()))
                return Math.min(100, totalCritRate);
            else
                return totalCritRate;
        }
    }
    default float getTotalCritDamage() {
        float totalCritDamage = this.moonay$getCritDamage() + this.moonay$getCritDamageFromEquipped();
        if (!(this instanceof PlayerEntity player)) {
            return totalCritDamage;
        } else {
            if (MoonayHelper.hasEnchantment(ModEnchantments.APINOX, player.getMainHandStack())) {
                int lvl = MoonayHelper.getEnchantmentLvl(ModEnchantments.APINOX, player.getMainHandStack());
                //not use getTotalCritRate it decrease value
                float totalCritRate = this.moonay$getCritRate() + this.moonay$getCritRateFromEquipped();
                float additionalCritDamage = 0.0F;
                if (totalCritRate > 100.0F)
                    additionalCritDamage = lvl * ((float) Math.floor((totalCritRate - 100.0F) / 10.0F)); // Formula enchant lvl * ((totalcrit * 100) / 10) exam:lvl 1 = 1% lvl 2 = 2% lvl 3 = 3%
                return additionalCritDamage + totalCritDamage;
            }
            return totalCritDamage;
        }
    }
}
