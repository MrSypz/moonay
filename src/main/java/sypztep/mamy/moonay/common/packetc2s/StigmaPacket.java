package sypztep.mamy.moonay.common.packetc2s;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import sypztep.mamy.moonay.client.packets2c.AddStigmaParticlePacket;
import sypztep.mamy.moonay.common.MoonayMod;

public class StigmaPacket {
	public static final Identifier ID = MoonayMod.id("stigma");

	public static void send() {
		ClientPlayNetworking.send(ID, new PacketByteBuf(Unpooled.buffer()));
	}
	public static class Receiver implements ServerPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			if (player != null)
				PlayerLookup.tracking(player).forEach(foundPlayer -> AddStigmaParticlePacket.send(foundPlayer, player.getId()));
		}
	}
}
