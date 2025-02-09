package tfar.damageenchantsmyinventory.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import tfar.damageenchantsmyinventory.init.ModMobEffects;

@Mixin(MouseHandler.class)
public class MouseHandlerMixin {

    @Shadow @Final private Minecraft minecraft;

    @ModifyArg(method = "turnPlayer",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"),index = 0)
    private double invertX(double original) {
        if (minecraft.player.hasEffect(ModMobEffects.INVERTED_CONTROLS)) {
            return -original;
        }
        return original;
    }

    @ModifyArg(method = "turnPlayer",at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"),index = 1)
    private double invertY(double original) {
        if (minecraft.player.hasEffect(ModMobEffects.INVERTED_CONTROLS)) {
            return -original;
        }
        return original;
    }

}
