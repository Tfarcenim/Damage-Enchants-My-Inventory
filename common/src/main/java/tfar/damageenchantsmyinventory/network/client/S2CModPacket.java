package tfar.damageenchantsmyinventory.network.client;


import tfar.damageenchantsmyinventory.network.ModPacket;

public interface S2CModPacket extends ModPacket {

    void handleClient();

}
