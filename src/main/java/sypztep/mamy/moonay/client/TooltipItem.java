package sypztep.mamy.moonay.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttributes;
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
            addCarveTooltip(lines, client);
        } else if (MoonayHelper.hasEnt(ModEnchantments.STIGMA, stack)) {
            addStigmaTooltip(lines, client);
        }
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
            double amount = damage * 0.25f + AbilityHelper.getMissingHealth(client, 0.12f) * AbilityHelper.getHitAmount();
            addFormattedTooltip(lines, amount, "stigma", "stigma.desc");
        }
    }

    private static void addFormattedTooltip(List<Text> lines, double amount, String key, String... extraKeys) {
        String formattedAmount = String.format("%.2f", amount);
        MutableFloat mutableFloat = new MutableFloat(formattedAmount);

        Text tooltip = Text.translatable(MoonayMod.MODID + ".modifytooltip." + key).formatted(Formatting.GRAY)
                .append(Text.literal(mutableFloat.toString()).formatted(Formatting.RED));

        for (String extraKey : extraKeys) {
            tooltip = tooltip.copy().append(Text.translatable(MoonayMod.MODID + ".modifytooltip." + extraKey).formatted(Formatting.GRAY));
        }

        lines.add(tooltip);
    }
}
