package tfar.damageenchantsmyinventory.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.damageenchantsmyinventory.init.ModEnchantments;
import tfar.damageenchantsmyinventory.platform.Services;

@Mixin(Slot.class)
public abstract class SlotMixin {
    @Shadow public abstract ItemStack getItem();

    @Inject(method = "mayPickup",at = @At("RETURN"),cancellable = true)
    private void blockPickup(Player $$0, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;
        ItemStack itemstack = this.getItem();
        if (!$$0.isCreative() && Services.PLATFORM.getEnchantmentLevel(itemstack, ModEnchantments.INVENTORY_LOCK)>0)
            cir.setReturnValue(false);
    }
}
