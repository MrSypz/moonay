package sypztep.mamy.moonay.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import sypztep.mamy.moonay.client.packets2c.AddCarveSoulParticlePacket;
import sypztep.mamy.moonay.client.packets2c.AddStigmaParticlePacket;
import sypztep.mamy.moonay.client.particle.BloodwaveParticle;
import sypztep.mamy.moonay.client.particle.ShockwaveParticle;
import sypztep.mamy.moonay.common.init.ModParticles;

public class MoonayClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register(((stack, context, lines) ->
                TooltipItem.onTooltipRender(stack,lines,context)));

        ClientPlayNetworking.registerGlobalReceiver(AddCarveSoulParticlePacket.ID, new AddCarveSoulParticlePacket.Receiver());
        ClientPlayNetworking.registerGlobalReceiver(AddStigmaParticlePacket.ID, new AddStigmaParticlePacket.Receiver());

        ParticleFactoryRegistry particleRegistry = ParticleFactoryRegistry.getInstance();
        particleRegistry.register(ModParticles.SHOCKWAVE, ShockwaveParticle.Factory::new);
        particleRegistry.register(ModParticles.BLOODWAVE, BloodwaveParticle.Factory::new);
    }
}
