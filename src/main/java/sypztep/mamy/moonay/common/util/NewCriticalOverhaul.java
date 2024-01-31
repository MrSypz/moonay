package sypztep.mamy.moonay.common.util;

import java.util.Random;

public interface NewCriticalOverhaul {

    void moonay$setCritical(boolean flag);

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
        return 0.0F;
    }

    default float getTotalCritRate() {
        return this.moonay$getCritRate() + this.moonay$getCritRateFromEquipped();
    }

    default float moonay$getCritDamage() {//Crit damage = 50%
        return 50.0F;
    }

    default float moonay$getCritDamageFromEquipped() {
        return 0.0F;
    }

    default float getTotalCritDamage() {
        return this.moonay$getCritDamage() + this.moonay$getCritDamageFromEquipped();
    }
}
