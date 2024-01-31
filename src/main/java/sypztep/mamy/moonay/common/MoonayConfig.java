package sypztep.mamy.moonay.common;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "moonay")
@Config.Gui.Background("minecraft:textures/block/dirt.png")

public class MoonayConfig implements ConfigData {
    @Comment("How many percent that gonna stripe armor out.")
    public double carveModifier = -0.08D;
    @Comment("Movement speed multiply from stalwart.")
    public double stalwartSpeedModifier = 0.02D;
    public boolean newCritOverhaul = true;
    public boolean playerstats = true;
}
