package tfar.damageenchantsmyinventory.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import tfar.damageenchantsmyinventory.ducks.EntityDuck;
import tfar.damageenchantsmyinventory.network.client.S2CEntityModData;

public class ClientPacketHandler {

    public static void handle(S2CEntityModData s2CEntityModData) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            Entity entity = level.getEntity(s2CEntityModData.entityID);
            if (entity != null) {
                EntityDuck.of(entity).setModData(s2CEntityModData.data);
            }
        }
    }
}
