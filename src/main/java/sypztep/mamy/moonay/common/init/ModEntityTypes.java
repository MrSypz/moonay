package sypztep.mamy.moonay.common.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.mamy.moonay.common.MoonayMod;
import sypztep.mamy.moonay.common.entity.projectile.NeedleEntity;
import sypztep.mamy.moonay.common.entity.projectile.StygiaEntity;

public class ModEntityTypes {
	public static EntityType<StygiaEntity> STYGIA;
	public static final EntityType<NeedleEntity> NEEDLE = FabricEntityTypeBuilder.<NeedleEntity>create(SpawnGroup.MISC, NeedleEntity::new).dimensions(EntityType.ARROW.getDimensions()).build();

	public static void init() {
		Registry.register(Registries.ENTITY_TYPE, MoonayMod.id("needle"), NEEDLE);

		STYGIA = registerEntity("stygia", createNoHitbock(StygiaEntity::new));
	}
	private static <T extends Entity> EntityType<T> registerEntity(String name, EntityType<T> entityType) {
		return Registry.register(Registries.ENTITY_TYPE, MoonayMod.id(name), entityType);
	}
	private static <T extends Entity> EntityType<T> createNoHitbock(EntityType.EntityFactory<T> factory) {
		return FabricEntityTypeBuilder.create(SpawnGroup.MISC, factory).dimensions(EntityDimensions.changing(0.1f, 0.1f)).trackRangeBlocks(512).trackedUpdateRate(4).build();
	}
}
