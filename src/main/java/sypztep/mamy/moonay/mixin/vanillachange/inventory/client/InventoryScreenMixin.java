package sypztep.mamy.moonay.mixin.vanillachange.inventory.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import sypztep.mamy.moonay.common.MoonayMod;
import sypztep.mamy.moonay.common.init.ModConfig;
import sypztep.mamy.moonay.common.util.MoonayHelper;
import sypztep.mamy.moonay.common.util.NewCriticalOverhaul;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends AbstractInventoryScreen<PlayerScreenHandler> implements RecipeBookProvider {
    @Unique
    private static final Identifier PLAYERINFO_TEXTURE = MoonayMod.id("textures/gui/container/player_info.png");

    @Shadow @Final private RecipeBookWidget recipeBook;

    public InventoryScreenMixin(PlayerScreenHandler screenHandler, PlayerInventory playerInventory, Text text) {
        super(screenHandler, playerInventory, text);
    }

    @Inject(method = "drawBackground", at = @At(value = "RETURN"))
    public void drawBackgroundMixin(DrawContext context, float delta, int mouseX, int mouseY, CallbackInfo info) {
        if (!ModConfig.CONFIG.playerstats)
            return;
        ClientPlayerEntity player = MinecraftClient.getInstance().player;
        double reduce = calculateDamageReduction(Objects.requireNonNull(player).getArmor(),player.getAttributeValue(EntityAttributes.GENERIC_ARMOR_TOUGHNESS));
        MutableText[] information = new MutableText[]{
                Text.translatable(MoonayMod.MODID + ".gui.player_info." + "header"),
                Text.translatable(MoonayMod.MODID + ".gui.player_info." + "critchance").append(String.format(": %.2f%%", getCritRate(player))),
                Text.translatable(MoonayMod.MODID + ".gui.player_info." + "critdamge").append(String.format(": %.2f%%", getCritDamage(player))),
                Text.translatable(MoonayMod.MODID + ".gui.player_info." + "damagereduce").append(String.format(": %.2f%%", reduce))
        };
        if (!recipeBook.isOpen()) {
            int i = this.x - 128;
            int j = this.y ;
            // UI
            context.drawTexture(PLAYERINFO_TEXTURE, i, j, 0, 0, 128, 128,128,128);

            int yOffset = 10;  // Initial offset
            int xOffset = 35;

            for (int index = 0; index < information.length; index++) {
                MutableText text = information[index];

                int offset = (index == 0) ? 20 : 14;
                int xoffset2 = 10;

                MoonayHelper.drawtextcustom(context, textRenderer, text.getString(), i + xOffset, j + yOffset, 0xFFFFFF, 0, false);

                // Skip the header(0)
                if (index != 0 && isMouseOverText(mouseX, mouseY, i + xOffset, j + yOffset, textRenderer.getWidth(text), 10)) {
                    List<Text> tooltipText = new ArrayList<>();
                    // Add individual tooltip lines for other elements
                    tooltipText.add(Text.translatable(MoonayMod.MODID + ".gui.player_info.tooltip." + "critchance").append(String.format(": %.2f%%", getCritRate(player))).formatted(Formatting.GRAY));
                    tooltipText.add(Text.translatable(MoonayMod.MODID + ".gui.player_info.tooltip." + "critdamage").append(String.format(": %.2f%%", getCritDamage(player))).formatted(Formatting.GRAY));
                    tooltipText.add(Text.translatable(MoonayMod.MODID + ".gui.player_info.tooltip." + "damagereduce").append(String.format(": %.2f%%", reduce)).formatted(Formatting.GRAY));
                    tooltipText.add(Text.translatable(MoonayMod.MODID + ".gui.player_info.tooltip." + "empty"));
                    if (index < tooltipText.size()) {
                        int x = mouseX - 5;
                        int y = mouseY + 2;
                        context.drawTooltip(textRenderer, tooltipText.get(index - 1), x, y);
                    }
                }
                yOffset += offset;
                xOffset = xoffset2;
            }
        }
    }
    @Unique
    public double calculateDamageReduction(int armorPoints, double armorToughness) {
        // The base damage reduction is 80% when armor is at 20 points
        double baseDamageReduction = 80.0;

        // Calculate the effective damage reduction based on armor points
        double effectiveDamageReduction = baseDamageReduction * (armorPoints / 20.0);

        // Adjust the effective damage reduction based on armor toughness
        effectiveDamageReduction += armorToughness;

        // Cap the effective damage reduction at 80%
        effectiveDamageReduction = Math.min(80.0, effectiveDamageReduction);

        return effectiveDamageReduction;
    }
    @Unique
    private float getCritRate(ClientPlayerEntity player) {
        if (player instanceof NewCriticalOverhaul invoker)
            return invoker.getTotalCritRate();

        return 0.0F; // Return a default value if the player is not a LivingEntityInvoker
    }
    @Unique
    private float getCritDamage(ClientPlayerEntity player) {
        if (player instanceof NewCriticalOverhaul invoker)
            return invoker.getTotalCritDamage();
        return 0.0F; // Return a default value if the player is not a LivingEntityInvoker
    }

    @Unique
    private boolean isMouseOverText(int mouseX, int mouseY, int textX, int textY, int textWidth, int textHeight) {
        return mouseX >= textX && mouseX <= textX + textWidth && mouseY >= textY && mouseY <= textY + textHeight;
    }
}