package sypztep.mamy.moonay.common.statuseffect;

import jdk.jfr.Category;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;

public class CooldownStatusEffect extends EmptyStatusEffect{
    public CooldownStatusEffect(StatusEffectCategory category) {
        super(category, 0);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
