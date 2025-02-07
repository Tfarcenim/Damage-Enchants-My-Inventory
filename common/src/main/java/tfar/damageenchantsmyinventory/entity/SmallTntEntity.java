package tfar.damageenchantsmyinventory.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TraceableEntity;
import tfar.damageenchantsmyinventory.init.ModEntityTypes;

public class SmallTntEntity extends Entity implements TraceableEntity {
        private static final EntityDataAccessor<Integer> DATA_FUSE_ID = SynchedEntityData.defineId(SmallTntEntity.class, EntityDataSerializers.INT);
        private static final int DEFAULT_FUSE_TIME = 80;
        @Nullable
        private LivingEntity owner;

        public SmallTntEntity(EntityType<? extends SmallTntEntity> pEntityType, Level pLevel) {
            super(pEntityType, pLevel);
            this.blocksBuilding = true;
        }

        public SmallTntEntity(Level pLevel, double pX, double pY, double pZ, @Nullable LivingEntity pOwner) {
            this(ModEntityTypes.SMALL_TNT, pLevel);
            this.setPos(pX, pY, pZ);
            double angle = pLevel.random.nextDouble() * 2*Math.PI;
            this.setDeltaMovement(-Math.sin(angle) * 0.02, 0.2, -Math.cos(angle) * 0.02);
            this.setFuse(80);
            this.xo = pX;
            this.yo = pY;
            this.zo = pZ;
            this.owner = pOwner;
        }

        @Override
        protected void defineSynchedData() {
            this.entityData.define(DATA_FUSE_ID, DEFAULT_FUSE_TIME);
        }

        @Override
        protected Entity.MovementEmission getMovementEmission() {
            return MovementEmission.NONE;
        }

        @Override
        public boolean isPickable() {
            return !this.isRemoved();
        }

        @Override
        public void tick() {
            if (!this.isNoGravity()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.04, 0.0));
            }

            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.98));
            if (this.onGround()) {
                this.setDeltaMovement(this.getDeltaMovement().multiply(0.7, -0.5, 0.7));
            }

            int $$0 = this.getFuse() - 1;
            this.setFuse($$0);
            if ($$0 <= 0) {
                this.discard();
                if (!this.level().isClientSide) {
                    this.explode();
                }
            } else {
                this.updateInWaterStateAndDoFluidPushing();
                if (this.level().isClientSide) {
                    this.level().addParticle(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5, this.getZ(), 0.0, 0.0, 0.0);
                }
            }

        }

        protected void explode() {
            float power = 4.0F;
            this.level().explode(this, this.getX(), this.getY(0.0625), this.getZ(), power, Level.ExplosionInteraction.TNT);
        }

        @Override
        protected void addAdditionalSaveData(CompoundTag pCompound) {
            pCompound.putShort("Fuse", (short)this.getFuse());
        }

        @Override
        protected void readAdditionalSaveData(CompoundTag pCompound) {
            this.setFuse(pCompound.getShort("Fuse"));
        }

        @Override
        @Nullable
        public LivingEntity getOwner() {
            return this.owner;
        }

        @Override
        protected float getEyeHeight(Pose pPose, EntityDimensions pSize) {
            return 0.15F;
        }

        public void setFuse(int pLife) {
            this.entityData.set(DATA_FUSE_ID, pLife);
        }

        public int getFuse() {
            return this.entityData.get(DATA_FUSE_ID);
        }

}

