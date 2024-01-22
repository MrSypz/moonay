package sypztep.mamy.moonay.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public class MoonayClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemTooltipCallback.EVENT.register(((stack, context, lines) ->
                TooltipItem.onTooltipRender(stack,lines,context)));
    }
}
