package sypztep.mamy.moonay.common.data;

public class CritOverhaulEntry {
    private String id;
    private final float critChance;
    private final float critDamage;

    public CritOverhaulEntry(float critChance, float critDamage) {
        this.critChance = critChance;
        this.critDamage = critDamage;
    }
    public CritOverhaulEntry(String id, float critChance, float critDamage) {
        this.id = id;
        this.critChance = critChance;
        this.critDamage = critDamage;
    }

    public float getCritChance() {
        return critChance;
    }
    public float getCritDamage() {
        return critDamage;
    }

    public String getId() {
        return id;
    }
}
