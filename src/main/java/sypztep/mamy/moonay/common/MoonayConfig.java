package sypztep.mamy.moonay.common;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "moonay")
@Config.Gui.Background("minecraft:textures/block/dirt.png")

public class MoonayConfig implements ConfigData {
    @Comment("How many perc that gonna stripe armor out Default 8% per stack")
    public double carveModifier = -0.08D;
}
