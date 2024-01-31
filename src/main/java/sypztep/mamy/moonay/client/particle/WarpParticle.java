package sypztep.mamy.moonay.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;

@Environment(EnvType.CLIENT)
public class WarpParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;
    WarpParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y , z, 0.0, 0.0, 0.0);
        this.spriteProvider = spriteProvider;
        this.maxAge = 8;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.scale = 0.85f;
        this.gravityStrength = 0F;
        this.setSpriteForAge(this.spriteProvider);
    }
    @Override
    public void tick() {
        super.tick();
        this.setSpriteForAge(this.spriteProvider);

        if(this.age++ >= this.maxAge || this.scale <= 0 || this.alpha <= 0) //Despawns the particle if the age has reached the max age, or if the scale is 0, or if the alpha is 0
            this.markDead(); //Despawns the particle

        else {
            this.scale += 0.1f;
            this.alpha -= 0.2f; //Slowly fades away upon hitting the ground
        }
    }

    @Override
    protected int getBrightness(float tint) {
        return 255;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }
    @Environment(EnvType.CLIENT)
    public record Factory(SpriteProvider sprites) implements ParticleFactory<DefaultParticleType> {
        public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new WarpParticle(world, x, y, z, sprites, xSpeed, ySpeed, zSpeed);
        }
    }
}
