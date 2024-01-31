package sypztep.mamy.moonay.common.init;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.mamy.moonay.common.MoonayMod;

public class ModEntityAttributes {
    public static EntityAttribute GENERIC_CRIT_CHANCE = new ClampedEntityAttribute("attribute.generic.crit_chance",0.0,0.0,512).setTracked(true);
    public static EntityAttribute GENERIC_CRIT_DAMAGE = new ClampedEntityAttribute("attribute.generic.crit_damage",0.0,0.0,512).setTracked(true);

    public static void init() {
        init("generic.crit_chance",GENERIC_CRIT_CHANCE);
        init("generic.crit_damage",GENERIC_CRIT_DAMAGE);
    }
    private static void init(String name, EntityAttribute attribute) {
        Registry.register(Registries.ATTRIBUTE, MoonayMod.id(name) ,attribute);
    }
}
