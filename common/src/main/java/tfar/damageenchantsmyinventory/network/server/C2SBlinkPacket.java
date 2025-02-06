package tfar.damageenchantsmyinventory.network.server;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import tfar.damageenchantsmyinventory.ducks.EntityDuck;

public class C2SBlinkPacket implements C2SModPacket {


    public C2SBlinkPacket(FriendlyByteBuf buf) {

    }

    public C2SBlinkPacket() {

    }

    @Override
    public void handleServer(ServerPlayer player) {
        if (EntityDuck.of(player).getModData().blinkTimer() > 0) {
            BlockHitResult hitResult = (BlockHitResult) player.pick(5, 1, false);
            Vec3 location = hitResult.getLocation();
            player.teleportTo(location.x, location.y, location.z);
        }
    }

    @Override
    public void write(FriendlyByteBuf to) {

    }
}
