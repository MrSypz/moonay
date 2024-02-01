package sypztep.mamy.moonay.common.data;

import com.google.gson.*;

import java.io.FileReader;
import java.io.Reader;
import java.util.Map;

public class CritChanceConfig {
    private Map<String, CritChanceItem> critChanceData;

    public CritChanceConfig(String filePath) {
        Gson gson = new Gson();
        try (Reader reader = new FileReader(filePath)) {
            critChanceData = gson.fromJson(reader, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CritChanceItem getItem(String itemName) {
        return critChanceData.getOrDefault(itemName, new CritChanceItem(0, 0));
    }

    private static class CritChanceItem {
        private float critChance;
        private float critDamage;

        public CritChanceItem(float critChance, float critDamage) {
            this.critChance = critChance;
            this.critDamage = critDamage;
        }

        public float getCritChance() {
            return critChance;
        }

        public float getCritDamage() {
            return critDamage;
        }
    }
}
