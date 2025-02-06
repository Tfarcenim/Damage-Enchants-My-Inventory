package tfar.damageenchantsmyinventory.network.server;

import net.minecraft.server.level.ServerPlayer;
import tfar.damageenchantsmyinventory.network.ModPacket;

public interface C2SModPacket extends ModPacket {

    void handleServer(ServerPlayer player);

}
