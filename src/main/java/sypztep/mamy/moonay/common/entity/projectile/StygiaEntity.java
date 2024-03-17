/*
 * All Rights Reserved (c) MoriyaShiine
 */

package sypztep.mamy.moonay.common.entity.projectile;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import sypztep.mamy.moonay.common.init.ModEntityTypes;
import sypztep.mamy.moonay.common.init.ModParticles;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class StygiaEntity extends PersistentProjectileEntity {
	public float maxY = 0;
	public int ticksExisted = 0;

	private final Set<Entity> killedEntities = new HashSet<>();

	public StygiaEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
		super(entityType, world);
		ignoreCameraFrustum = true;

	}

	public StygiaEntity(World world, LivingEntity owner) {
		super(ModEntityTypes.STYGIA, owner, world);
		setPosition(owner.getX(), owner.getEyeY() - 0.3, owner.getZ());
	}


	@Override
	protected ItemStack asItemStack() {
		return ItemStack.EMPTY;
	}

	@Override
	public void tick() {
		setVelocity(Vec3d.ZERO);
		super.tick();
		ticksExisted++;
		maxY = 0;
		Vec3d start = getPos(), end = start.add(getRotationVector());
		while (maxY < 256) {
			maxY++;
			start = end;
			end = start.add(getRotationVector());
		}
		if (getWorld().isClient) {
			addParticles(this.getPos().getX(), this.getPos().getY(), this.getPos().getZ());
			if (ticksExisted == 9)
				getWorld().addParticle(ModParticles.SHOCKWAVE, true, this.getPos().getX(), this.getPos().getY(), this.getPos().getZ(), 0, 0, 0);
		}
		if (!getWorld().isClient) {
			if (ticksExisted == 5)
				getWorld().emitGameEvent(GameEvent.PROJECTILE_LAND, end, GameEvent.Emitter.of(this));
			if (ticksExisted > 15) {
				if (getOwner() instanceof ServerPlayerEntity player)
					Criteria.KILLED_BY_CROSSBOW.trigger(player, killedEntities);
				discard();
			}
		}
	}
	private void addParticles(double x, double y, double z) {
		World world = getWorld();
		if (world == null) return;

		Random random = new Random();

		int numParticles = 32;
		float range = 3f;

        for (int i = 0; i < numParticles; i++) {
			double angle = (double) i / numParticles * Math.PI * 2.0;
			double radius = range + (i * 0.1f) * random.nextFloat(); // Varying radius for each particle
			double yOffset = random.nextFloat() * 64.0; // Increased y-axis offset

			double xOffset = radius * Math.cos(angle);
			double zOffset = radius * Math.sin(angle);

			double posX = x + xOffset;
			double posY = y + yOffset;
			double posZ = z + zOffset;

			world.addParticle(ParticleTypes.SCULK_SOUL,true, posX, posY, posZ, 0, .10, 0);
		}
	}


	@Override
	protected void onEntityHit(EntityHitResult entityHitResult) {
	}

	@Override
	protected void onBlockHit(BlockHitResult blockHitResult) {
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
	}

}
