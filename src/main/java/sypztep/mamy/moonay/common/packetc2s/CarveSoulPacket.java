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
import sypztep.mamy.moonay.client.packets2c.AddCarveSoulParticlePacket;
import sypztep.mamy.moonay.common.MoonayMod;

public class CarveSoulPacket {
	public static final Identifier ID = MoonayMod.id("carvesoul");

	public static void send(int power) {
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(power);
		ClientPlayNetworking.send(ID, buf);
	}
	public static class Receiver implements ServerPlayNetworking.PlayChannelHandler {
		@Override
		public void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
			int pow = buf.readInt();
			if (player != null)
				PlayerLookup.tracking(player).forEach(foundPlayer -> AddCarveSoulParticlePacket.send(foundPlayer, player.getId(),pow));
		}
	}
}
