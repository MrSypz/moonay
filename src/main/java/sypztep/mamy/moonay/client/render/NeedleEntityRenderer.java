package sypztep.mamy.moonay.client.render;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.util.Identifier;
import sypztep.mamy.moonay.common.MoonayMod;
import sypztep.mamy.moonay.common.entity.projectile.NeedleEntity;

public class NeedleEntityRenderer extends ProjectileEntityRenderer<NeedleEntity> {
	private static final Identifier TEXTURE = MoonayMod.id("textures/entity/projectiles/needle.png");

	public NeedleEntityRenderer(EntityRendererFactory.Context context) {
		super(context);
	}

	@Override
	public Identifier getTexture(NeedleEntity entity) {
		return TEXTURE;
	}
}
