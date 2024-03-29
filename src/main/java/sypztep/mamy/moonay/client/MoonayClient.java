package sypztep.mamy.moonay.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import sypztep.mamy.moonay.client.event.HealthBarRenderEvent;
import sypztep.mamy.moonay.client.packets2c.*;
import sypztep.mamy.moonay.client.particle.BloodwaveParticle;
import sypztep.mamy.moonay.client.particle.ShockwaveParticle;
import sypztep.mamy.moonay.client.particle.WarpParticle;
import sypztep.mamy.moonay.client.render.NeedleEntityRenderer;
import sypztep.mamy.moonay.client.render.StygiaEntityRenderer;
import sypztep.mamy.moonay.common.init.ModConfig;
import sypztep.mamy.moonay.common.init.ModEnchantments;
import sypztep.mamy.moonay.common.init.ModEntityTypes;
import sypztep.mamy.moonay.common.init.ModParticles;
import sypztep.mamy.moonay.common.util.AbilityHelper;
import sypztep.mamy.moonay.common.util.MoonayHelper;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class MoonayClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register(((stack, context, lines) ->
                TooltipItem.onTooltipRender(stack,lines,context)));

        ClientPlayNetworking.registerGlobalReceiver(AddCarveSoulParticlePacket.ID, new AddCarveSoulParticlePacket.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(AddStigmaParticlePacket.ID, new AddStigmaParticlePacket.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(SyncCritS2CPacket.ID, new SyncCritS2CPacket.Receiver());

        ParticleFactoryRegistry particleRegistry = ParticleFactoryRegistry.getInstance();
        particleRegistry.register(ModParticles.SHOCKWAVE, ShockwaveParticle.Factory::new);
        particleRegistry.register(ModParticles.BLOODWAVE, BloodwaveParticle.Factory::new);
        particleRegistry.register(ModParticles.WARP, WarpParticle.Factory::new);

        EntityRendererRegistry.register(ModEntityTypes.NEEDLE, NeedleEntityRenderer::new);
        EntityRendererRegistry.register(ModEntityTypes.STYGIA, StygiaEntityRenderer::new);


        HudRenderCallback.EVENT.register(new HealthBarRenderEvent());

        ClientTickEvents.END_CLIENT_TICK.register(minecraft -> {
            if (minecraft.player != null) {
                if (MoonayHelper.hasEnchantment(ModEnchantments.STIGMA,minecraft.player.getMainHandStack()))
                    AbilityHelper.boxArea(minecraft.player, ModConfig.CONFIG.stigmarange);
            }
        });
    }

}
