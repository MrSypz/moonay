package sypztep.mamy.moonay.mixin.vanillachange.newcrit.util;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import sypztep.mamy.moonay.client.packets2c.SyncCritS2CPacket;
import sypztep.mamy.moonay.common.init.ModConfig;
import sypztep.mamy.moonay.common.packetc2s.SyncCritPacket;
import sypztep.mamy.moonay.common.util.NewCriticalOverhaul;

@Mixin(LivingEntity.class)
public abstract class LivingEntityUtilMixin extends Entity implements NewCriticalOverhaul {
    @Unique
    private boolean crit;

    LivingEntityUtilMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    /**
     * Injects into the "damage" method at the HEAD to handle critical hit logic before damage is applied.
     * This method sets the critical hit status of the attacking entity based on the source.
     *
     * @param source The source of the damage.
     * @param amount The amount of damage to be dealt.
     * @param cir    The callback info.
     */
    @Inject(method = "damage", at = @At("HEAD"))
    private void damageFirst(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.CONFIG.shouldDoCrit()) {
            if (source.getAttacker() instanceof NewCriticalOverhaul newCriticalOverhaul &&
                    source.getSource() instanceof PersistentProjectileEntity)
                newCriticalOverhaul.moonay$setCritical(this.moonay$isCritical());
        }
    }

    /**
     * Injects into the "damage" method at the RETURN to handle critical hit logic after damage is applied.
     * This method resets the critical hit status of the attacking entity to false.
     *
     * @param source The source of the damage.
     * @param amount The amount of damage dealt.
     * @param cir    The callback info.
     */
    @Inject(method = "damage", at = @At("RETURN"))
    private void handleCrit(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.CONFIG.shouldDoCrit()) {
            if (source.getAttacker() instanceof NewCriticalOverhaul newCriticalOverhaul)
                newCriticalOverhaul.moonay$setCritical(false);
        }
    }


    /**
     * Sets the critical hit status of the entity.
     *
     * @param setCrit If true, the entity is marked as performing a critical hit.
     *                If false, the entity is not considered to perform a critical hit.
     *                The critical hit status is synchronized with clients through packets.
     */
    @Override
    public void moonay$setCritical(boolean setCrit) {
        if (ModConfig.CONFIG.shouldDoCrit()) {
            this.crit = setCrit;
            if (!this.getWorld().isClient) {
                PacketByteBuf byteBuf = new SyncCritPacket(this.getId(), this.crit).write(PacketByteBufs.create());
                this.getWorld().getServer().getPlayerManager().sendToAll(new CustomPayloadS2CPacket(SyncCritS2CPacket.ID, byteBuf));
            }
        }
    }

    @Override
    public boolean moonay$isCritical() {
        return this.crit;
    }
}
