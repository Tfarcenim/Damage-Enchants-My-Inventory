package tfar.damageenchantsmyinventory.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tfar.damageenchantsmyinventory.DEMIConfig;
import tfar.damageenchantsmyinventory.EntityModData;
import tfar.damageenchantsmyinventory.ducks.EntityDuck;
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
        if (misfire && localLivingEntity.get().getRandom().nextDouble() < DEMIConfig.DEMI_CONFIG.misfire_chance.get()) {
            return 100 * original;
        }
        return original;
    }

    @Inject(method = "releaseUsing",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void onArrowAdded(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft, CallbackInfo ci,
                              Player player, boolean flag, ItemStack itemstack, int i, float f, boolean flag1, ArrowItem arrowitem, AbstractArrow abstractarrow) {

        if (pStack.getEnchantmentLevel(ModEnchantments.MISFIRE) > 0 && pEntityLiving.getRandom().nextDouble() < DEMIConfig.DEMI_CONFIG.critical_misfire_chance.get()) {
            Vec3 look = player.getLookAngle();
            abstractarrow.leftOwner = true;
            abstractarrow.setPos(abstractarrow.position().add(look.reverse()));
        }
        if (pStack.getEnchantmentLevel(ModEnchantments.INFERNAL_FLAME) > 0) {
            EntityDuck.of(abstractarrow).modifyData(EntityModData.INFERNAL_FIRE,true);
        }
    }
}
