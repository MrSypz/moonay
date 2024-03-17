package sypztep.mamy.moonay.client.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import sypztep.mamy.moonay.common.MoonayMod;
import sypztep.mamy.moonay.common.entity.projectile.StygiaEntity;

@Environment(EnvType.CLIENT)
public class StygiaEntityRenderer extends ProjectileEntityRenderer<StygiaEntity> {
    private static final Identifier TEXTURE = MoonayMod.id("textures/entity/static/orbital.png");
    public StygiaEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public Identifier getTexture(StygiaEntity entity) {
        return TEXTURE;
    }

    @Override
    public void render(StygiaEntity entity, float f, float g, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light) {
        float progress = entity.ticksExisted / 14F;
        float scale = MathHelper.lerp(progress, 1, 0.0625F) * MathHelper.lerp((float) 1024 / 16F, 0.1F, 1);
        float v = (Math.floorMod(entity.getWorld().getTime(), 40) + g) / 4;
        float u = v + 4 * -0.5F / scale;
        VertexConsumer vertices = vertexConsumerProvider.getBuffer(RenderLayer.getEntityAlpha(TEXTURE));
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(-MathHelper.lerp(g, entity.prevYaw, entity.getYaw())));
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(g, entity.prevPitch, entity.getPitch())));
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((entity.getWorld().getTime() + entity.age) * 12));

        // Smooth scaling interpolation
        float smoothScale = smoothScale(progress);
        matrices.scale(scale * smoothScale, 1f, scale * smoothScale);

        MatrixStack.Entry entry = matrices.peek();
        for (int j = 0; j < entity.maxY; j++) {
            for (int i = 0; i < 360; i += 15) {
                drawPlane(entry.getPositionMatrix(), entry.getNormalMatrix(), vertices, u, v);
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(i));
            }
            matrices.translate(0, 1, 0);
        }
        matrices.pop();
    }

    // Smooth scale interpolation function
    private static float smoothScale(float progress) {
        // Apply smoothstep function for smoother interpolation
        float t = MathHelper.clamp(progress, 0, 1);
        return t * t * (3 - 2 * t);
    }

    private static void drawPlane(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertices, float u, float v) {
        drawVertex(positionMatrix, normalMatrix, vertices, 1, 0, 1, u);
        drawVertex(positionMatrix, normalMatrix, vertices, 0, 0, 1, v);
        drawVertex(positionMatrix, normalMatrix, vertices, 0, 0.25f, 0, v);
        drawVertex(positionMatrix, normalMatrix, vertices, 1, 0.25f, 0, u);

        drawVertex(positionMatrix, normalMatrix, vertices, 0, 0.25f, 0, v);
        drawVertex(positionMatrix, normalMatrix, vertices, 0, 0, 1, v);
        drawVertex(positionMatrix, normalMatrix, vertices, 1, 0, 1, u);
        drawVertex(positionMatrix, normalMatrix, vertices, 1, 0.25f, 0, u);

    }

    private static void drawVertex(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertices, int y, float z, float u, float v) {
        vertices.vertex(positionMatrix, 0, y, z).color(255, 255, 255, 255).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(LightmapTextureManager.MAX_LIGHT_COORDINATE).normal(normalMatrix, 0, 1, 0).next();
    }
}
