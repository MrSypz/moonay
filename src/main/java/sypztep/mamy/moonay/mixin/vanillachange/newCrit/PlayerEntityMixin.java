package sypztep.mamy.moonay.mixin.vanillachange.newCrit;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import sypztep.mamy.moonay.common.init.ModConfig;
import sypztep.mamy.moonay.common.init.ModEntityAttributes;

import java.util.Objects;
import java.util.function.Consumer;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntityMixin {
    @Unique
    private boolean alreadyCalculated;

    protected PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    private void forEachItemExceptOffHand(Consumer<ItemStack> itemStackConsumer) {
        if (ModConfig.CONFIG.newCritOverhaul) {
            if (!this.getWorld().isClient()) {
                EquipmentSlot[] slot = EquipmentSlot.values();
                int getslot = slot.length;

                for (int i = 0; i < getslot; ++i) {
                    EquipmentSlot equipmentSlot = slot[i];
                    if (equipmentSlot != EquipmentSlot.OFFHAND) {
                        ItemStack itemStack = this.getEquippedStack(equipmentSlot);
                        if (!itemStack.isEmpty())
                            itemStackConsumer.accept(itemStack);
                    }
                }
            }
        }
    }
    public float moonay$getCritRateFromEquipped() {
        if (ModConfig.CONFIG.newCritOverhaul) {
            MutableFloat additionalRate = new MutableFloat();
            additionalRate.add(Objects.requireNonNull(this.getAttributeInstance(ModEntityAttributes.GENERIC_CRIT_CHANCE)).getValue());

            this.forEachItemExceptOffHand((itemStack) -> {
                additionalRate.add(Objects.requireNonNull(this.getAttributeInstance(ModEntityAttributes.GENERIC_CRIT_CHANCE)).getValue());
            });
            return additionalRate.floatValue();
        }
        return 0;
    }
    public float moonay$getCritDamageFromEquipped() {
        if (ModConfig.CONFIG.newCritOverhaul) {
            MutableFloat additionalDamage = new MutableFloat();
            additionalDamage.add(Objects.requireNonNull(this.getAttributeInstance(ModEntityAttributes.GENERIC_CRIT_DAMAGE)).getValue());

            this.forEachItemExceptOffHand((itemStack) -> {
                additionalDamage.add(Objects.requireNonNull(this.getAttributeInstance(ModEntityAttributes.GENERIC_CRIT_DAMAGE)).getValue());
//            additionalDamage.add(Critical.CRIT_DAMAGE.getPercent(EnchantmentHelper.getLevel(Critical.CRIT_DAMAGE, itemStack)));
            });
            return additionalDamage.floatValue();
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
