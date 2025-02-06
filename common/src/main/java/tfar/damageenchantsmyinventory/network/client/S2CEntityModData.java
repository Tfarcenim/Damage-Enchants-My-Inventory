package tfar.damageenchantsmyinventory.network.client;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import tfar.damageenchantsmyinventory.EntityModData;
import tfar.damageenchantsmyinventory.client.ClientPacketHandler;

public class S2CEntityModData implements S2CModPacket{

    public final int entityID;
    public final EntityModData data;

    public S2CEntityModData(Entity entity,EntityModData data) {
        entityID = entity.getId();
        this.data = data;
    }

    public S2CEntityModData(FriendlyByteBuf buf) {
        entityID = buf.readInt();
        data = EntityModData.fromPacket(buf);
    }
    @Override
    public void handleClient() {
        ClientPacketHandler.handle(this);
    }

    @Override
    public void write(FriendlyByteBuf to) {
        to.writeInt(entityID);
        data.toPacket(to);
    }
}
