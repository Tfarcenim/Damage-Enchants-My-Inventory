package tfar.damageenchantsmyinventory;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

public class ModData extends SavedData {


    private final ServerLevel serverLevel;

    boolean huntersGainEnchantments;

    public ModData(ServerLevel serverLevel) {
        this.serverLevel = serverLevel;
    }

    @Nullable
    public static ModData getInstance(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .get(compoundTag -> loadStatic(compoundTag, serverLevel), name(serverLevel));
    }

    @Nullable
    public static ModData getDefaultInstance(MinecraftServer server) {
        return getInstance(server.overworld());
    }

    public static ModData getOrCreateInstance(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .computeIfAbsent(compoundTag -> loadStatic(compoundTag,serverLevel),
                        () -> new ModData(serverLevel),name(serverLevel));
    }
    public static ModData getOrCreateDefaultInstance(MinecraftServer server) {
        return getOrCreateInstance(server.overworld());
    }

    private static String name(ServerLevel level) {
        return  DamageEnchantsMyInventory.MOD_ID+"_"+level.dimension().location().toString().replace(':','.');
    }



    public static ModData loadStatic(CompoundTag compoundTag,ServerLevel level) {
        ModData id = new ModData(level);
        id.load(compoundTag,level);
        return id;
    }
    
    public void load(CompoundTag tag,ServerLevel level) {
        huntersGainEnchantments = tag.getBoolean("Hunters_gain_enchantments");
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        compoundTag.putBoolean("hunters_gain_enchantments",huntersGainEnchantments);
        return compoundTag;
    }
}
