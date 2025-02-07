package tfar.damageenchantsmyinventory.platform;

import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.lang3.tuple.Pair;
import tfar.damageenchantsmyinventory.DamageEnchantsMyInventory;
import tfar.damageenchantsmyinventory.DamageEnchantsMyInventoryForge;
import tfar.damageenchantsmyinventory.PacketHandlerForge;
import tfar.damageenchantsmyinventory.compat.WoodwalkersCompat;
import tfar.damageenchantsmyinventory.mobeffect.PolymorphMobEffect;
import tfar.damageenchantsmyinventory.network.client.S2CModPacket;
import tfar.damageenchantsmyinventory.network.server.C2SModPacket;
import tfar.damageenchantsmyinventory.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;
import tocraft.remorphed.Remorphed;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public String getPlatformName() {

        return "Forge";
    }

    @Override
    public boolean isModLoaded(String modId) {

        return ModList.get().isLoaded(modId);
    }

    @Override
    public boolean isDevelopmentEnvironment() {

        return !FMLLoader.isProduction();
    }

    @Override
    public <F> void registerAll(Map<String, ? extends F> map, Registry<F> registry, Class<? extends F> filter) {
        List<Pair<ResourceLocation, Supplier<?>>> list = DamageEnchantsMyInventoryForge.registerLater.computeIfAbsent(registry, k -> new ArrayList<>());
        for (Map.Entry<String, ? extends F> entry : map.entrySet()) {
            list.add(Pair.of(DamageEnchantsMyInventory.id(entry.getKey()), entry::getValue));
        }
    }

    @Override
    public <F> void unfreeze(Registry<F> registry) {
        ((MappedRegistry<F>)registry).unfreeze();
    }

    int i;

    @Override
    public <MSG extends S2CModPacket> void registerClientPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        PacketHandlerForge.INSTANCE.registerMessage(i++, packetLocation, MSG::write, reader, PacketHandlerForge.wrapS2C());
    }

    @Override
    public <MSG extends C2SModPacket> void registerServerPacket(Class<MSG> packetLocation, Function<FriendlyByteBuf, MSG> reader) {
        PacketHandlerForge.INSTANCE.registerMessage(i++, packetLocation, MSG::write, reader, PacketHandlerForge.wrapC2S());
    }


    @Override
    public void sendToClient(S2CModPacket msg, ServerPlayer player) {
        PacketHandlerForge.sendToClient(msg, player);
    }

    @Override
    public void sendToServer(C2SModPacket msg) {
        PacketHandlerForge.sendToServer(msg);
    }

    @Override
    public void sendToTrackingClients(S2CModPacket msg, Entity entity) {
        PacketHandlerForge.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),msg);
    }

    @Override
    public void morphIntoPassiveMob(LivingEntity livingEntity) {
        if (livingEntity instanceof ServerPlayer player) {
            EntityType<? extends LivingEntity> type = PolymorphMobEffect.TYPES.get(livingEntity.getRandom().nextInt(PolymorphMobEffect.TYPES.size()));
            WoodwalkersCompat.morph(player, type.create(livingEntity.level()));
        }
    }

    @Override
    public void demorph(LivingEntity livingEntity) {
        if (livingEntity instanceof ServerPlayer player) {
            WoodwalkersCompat.morph(player, null);
        }
    }
}