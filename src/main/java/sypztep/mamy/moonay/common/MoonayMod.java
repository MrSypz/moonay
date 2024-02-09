package sypztep.mamy.moonay.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sypztep.mamy.moonay.common.init.*;
import sypztep.mamy.moonay.common.packetc2s.CarveSoulPacket;
import sypztep.mamy.moonay.common.packetc2s.StigmaPacket;

public class MoonayMod implements ModInitializer {
    public static final String MODID = "moonay";
    public static Identifier id(String id){
        return new Identifier(MODID,id);
    }
    public static final Logger LOGGER = LoggerFactory.getLogger(MODID);

    @Override
    public void onInitialize() {
        ModConfig.init();

        //Mod Init
        ModEnchantments.init();
        ModStatusEffects.init();
        ModParticles.init();
        ModEntityAttributes.init();
        ModCritData.initItemData();

        ServerPlayNetworking.registerGlobalReceiver(CarveSoulPacket.ID,new CarveSoulPacket.Receiver());
        ServerPlayNetworking.registerGlobalReceiver(StigmaPacket.ID,new StigmaPacket.Receiver());
    }
}
