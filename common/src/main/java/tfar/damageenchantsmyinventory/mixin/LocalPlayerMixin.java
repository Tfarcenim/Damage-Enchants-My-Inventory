package tfar.damageenchantsmyinventory.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.damageenchantsmyinventory.init.ModEnchantments;
import tfar.damageenchantsmyinventory.platform.Services;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends Player {
    public LocalPlayerMixin(Level $$0, BlockPos $$1, float $$2, GameProfile $$3) {
        super($$0, $$1, $$2, $$3);
    }

    @Inject(method = "drop",at = @At("HEAD"),cancellable = true)
    private void avoidDrop(boolean $$0, CallbackInfoReturnable<Boolean> cir) {
        if (!isCreative() && Services.PLATFORM.getEnchantmentLevel(getInventory().getSelected(),ModEnchantments.INVENTORY_LOCK) > 0) {
            cir.setReturnValue(false);
        }
    }
}
