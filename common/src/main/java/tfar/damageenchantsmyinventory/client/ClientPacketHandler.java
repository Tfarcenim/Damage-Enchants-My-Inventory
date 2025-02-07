package tfar.damageenchantsmyinventory.client;

import com.google.common.collect.ImmutableMap;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import tfar.damageenchantsmyinventory.DamageEnchantsMyInventory;
import tfar.damageenchantsmyinventory.ducks.EntityDuck;
import tfar.damageenchantsmyinventory.entity.ClonePlayerEntity;
import tfar.damageenchantsmyinventory.network.client.S2CEntityModData;

import java.util.Map;

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

    public static Map<String, EntityRenderer<ClonePlayerEntity>> createCloneRenderers(EntityRendererProvider.Context context) {
        ImmutableMap.Builder<String, EntityRenderer<ClonePlayerEntity>> builder = ImmutableMap.builder();
        CLONE_PROVIDERS.forEach((s, provider) -> {
            try {
                builder.put(s, provider.create(context));
            } catch (Exception var5) {
                throw new IllegalArgumentException("Failed to create player model for " + s, var5);
            }
        });
        return builder.build();
    }

    public static ResourceLocation getPlayerSkin(GameProfile gameProfile) {
        Minecraft minecraft = Minecraft.getInstance();
        SkinManager skinManager = minecraft.getSkinManager();
        Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = skinManager.getInsecureSkinInformation(gameProfile);
        return map.containsKey(MinecraftProfileTexture.Type.SKIN) ? skinManager.registerTexture(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN) :
                DefaultPlayerSkin.getDefaultSkin(UUIDUtil.getOrCreatePlayerUUID(gameProfile));
    }

    private static final Map<String, EntityRendererProvider<ClonePlayerEntity>> CLONE_PROVIDERS = ImmutableMap.of(
            "default", (context) -> new ClonePlayerEntityRenderer(context, false),
            "slim", (context) -> new ClonePlayerEntityRenderer(context, true));

}
