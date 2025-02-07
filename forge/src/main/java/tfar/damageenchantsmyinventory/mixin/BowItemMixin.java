package tfar.damageenchantsmyinventory.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.damageenchantsmyinventory.DEMIConfig;
import tfar.damageenchantsmyinventory.init.ModEnchantments;

@Mixin(BowItem.class)
//@Debug(export = true)
public class BowItemMixin {

    private static ThreadLocal<ItemStack> localStack = ThreadLocal.withInitial(() -> ItemStack.EMPTY);
    private static ThreadLocal<LivingEntity> localLivingEntity = ThreadLocal.withInitial(() -> null);

    @Inject(method = "releaseUsing",at = @At("HEAD"))
    private void captureLocals(ItemStack stack, Level $$1, LivingEntity livingEntity, int $$3, CallbackInfo ci) {
        localStack.set(stack);
        localLivingEntity.set(livingEntity);
    }

    @ModifyArg(method = "releaseUsing",at = @At(value = "INVOKE",
            target = "Lnet/minecraft/world/entity/projectile/AbstractArrow;shootFromRotation(Lnet/minecraft/world/entity/Entity;FFFFF)V"),index = 5)
    private float modifyInaccuracy(float original) {
        boolean misfire = localStack.get().getEnchantmentLevel(ModEnchantments.MISFIRE) > 0;
        if (misfire && localLivingEntity.get().getRandom().nextDouble() < DEMIConfig.misfire_chance) {
            return 100 * original;
        }
        return original;
    }
}
