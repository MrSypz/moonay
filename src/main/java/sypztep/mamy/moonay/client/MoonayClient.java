package sypztep.mamy.moonay.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import sypztep.mamy.moonay.client.packets2c.AddCarveSoulParticlePacket;

public class MoonayClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register(((stack, context, lines) ->
                TooltipItem.onTooltipRender(stack,lines,context)));

        ClientPlayNetworking.registerGlobalReceiver(AddCarveSoulParticlePacket.ID, new AddCarveSoulParticlePacket.Receiver());
    }
}
