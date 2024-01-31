package sypztep.mamy.moonay.client.packets2c;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;
import sypztep.mamy.moonay.common.MoonayMod;
import sypztep.mamy.moonay.common.util.NewCriticalOverhaul;
import sypztep.mamy.moonay.common.packetc2s.SyncCritPacket;

public class SyncCritS2CPacket {
    public static final Identifier ID = MoonayMod.id("sync_crit");
    @Environment(EnvType.CLIENT)
    public static class Receiver implements ClientPlayNetworking.PlayChannelHandler {
        @Override
        public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            SyncCritPacket packet = new SyncCritPacket(buf);
            if (client.world != null && client.world.getEntityById(packet.getEntityId()) instanceof NewCriticalOverhaul invoker) {
                invoker.moonay$setCritical(packet.setBoolean());
            }
        }
    }
}
