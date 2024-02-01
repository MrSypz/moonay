package sypztep.mamy.moonay.mixin.vanillachange.newCrit;

import it.unimi.dsi.fastutil.floats.FloatConsumer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.mamy.moonay.common.data.CritOverhaulConfig;
import sypztep.mamy.moonay.common.init.ModConfig;
import sypztep.mamy.moonay.common.init.ModEntityAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    @Unique
    CritOverhaulConfig critOverhaulConfig = new CritOverhaulConfig();
    @Unique
    private boolean alreadyCalculated;

    protected PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }
    /**
     *
     */
    @Unique
    private List<String> getItemIdsFromEquippedSlots() {
        List<String> itemIds = new ArrayList<>();
        EquipmentSlot[] slots = EquipmentSlot.values();

        for (EquipmentSlot slot : slots) {
            if (slot != EquipmentSlot.OFFHAND) {
                ItemStack itemStack = this.getEquippedStack(slot);
                if (!itemStack.isEmpty()) {
                    String itemId = Registries.ITEM.getId(itemStack.getItem()).toString();
                    itemIds.add(itemId);
                }
            }
        }

        return itemIds;
    }
    /**
     * Add Crit Chance Part
     */
    public float moonay$getCritRateFromEquipped() {
        if (ModConfig.CONFIG.newCritOverhaul) {
            MutableFloat critRate = new MutableFloat();
            //Add from attribute
            critRate.add(Objects.requireNonNull(this.getAttributeInstance(ModEntityAttributes.GENERIC_CRIT_CHANCE)).getValue()); //Get From attribute
            //Add from item now stackable
            List<String> equippedItemIds = getItemIdsFromEquippedSlots();
            for (String itemId : equippedItemIds) {
                critRate.add(critOverhaulConfig.getCritDataForItem(itemId).getCritChance());
            }
            return critRate.floatValue();
        }
        return 0;
    }

    /**
     * Add Crit Damage Part
     */
    public float moonay$getCritDamageFromEquipped() {
        if (ModConfig.CONFIG.newCritOverhaul) {
            MutableFloat critDamage = new MutableFloat();

            critDamage.add(Objects.requireNonNull(this.getAttributeInstance(ModEntityAttributes.GENERIC_CRIT_DAMAGE)).getValue()); //Get From attribute
            //Add from item now stackable
            List<String> equippedItemIds = getItemIdsFromEquippedSlots();
            for (String itemId : equippedItemIds) {
                critDamage.add(critOverhaulConfig.getCritDataForItem(itemId).getCritDamage());
            }
            return critDamage.floatValue();
        }
        return 0;
    }

    @ModifyVariable(method = {"attack"},at = @At(value = "STORE",ordinal = 1),ordinal = 0)
    private float storedamage(float f) {
        if (ModConfig.CONFIG.newCritOverhaul) {
            float f1 = this.calculateCritDamage(f);
            this.alreadyCalculated = f != f1;
            return f1;
        }
        return f;
    }

    @ModifyConstant(method = {"attack"},constant = {@Constant(floatValue = 1.5F)})
    private float storevanillacritdmg(float defaultcritdmg) {
        if (ModConfig.CONFIG.newCritOverhaul) {
            float f = this.alreadyCalculated ? 1.0F : (this.storeCrit().moonay$isCritical() ? this.getTotalCritDamage() / 100.0F + 1.0F : defaultcritdmg);
            this.alreadyCalculated = false;
            return f;
        }
        return defaultcritdmg;
    }
}
