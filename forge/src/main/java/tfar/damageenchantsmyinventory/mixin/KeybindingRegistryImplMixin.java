package tfar.damageenchantsmyinventory.mixin;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tocraft.craftedcore.registration.forge.KeyBindingRegistryImpl;
//this exists because of mods
@Mixin(value = KeyBindingRegistryImpl.class,remap = false)
public class KeybindingRegistryImplMixin {
    @Inject(method = "register",at = @At("HEAD"),cancellable = true)
    private static void no(KeyMapping keyMapping, CallbackInfo ci) {
        if (Minecraft.getInstance() == null) ci.cancel();
    }
}