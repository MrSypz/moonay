package sypztep.mamy.moonay.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.apache.commons.lang3.mutable.MutableFloat;
import sypztep.mamy.moonay.common.MoonayMod;
import sypztep.mamy.moonay.common.init.ModConfig;
import sypztep.mamy.moonay.common.init.ModEnchantments;
import sypztep.mamy.moonay.common.init.ModStatusEffects;
import sypztep.mamy.moonay.common.util.AbilityHelper;
import sypztep.mamy.moonay.common.util.MoonayHelper;

import java.util.List;


@Environment(EnvType.CLIENT)
public class TooltipItem {
    public static void onTooltipRender(ItemStack stack, List<Text> lines, TooltipContext context) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        if (MoonayHelper.hasEnchantment(ModEnchantments.CARVE, stack)) {
            addCarveTooltip(lines, player);
        } else if (MoonayHelper.hasEnchantment(ModEnchantments.STIGMA, stack)) {
            addStigmaTooltip(lines, player);
        } else if (MoonayHelper.hasEnchantment(ModEnchantments.PRAMINAX, stack)) {
            addPraminaxTooltip(lines, player);
        }
    }

    private static void addCarveTooltip(List<Text> lines, ClientPlayerEntity client) {
        if (client != null) {
            double amount = MoonayHelper.getStatusAmp(ModStatusEffects.STALWART, client) + AbilityHelper.getMissingHealth(client, ModConfig.CONFIG.carvehealratio);
            addFormattedTooltip(lines, String.format("%.2f",amount), "carve");
        }
    }

    private static void addStigmaTooltip(List<Text> lines, ClientPlayerEntity client) {
        if (client != null) {
            double amount = 0.25f + AbilityHelper.getMissingHealth(client, ModConfig.CONFIG.stigmahealratio);
            addFormattedTooltip(lines, String.format("%.2f",amount) + " x " + AbilityHelper.getHitAmount(), "stigma","stigma.condition");
            addFormattedTooltip(lines, 1.5f + "% x " + (float) MoonayHelper.getEnchantmentLvl(ModEnchantments.STIGMA, client.getMainHandStack()) + "%", "stigma.desc","stigma.desc2");
        }
    }
    private static void addPraminaxTooltip(List<Text> lines, ClientPlayerEntity client) {
        if (client != null) {
            int amount = 20 * MoonayHelper.getEnchantmentLvl(ModEnchantments.PRAMINAX,client.getMainHandStack());
            addFormattedTooltip(lines, String.format("%d",amount) + "%", "praminax" );
        }
    }

    private static void addFormattedTooltip(List<Text> lines, String value, String key, String... extraKeys) {
        Text tooltip = Text.translatable(MoonayMod.MODID + ".modifytooltip." + key).formatted(Formatting.GRAY)
                .append(Text.literal(value).formatted(Formatting.RED));

        for (String extraKey : extraKeys)
            tooltip = tooltip.copy().append(Text.translatable(MoonayMod.MODID + ".modifytooltip." + extraKey).formatted(Formatting.GRAY));

        lines.add(tooltip);
    }


    private static void addFormattedTooltip(List<Text> lines, double amount, String key,Formatting formatting, String... extraKeys) {
        String formattedAmount = String.format("%.2f", amount);
        MutableFloat mutableFloat = new MutableFloat(formattedAmount);

        Text tooltip = Text.literal(" " + mutableFloat + "% ").formatted(formatting).append(Text.translatable(MoonayMod.MODID + ".modifytooltip." + key).formatted(formatting));

        for (String extraKey : extraKeys)
            tooltip = tooltip.copy().append(Text.translatable(MoonayMod.MODID + ".modifytooltip." + extraKey).formatted(Formatting.DARK_GREEN));

        lines.add(tooltip);
    }
}
