package tfar.damageenchantsmyinventory.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.damageenchantsmyinventory.DamageEnchantsMyInventoryForge;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(method = "inventoryTick",at = @At("HEAD"))
    private void onItemTick(ItemStack stack, Level level, Entity entity, int slot, boolean equipped, CallbackInfo ci) {
        DamageEnchantsMyInventoryForge.onItemTick(stack,level,entity,slot,equipped);
    }
}
