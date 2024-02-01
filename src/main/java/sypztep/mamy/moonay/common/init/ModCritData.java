package sypztep.mamy.moonay.common.init;

import sypztep.mamy.moonay.common.data.CritOverhaulConfig;
import sypztep.mamy.moonay.common.data.CritOverhaulEntry;

import java.util.List;

public class ModCritData {
    private static final CritOverhaulConfig critOverhaulConfig = new CritOverhaulConfig();
    public static void initItemData() {
        List<CritOverhaulEntry> itemsToAdd = List.of(
                new CritOverhaulEntry("minecraft:iron_sword", 20f, 0.0f),
                new CritOverhaulEntry("minecraft:diamond_sword", 30f, 10.0f),
                new CritOverhaulEntry("minecraft:iron_chestplate",15f,0f)
        );
        critOverhaulConfig.addItems(itemsToAdd);

    }
}
