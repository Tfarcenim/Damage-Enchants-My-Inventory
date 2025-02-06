package tfar.damageenchantsmyinventory.mixin;

import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LevelEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.damageenchantsmyinventory.DamageEnchantsMyInventory;
import tfar.damageenchantsmyinventory.EntityModData;
import tfar.damageenchantsmyinventory.ducks.EntityDuck;
import tfar.damageenchantsmyinventory.network.client.S2CEntityModData;
import tfar.damageenchantsmyinventory.platform.Services;

import java.util.Objects;

@Mixin(Entity.class)
public abstract class EntityMixin implements EntityDuck {

    @Shadow public abstract Level level();

    @Shadow public abstract int getTicksFrozen();

    @Shadow public abstract void setTicksFrozen(int pTicksFrozen);


    @Shadow private BlockPos blockPosition;
    @Unique
    private int infernalFireTicks;
    @Unique
    EntityModData entityModData = new EntityModData();

    @Override
    public EntityModData getModData() {
        return entityModData;
    }

    @Override
    public int getInfernalFireTicks() {
        return infernalFireTicks;
    }

    @Override
    public void setInfernalFireTicks(int infernalFireTicks) {
        this.infernalFireTicks = infernalFireTicks;
    }

    @Override
    public void setModData(EntityModData entityModData) {
        this.entityModData = entityModData;
    }
    @Inject(method = "baseTick",at = @At("HEAD"))
    private void entityTickEvent(CallbackInfo ci) {
        DamageEnchantsMyInventory.entityTickEvent(selfCast());
    }


    @Inject(method = "setSecondsOnFire",at = @At("HEAD"))
    private void cancelInfernal(int $$0, CallbackInfo ci) {
        modifyData(EntityModData.INFERNAL_FIRE,false);
    }

    @Inject(method = "saveWithoutId",at = @At("HEAD"))
    private void addExtra(CompoundTag tag, CallbackInfoReturnable<CompoundTag> cir){
        tag.put("entityModData",EntityModData.CODEC.encodeStart(NbtOps.INSTANCE, entityModData).resultOrPartial(DamageEnchantsMyInventory.LOG::error).orElseThrow());
    }

    @Inject(method = "load",at = @At("HEAD"))
    private void readExtra(CompoundTag tag, CallbackInfo ci){
        if (tag.contains("entityModData")) {
            EntityModData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE,tag.get("entityModData"))).resultOrPartial(DamageEnchantsMyInventory.LOG::error).ifPresent(data -> entityModData  = data);
            Services.PLATFORM.sendToTrackingClients(new S2CEntityModData((Entity)(Object)this,entityModData),selfCast());
        }
    }

    @Unique
    private Entity selfCast() {
        return (Entity) (Object) this;
    }
}
