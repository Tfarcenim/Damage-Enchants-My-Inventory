package tfar.damageenchantsmyinventory.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import tfar.damageenchantsmyinventory.DamageEnchantsMyInventory;
import tfar.damageenchantsmyinventory.ducks.EntityDuck;
import tfar.damageenchantsmyinventory.network.client.S2CEntityModData;

public class ClientPacketHandler {

    public static final Material INFERNAL_FIRE_0 = new Material(TextureAtlas.LOCATION_BLOCKS, DamageEnchantsMyInventory.id("block/infernal_fire_0"));
    public static final Material INFERNAL_FIRE_1 = new Material(TextureAtlas.LOCATION_BLOCKS, DamageEnchantsMyInventory.id("block/infernal_fire_1"));


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
