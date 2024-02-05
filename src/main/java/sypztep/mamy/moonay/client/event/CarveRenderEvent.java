package sypztep.mamy.moonay.client.event;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import sypztep.mamy.moonay.common.init.ModEnchantments;
import sypztep.mamy.moonay.common.init.ModStatusEffects;
import sypztep.mamy.moonay.common.util.AbilityHelper;
import sypztep.mamy.moonay.common.util.MoonayHelper;
import sypztep.mamy.moonay.mixin.enchantment.special.InGameHudAccessor;

import static sypztep.mamy.moonay.common.init.ModConfig.CONFIG;

public class CarveRenderEvent implements HudRenderCallback {
    public static int forcedHeight = -1;

    @Override
    public void onHudRender(DrawContext drawContext, float tickDelta) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        ClientPlayerEntity client = minecraft.player;


        if (client != null) {
            if (MoonayHelper.hasEnchantment(ModEnchantments.CARVE,client.getMainHandStack())) {
                int scaledWidth = minecraft.getWindow().getScaledWidth(), scaledHeight = minecraft.getWindow().getScaledHeight();
                if (MoonayHelper.hasStatusWithAmpValue$greather(ModStatusEffects.STALWART,client,0)) {
//                    MoonayHelper.drawtextcustom(drawContext, MinecraftClient.getInstance().textRenderer, "Healing Bonus : " + (MoonayHelper.getStatusAmp(ModStatusEffects.STALWART, client) + AbilityHelper.getMissingHealth(client, 0.05f)),
//                            (scaledWidth / 2) - 25, (scaledHeight / 2) + 6, 0xffffff, 0, false);
                    forcedHeight = (scaledHeight / 2) + 6;
                 ((InGameHudAccessor) minecraft.inGameHud).moonay$renderHealthBar(drawContext, minecraft.player, (scaledWidth / 2) - 25,forcedHeight , 1, -1, (float) Math.ceil(MoonayHelper.getStatusAmp(ModStatusEffects.STALWART, client) + AbilityHelper.getMissingHealth(client, 0.5f)), (int) (MoonayHelper.getStatusAmp(ModStatusEffects.STALWART, client) + AbilityHelper.getMissingHealth(client, CONFIG.carvehealratio)), (int) (MoonayHelper.getStatusAmp(ModStatusEffects.STALWART, client) + AbilityHelper.getMissingHealth(client, CONFIG.carvehealratio)), 0, false);
                    forcedHeight = -1;
                }
            }
        }
    }
}
