package tfar.damageenchantsmyinventory.network;

import net.minecraft.resources.ResourceLocation;
import tfar.damageenchantsmyinventory.DamageEnchantsMyInventory;
import tfar.damageenchantsmyinventory.network.client.S2CEntityModData;
import tfar.damageenchantsmyinventory.platform.Services;

import java.util.Locale;

public class PacketHandler {

    public static void registerPackets() {
        Services.PLATFORM.registerClientPacket(S2CEntityModData.class, S2CEntityModData::new);
       // Services.PLATFORM.registerServerPacket(C2SSetTablePacket.class, C2SSetTablePacket::new);


    }

    public static ResourceLocation packet(Class<?> clazz) {
        return DamageEnchantsMyInventory.id(clazz.getName().toLowerCase(Locale.ROOT));
    }

}
