package sypztep.mamy.moonay.common;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

@Config(name = "moonay")
@Config.Gui.Background("minecraft:textures/block/dirt.png")

public class MoonayConfig implements ConfigData {
    @Comment("New Crit feature overhaul.")
    public boolean newCritOverhaul = true;
    @Comment("UI to show stats when open inventory.")
    public boolean playerstats = true;
}
