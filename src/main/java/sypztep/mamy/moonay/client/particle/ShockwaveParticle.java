package sypztep.mamy.moonay.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Quaternionf;
import org.joml.Vector3f;


@Environment(EnvType.CLIENT)
public class ShockwaveParticle extends SpriteBillboardParticle {
    private final SpriteProvider spriteProvider;
    private static final Quaternionf QUATERNION = new Quaternionf(0F, -0.7F, 0.7F, 0F);

    ShockwaveParticle(ClientWorld world, double x, double y, double z, SpriteProvider spriteProvider, double velocityX, double velocityY, double velocityZ) {
        super(world, x, y , z, 0.0, 0.0, 0.0);
        this.spriteProvider = spriteProvider;
        this.maxAge = 10;
        this.scale = 4.5f;
        this.gravityStrength = 0.008F;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.setSpriteForAge(this.spriteProvider);
    }
    @Override
    public void buildGeometry(VertexConsumer buffer, Camera camera, float ticks) {
        Vec3d vec3 = camera.getPos();
        float x = (float) (MathHelper.lerp(ticks, this.prevPosX, this.x) - vec3.getX());
        float y = (float) (MathHelper.lerp(ticks, this.prevPosY, this.y) - vec3.getY());
        float z = (float) (MathHelper.lerp(ticks, this.prevPosZ, this.z) - vec3.getZ());

        Vector3f[] vector3fs = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
        Vector3f[] vector3fsBottom = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, -1.0F, 0.0F)};

        float f4 = this.getSize(ticks);

        for (int i = 0; i < 4; ++i) {
            Vector3f vector3f = vector3fs[i];
            vector3f.rotate(QUATERNION);
            vector3f.mul(f4);
            vector3f.add(x, y, z);

            Vector3f vector3fBottom = vector3fsBottom[i];
            vector3fBottom.rotate(QUATERNION);
            vector3fBottom.mul(f4);
            vector3fBottom.add(x, y - 0.1F, z);
        }

        float f7 = this.getMinU();
        float f8 = this.getMaxU();
        float f5 = this.getMinV();
        float f6 = this.getMaxV();
        int light = this.getBrightness(ticks);

        buffer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).texture(f8, f6).color(this.red, this.green, this.blue, this.alpha).light(light).next();
        buffer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).texture(f8, f5).color(this.red, this.green, this.blue, this.alpha).light(light).next();
        buffer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).texture(f7, f5).color(this.red, this.green, this.blue, this.alpha).light(light).next();
        buffer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).texture(f7, f6).color(this.red, this.green, this.blue, this.alpha).light(light).next();

        buffer.vertex(vector3fs[3].x(), vector3fs[3].y(), vector3fs[3].z()).texture(f7, f6).color(this.red, this.green, this.blue, this.alpha).light(light).next();
        buffer.vertex(vector3fs[2].x(), vector3fs[2].y(), vector3fs[2].z()).texture(f7, f5).color(this.red, this.green, this.blue, this.alpha).light(light).next();
        buffer.vertex(vector3fs[1].x(), vector3fs[1].y(), vector3fs[1].z()).texture(f8, f5).color(this.red, this.green, this.blue, this.alpha).light(light).next();
        buffer.vertex(vector3fs[0].x(), vector3fs[0].y(), vector3fs[0].z()).texture(f8, f6).color(this.red, this.green, this.blue, this.alpha).light(light).next();
    }
    @Override
    public void tick() {
        super.tick();
        this.setSpriteForAge(this.spriteProvider);
    }
    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public record Factory(SpriteProvider sprites) implements ParticleFactory<DefaultParticleType> {
        public Particle createParticle(DefaultParticleType type, ClientWorld world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new ShockwaveParticle(world, x, y, z, sprites, xSpeed, ySpeed, zSpeed);
        }
    }
}
