package sypztep.mamy.moonay.client.packets2c;

import com.google.gson.JsonSyntaxException;
import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import sypztep.mamy.moonay.common.MoonayMod;
import sypztep.mamy.moonay.common.data.CritOverhaulConfig;
import sypztep.mamy.moonay.common.data.CritOverhaulData;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static sypztep.mamy.moonay.common.data.CritOverhaulConfig.gson;
public class ConfigSyncPacket {
    private static CritOverhaulData critOverhaulData = new CritOverhaulData();
    private static final CritOverhaulData SERVER_DATA = loadCritOverhaulDataFromFile();
    public static final Identifier ID = MoonayMod.id("config_sync");
    private static final Text DISCONNECT_TEXT = Text.literal("The server you are attempting to connect to has ")
            .append(Text.literal("Mooony Mod").formatted(Formatting.GREEN))
            .append(" installed, but your configuration file does not match the server's.\n" +
                    "the server already send a file to your game\n").formatted(Formatting.AQUA)
            .append(Text.literal("\n\n"))
            .append(Text.literal("This is not a bug, Just restart your game.").formatted(Formatting.DARK_RED, Formatting.BOLD));
    public static void send(ServerPlayerEntity player) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        String jsonConfig = CritOverhaulConfig.gson.toJson(SERVER_DATA);
        buf.writeString(jsonConfig);
        ServerPlayNetworking.send(player, ID, buf);
    }
    @Environment(EnvType.CLIENT)
    public static class Receiver implements ClientPlayNetworking.PlayChannelHandler {
        @Override
        public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            String jsonConfig = buf.readString();
            client.execute(() -> {
                boolean isMatch = isClientConfigMatch(jsonConfig);
                if (isMatch)
                    handler.sendChatMessage("Client configuration matches the server.");
                 else {
                    handler.getConnection().disconnect(DISCONNECT_TEXT);
                }
                // Continue with handling the received configuration
                handleReceivedConfig(jsonConfig);
            });
        }
        private boolean isClientConfigMatch(String clientJson) {
            String serverJson = CritOverhaulConfig.gson.toJson(SERVER_DATA);
            return serverJson.equals(clientJson);
        }
    }
    private static void handleReceivedConfig(String jsonConfig) {
        // Parse the JSON config and update the client's state
        try {
            CritOverhaulData receivedData = CritOverhaulConfig.gson.fromJson(jsonConfig, CritOverhaulData.class);
            updateState(receivedData);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
    }
    private static CritOverhaulData loadCritOverhaulDataFromFile() {
        Path configFilePath = Paths.get(CritOverhaulConfig.getConfigFilePath());
        if (Files.exists(configFilePath)) {
            try {
                String jsonContent = Files.readString(configFilePath);
                return CritOverhaulConfig.gson.fromJson(jsonContent, CritOverhaulData.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new CritOverhaulData();
    }
    private static void updateState(CritOverhaulData receivedData) {
        critOverhaulData = receivedData;
        saveConfigToFile();
//        System.out.println("Received updated CritOverhaulData");
    }
    private static void saveConfigToFile() {
        Path configFilePath = Paths.get(CritOverhaulConfig.getConfigFilePath());
        try (FileWriter writer = new FileWriter(configFilePath.toFile())) {
            String jsonConfig = gson.toJson(critOverhaulData);
            writer.write(jsonConfig);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
