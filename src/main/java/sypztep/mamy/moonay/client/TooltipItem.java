package sypztep.mamy.moonay.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.mutable.MutableFloat;
import sypztep.mamy.moonay.common.MoonayMod;
import sypztep.mamy.moonay.common.enchantment.EmptyEnchantment;
import sypztep.mamy.moonay.common.init.ModEnchantments;
import sypztep.mamy.moonay.common.init.ModStatusEffects;
import sypztep.mamy.moonay.common.util.AbilityHelper;
import sypztep.mamy.moonay.common.util.MoonayHelper;

import java.util.List;

@Environment(EnvType.CLIENT)
public class TooltipItem {
    public static void onTooltipRender(ItemStack stack, List<Text> lines, TooltipContext context) {
        ClientPlayerEntity client = MinecraftClient.getInstance().player;
        if (MoonayHelper.hasEnt(ModEnchantments.CARVE, stack)) {
            if (client != null) {
                String formattedAmount = String.format("%.2f", MoonayHelper.getStatusAmp(ModStatusEffects.STALWART, client) + AbilityHelper.getMissingHealth(client, 0.05f));
                MutableFloat mutableFloat = new MutableFloat(formattedAmount);
                if (MoonayHelper.getStatusAmp(ModStatusEffects.STALWART, client) > 0)
                    lines.add(Text.translatable(MoonayMod.MODID + ".modifytooltip.carve").formatted(Formatting.GRAY)
                            .append(Text.literal(mutableFloat.toString()).formatted(Formatting.RED)));
                else lines.add(Text.translatable(MoonayMod.MODID + ".modifytooltip.carve").formatted(Formatting.GRAY)
                        .append(Text.literal(String.valueOf(MoonayHelper.getStatusAmp(ModStatusEffects.STALWART, client))).formatted(Formatting.RED)));
            }
        } else if (MoonayHelper.hasEnt(ModEnchantments.STIGMA,stack)) {
            if (client != null) {
                String formattedAmount = String.format("%.2f", MoonayHelper.getStatusAmp(ModStatusEffects.STALWART, client) + AbilityHelper.getMissingHealth(client, 0.05f));
                MutableFloat mutableFloat = new MutableFloat(formattedAmount);
                if (MoonayHelper.getStatusAmp(ModStatusEffects.STALWART, client) > 0)
                    lines.add(Text.translatable(MoonayMod.MODID + ".modifytooltip.stigma").formatted(Formatting.GRAY)
                            .append(Text.literal(mutableFloat.toString()).formatted(Formatting.RED)));
                else lines.add(Text.translatable(MoonayMod.MODID + ".modifytooltip.stigma").formatted(Formatting.GRAY)
                        .append(Text.literal(String.valueOf(MoonayHelper.getStatusAmp(ModStatusEffects.STALWART, client))).formatted(Formatting.RED)));
            }
        }
    }
}
