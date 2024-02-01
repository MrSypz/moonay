package sypztep.mamy.moonay.mixin.util;

import com.terraformersmc.modmenu.util.mod.Mod;
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
import sypztep.mamy.moonay.common.util.NewCriticalOverhaul;
import sypztep.mamy.moonay.common.packetc2s.SyncCritPacket;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements NewCriticalOverhaul {
    @Unique
    private boolean crit;

    LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "damage", at = @At("HEAD"))
    private void damageFirst(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.CONFIG.newCritOverhaul)
            if (source.getAttacker() instanceof NewCriticalOverhaul newCriticalOverhaul && source.getSource() instanceof PersistentProjectileEntity projectile)
                newCriticalOverhaul.moonay$setCritical(projectile.isCritical());
    }

    @Inject(method = "damage", at = @At("RETURN"))
    private void handleCrit(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (ModConfig.CONFIG.newCritOverhaul)
            if (source.getAttacker() instanceof NewCriticalOverhaul newCriticalOverhaul)
                newCriticalOverhaul.moonay$setCritical(false);
    }

    @Override
    public void moonay$setCritical(boolean setCrit) {
        if (ModConfig.CONFIG.newCritOverhaul) {
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
