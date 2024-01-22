package sypztep.mamy.moonay.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import sypztep.mamy.moonay.common.MoonayMod;
import sypztep.mamy.moonay.common.init.ModEnchantments;
import sypztep.mamy.moonay.common.init.ModStatusEffects;
import sypztep.mamy.moonay.common.util.MoonayHelper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class TooltipItem {
    public static void onTooltipRender(ItemStack stack, List<Text> lines, TooltipContext context) {
        if (MoonayHelper.hasEnt(ModEnchantments.CARVE, stack)) {
            ClientPlayerEntity client = MinecraftClient.getInstance().player;
            lines.add(Text.translatable(MoonayMod.MODID + ".modifytooltip").formatted(Formatting.GRAY).append(Text.literal(String.valueOf(MoonayHelper.getStatusAmp(ModStatusEffects.STALWART, Objects.requireNonNull(client)))).formatted(Formatting.RED)));
        }
    }
}
