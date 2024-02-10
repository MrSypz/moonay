/*
 * All Rights Reserved (c) MoriyaShiine
 */

package sypztep.mamy.moonay.common.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import sypztep.mamy.moonay.common.MoonayMod;
import sypztep.mamy.moonay.common.entity.projectile.NeedleEntity;

public class ModEntityTypes {
	public static final EntityType<NeedleEntity> NEEDLE = FabricEntityTypeBuilder.<NeedleEntity>create(SpawnGroup.MISC, NeedleEntity::new).dimensions(EntityType.ARROW.getDimensions()).build();

	public static void init() {
		Registry.register(Registries.ENTITY_TYPE, MoonayMod.id("ice_shard"), NEEDLE);
	}
}
