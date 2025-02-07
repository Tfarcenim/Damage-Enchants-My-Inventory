package tfar.damageenchantsmyinventory.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.damageenchantsmyinventory.DEMIConfig;
import tfar.damageenchantsmyinventory.init.ModEnchantments;
import tfar.damageenchantsmyinventory.platform.Services;

@Mixin(CrossbowItem.class)
public class CrossbowItemMixin {
    private static ThreadLocal<ItemStack> localStack = ThreadLocal.withInitial(() -> ItemStack.EMPTY);
    private static ThreadLocal<LivingEntity> localLivingEntity = ThreadLocal.withInitial(() -> null);

    @Inject(method = "use",at = @At("HEAD"))
    private void captureLocals(Level $$0, Player $$1, InteractionHand $$2, CallbackInfoReturnable<InteractionResultHolder<ItemStack>> cir) {
        localStack.set($$1.getItemInHand($$2));
        localLivingEntity.set($$1);
    }

    @ModifyArg(method = "use",at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/item/CrossbowItem;performShooting(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/item/ItemStack;FF)V"),index = 5)
    private float modifyInaccuracy(float original) {
        boolean misfire = Services.PLATFORM.getEnchantmentLevel(localStack.get(),ModEnchantments.MISFIRE) > 0;
        if (misfire && localLivingEntity.get().getRandom().nextDouble() < DEMIConfig.misfire_chance) {
            return 100 * original;
        }
        return original;
    }
}
