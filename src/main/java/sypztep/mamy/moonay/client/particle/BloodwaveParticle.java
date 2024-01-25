package sypztep.mamy.moonay.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;


@Environment(EnvType.CLIENT)
public class BloodwaveParticle extends ShockwaveParticle {
    public void setScale(int scale) {
        this.scale = scale;
    }

    BloodwaveParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y, z, spriteProvider, velocityX, velocityY, velocityZ);
        this.maxAge = 14;
        this.gravityStrength = 0.0F;
        this.setVelocity(0D, 0D, 0D);
        this.setSpriteForAge(spriteProvider);
    }

    @Override
    protected int getBrightness(float tint) {
        return 255;
    }

    @Environment(EnvType.CLIENT)
    public record Factory(SpriteProvider sprites) implements ParticleFactory<DefaultParticleType> {
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new BloodwaveParticle(world, x, y, z, sprites, velocityX, velocityY, velocityZ);
        }
    }
}
