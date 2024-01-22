package sypztep.mamy.moonay.common.statuseffect;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;

public class MarkStatusEffect extends EmptyStatusEffect {
    public MarkStatusEffect(StatusEffectCategory category) {
        super(category,0);
    }
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration <= 5 && amplifier != 0;
    }
}
