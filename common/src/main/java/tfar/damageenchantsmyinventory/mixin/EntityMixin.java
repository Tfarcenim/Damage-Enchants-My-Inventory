package tfar.damageenchantsmyinventory.mixin;

import com.mojang.serialization.Dynamic;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
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

    @Unique
    EntityModData entityModData = new EntityModData();

    @Override
    public EntityModData getModData() {
        return entityModData;
    }

    @Override
    public void setModData(EntityModData entityModData) {
        if (!Objects.equals(entityModData,this.entityModData) && !level().isClientSide) {
            Services.PLATFORM.sendToTrackingClients(new S2CEntityModData((Entity)(Object)this,entityModData),(Entity)(Object)this);
        }
        this.entityModData = entityModData;
    }

    @Inject(method = "setSecondsOnFire",at = @At("HEAD"))
    private void cancelInfernal(int $$0, CallbackInfo ci) {
        modifyData(EntityModData.INFERNAL_FIRE,false);
    }

    @Inject(method = "saveWithoutId",at = @At("HEAD"))
    private void addExtra(CompoundTag $$0, CallbackInfoReturnable<CompoundTag> cir){
        $$0.put("entityModData",EntityModData.CODEC.encodeStart(NbtOps.INSTANCE, entityModData).resultOrPartial(DamageEnchantsMyInventory.LOG::error).orElseThrow());
    }

    @Inject(method = "load",at = @At("HEAD"))
    private void readExtra(CompoundTag $$0, CallbackInfo ci){
        entityModData = EntityModData.CODEC.parse(new Dynamic<>(NbtOps.INSTANCE,$$0.get("entityModData"))).resultOrPartial(DamageEnchantsMyInventory.LOG::error).orElseThrow();
    }
}
