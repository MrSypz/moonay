package sypztep.mamy.moonay.common.init;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.mamy.moonay.common.MoonayMod;
import sypztep.mamy.moonay.common.statuseffect.CooldownStatusEffect;
import sypztep.mamy.moonay.common.statuseffect.MarkStatusEffect;

public class ModStatusEffects {
    public static final StatusEffect CARVE = new MarkStatusEffect(StatusEffectCategory.HARMFUL)
            .addAttributeModifier(EntityAttributes.GENERIC_ARMOR,
                    "9f66ca04-e8c5-4225-952c-665ccb332fe7",
                    -0.08D,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static final StatusEffect STALWART = new CooldownStatusEffect(StatusEffectCategory.NEUTRAL)
            .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED,
                    "775287b5-79b9-48f4-9afe-ab8fd2cfd35f",
                    0.02D,
                    EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
    public static final StatusEffect STALWART_COOLDOWN = new CooldownStatusEffect(StatusEffectCategory.HARMFUL);
    public static final StatusEffect STIGMA_COOLDOWN = new CooldownStatusEffect(StatusEffectCategory.HARMFUL);
    public static final StatusEffect PRAMINAX = new CooldownStatusEffect(StatusEffectCategory.NEUTRAL);

    public static void init() {
        init("carve", CARVE);
        init("stalwart", STALWART);
        init("stalwart_cooldown", STALWART_COOLDOWN);
        init("stigma_cooldown", STIGMA_COOLDOWN);
        init("praminax", PRAMINAX);
    }
    private static void init(String name,StatusEffect statusEffect){
        Registry.register(Registries.STATUS_EFFECT, MoonayMod.id(name), statusEffect);
    }
}
