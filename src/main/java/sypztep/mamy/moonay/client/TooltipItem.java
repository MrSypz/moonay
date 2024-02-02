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
import sypztep.mamy.moonay.common.data.CritOverhaulConfig;
import sypztep.mamy.moonay.common.data.CritOverhaulEntry;
import sypztep.mamy.moonay.common.init.ModEnchantments;
import sypztep.mamy.moonay.common.init.ModStatusEffects;
import sypztep.mamy.moonay.common.util.AbilityHelper;
import sypztep.mamy.moonay.common.util.MoonayHelper;

import java.util.List;


@Environment(EnvType.CLIENT)
public class TooltipItem {
    private static final CritOverhaulConfig critOverhaulConfig = new CritOverhaulConfig();
    public static void onTooltipRender(ItemStack stack, List<Text> lines, TooltipContext context) {
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        String itemId = Registries.ITEM.getId(stack.getItem()).toString();
        if (MoonayHelper.hasEnt(ModEnchantments.CARVE, stack)) {
            addCarveTooltip(lines, player);
        } else if (MoonayHelper.hasEnt(ModEnchantments.STIGMA, stack)) {
            addStigmaTooltip(lines, player);
        }
        if (critOverhaulConfig.getCritDataForItem(itemId).isValid()) {
            if (critOverhaulConfig.getCritDataForItem(itemId).getCritChance() >= 0) {
                addCritOverhaulTooltip(stack, lines, Formatting.DARK_GREEN);
            } else if (critOverhaulConfig.getCritDataForItem(itemId).getCritChance() < 0)
                addCritOverhaulTooltip(stack, lines, Formatting.RED);
        }
    }
    private static void addCritOverhaulTooltip(ItemStack stack, List<Text> lines,Formatting color) {
        String itemName = Registries.ITEM.getId(stack.getItem()).toString();

        CritOverhaulEntry critData = critOverhaulConfig.getCritDataForItem(itemName);

        addFormattedTooltip(lines, critData.getCritChance(), "critchance" ,color);
        addFormattedTooltip(lines, critData.getCritDamage(), "critdmg" ,color);
    }

    private static void addCarveTooltip(List<Text> lines, ClientPlayerEntity client) {
        if (client != null) {
            double amount = MoonayHelper.getStatusAmp(ModStatusEffects.STALWART, client) + AbilityHelper.getMissingHealth(client, 0.05f);
            addFormattedTooltip(lines, amount, "carve");
        }
    }

    private static void addStigmaTooltip(List<Text> lines, ClientPlayerEntity client) {
        if (client != null) {
            double damage = client.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE);
            double amount = damage * 0.25f + AbilityHelper.getMissingHealth(client, 0.12f);
            addFormattedTooltip2(lines, amount, " + " + AbilityHelper.getHitAmount(), "carve", "stigma.desc");
            addFormattedTooltip(lines, 1f + MoonayHelper.getEntLvl(ModEnchantments.STIGMA,client.getMainHandStack()), "stigma.passive", "stigma.desc.passive");
        }
    }
    private static void addFormattedTooltip(List<Text> lines, double amount, String key, String... extraKeys) {
        String formattedAmount = String.format("%.2f", amount);
        MutableFloat mutableFloat = new MutableFloat(formattedAmount);

        Text tooltip = Text.translatable(MoonayMod.MODID + ".modifytooltip." + key).formatted(Formatting.GRAY)
                .append(Text.literal(mutableFloat.toString()).formatted(Formatting.RED));

        for (String extraKey : extraKeys)
            tooltip = tooltip.copy().append(Text.translatable(MoonayMod.MODID + ".modifytooltip." + extraKey).formatted(Formatting.GRAY));

        lines.add(tooltip);
    }
    private static void addFormattedTooltip(List<Text> lines, double amount, String key,Formatting formatting, String... extraKeys) {
        String formattedAmount = String.format("%.2f", amount);
        MutableFloat mutableFloat = new MutableFloat(formattedAmount);

        Text tooltip = Text.literal(" " + mutableFloat + "% ").formatted(formatting).append(Text.translatable(MoonayMod.MODID + ".modifytooltip." + key).formatted(Formatting.DARK_GREEN));

        for (String extraKey : extraKeys)
            tooltip = tooltip.copy().append(Text.translatable(MoonayMod.MODID + ".modifytooltip." + extraKey).formatted(Formatting.DARK_GREEN));

        lines.add(tooltip);
    }
    private static void addFormattedTooltip2(List<Text> lines, double amount, String string, String key, String... extraKeys) {
        String formattedAmount = String.format("%.2f", amount);
        MutableFloat mutableFloat = new MutableFloat(formattedAmount);

        Text tooltip = Text.translatable(MoonayMod.MODID + ".modifytooltip." + key).formatted(Formatting.GRAY)
                .append(Text.literal(mutableFloat + string).formatted(Formatting.RED));

        for (String extraKey : extraKeys)
            tooltip = tooltip.copy().append(Text.translatable(MoonayMod.MODID + ".modifytooltip." + extraKey).formatted(Formatting.GRAY));

        lines.add(tooltip);
    }
}
