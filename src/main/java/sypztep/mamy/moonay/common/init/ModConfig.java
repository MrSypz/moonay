package sypztep.mamy.moonay.common.init;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import sypztep.mamy.moonay.common.MoonayConfig;

public class ModConfig {
    public static MoonayConfig CONFIG = new MoonayConfig();
    public static void init() {
        //Registering Cloth Config (GSON)
        AutoConfig.register(MoonayConfig.class, GsonConfigSerializer::new);
        CONFIG = AutoConfig.getConfigHolder(MoonayConfig.class).getConfig();
    }
}
