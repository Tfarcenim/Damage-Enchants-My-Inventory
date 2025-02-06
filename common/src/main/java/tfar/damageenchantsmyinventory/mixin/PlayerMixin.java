package tfar.damageenchantsmyinventory.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.damageenchantsmyinventory.PlayerDuck;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerDuck {
    @Unique
    boolean runner;

    protected PlayerMixin(EntityType<? extends LivingEntity> $$0, Level $$1) {
        super($$0, $$1);
    }

    @Override
    public boolean isRunner() {
        return runner;
    }

    @Override
    public void setRunner(boolean runner) {
        this.runner = runner;
    }


    @Inject(method = "addAdditionalSaveData",at = @At("RETURN"))
    private void addExtra(CompoundTag $$0, CallbackInfo ci){
        $$0.putBoolean("runner",runner);
    }


    @Inject(method = "readAdditionalSaveData",at = @At("RETURN"))
    private void readExtra(CompoundTag $$0, CallbackInfo ci){
        runner = $$0.getBoolean("runner");
    }

}
