package tfar.damageenchantsmyinventory.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import tfar.damageenchantsmyinventory.client.ClientPacketHandler;
import tfar.damageenchantsmyinventory.init.ModEntityDataSerializers;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class ClonePlayerEntity extends PathfinderMob implements OwnableEntity{

    protected static final EntityDataAccessor<GameProfile> DATA_GAME_PROFILE = SynchedEntityData.defineId(ClonePlayerEntity.class, ModEntityDataSerializers.GAME_PROFILE);

    protected UUID owner;


    public ClonePlayerEntity(EntityType<? extends PathfinderMob> $$0, Level $$1) {
        super($$0, $$1);
        Arrays.fill(this.armorDropChances, 0);
        Arrays.fill(this.handDropChances, 0);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE,1).add(Attributes.MOVEMENT_SPEED, 0.3).add(Attributes.MAX_HEALTH,20);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new RunFromHuntersGoal(this,1.25));
        //this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, LivingEntity.class, true, this::shouldAttack));
      //  goalSelector.addGoal(3,new CloneFollowOwnerGoal(this,1,4,12,false));

     //   this.targetSelector.addGoal(1, new CloneOwnerHurtByTargetGoal(this));
      //  this.targetSelector.addGoal(1, new CloneOwnerHurtTargetGoal(this));
    }

    boolean shouldAttack(LivingEntity living) {
        UUID uuid1 = living.getUUID();
        UUID uuid2 = getOwnerUUID();
        return !Objects.equals(uuid1,uuid2);
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_GAME_PROFILE,new GameProfile(Util.NIL_UUID,""));

    }


    public void setClone(GameProfile clone) {
        entityData.set(DATA_GAME_PROFILE,clone);
    }

    public GameProfile getClone() {
        return entityData.get(DATA_GAME_PROFILE);
    }

    public ResourceLocation getSkinTextureLocation() {
        return ClientPacketHandler.getPlayerSkin(getClone());
    }

    @Override
    public void tick() {
        super.tick();
        if (tickCount > 160 && !level().isClientSide) {
            discard();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        GameProfile uuid = getClone();
        if (uuid != null) {
            CompoundTag gameTag = NbtUtils.writeGameProfile(new CompoundTag(),uuid);
            tag.put("clone",gameTag);
        }
        if (owner != null) {
            tag.putUUID("owner",owner);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("clone")) {
            GameProfile gameProfile = NbtUtils.readGameProfile(tag.getCompound("clone"));
            setClone(gameProfile);
        }
        if (tag.hasUUID("owner")) {
            setOwnerUUID(tag.getUUID("owner"));
        }
    }

    @Nullable
    @Override
    public UUID getOwnerUUID() {
        return owner;
    }

    public void setOwnerUUID(UUID owner) {
        this.owner = owner;
    }

}
