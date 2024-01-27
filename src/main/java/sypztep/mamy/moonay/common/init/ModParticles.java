package sypztep.mamy.moonay.common.init;

import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.mamy.moonay.common.MoonayMod;


public class ModParticles {
    public static DefaultParticleType SHOCKWAVE;
    public static DefaultParticleType BLOODWAVE;
    public static DefaultParticleType WARP;

    public static void init(){
        SHOCKWAVE = Registry.register(Registries.PARTICLE_TYPE, MoonayMod.id("shockwave"), FabricParticleTypes.simple(true));
        BLOODWAVE = Registry.register(Registries.PARTICLE_TYPE, MoonayMod.id("bloodwave"), FabricParticleTypes.simple(true));
        WARP = Registry.register(Registries.PARTICLE_TYPE, MoonayMod.id("warp"), FabricParticleTypes.simple(true));
    }
}
