package tfar.damageenchantsmyinventory;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.Nullable;

public class ModLevelData extends SavedData {


    private final ServerLevel serverLevel;


    Holder<Enchantment> forcedRunnerEnchantment;

    public ModLevelData(ServerLevel serverLevel) {
        this.serverLevel = serverLevel;
    }

    @Nullable
    public static ModLevelData getInstance(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .get(compoundTag -> loadStatic(compoundTag, serverLevel), name(serverLevel));
    }

    @Nullable
    public static ModLevelData getDefaultInstance(MinecraftServer server) {
        return getInstance(server.overworld());
    }

    public static ModLevelData getOrCreateInstance(ServerLevel serverLevel) {
        return serverLevel.getDataStorage()
                .computeIfAbsent(compoundTag -> loadStatic(compoundTag,serverLevel),
                        () -> new ModLevelData(serverLevel),name(serverLevel));
    }
    public static ModLevelData getOrCreateDefaultInstance(MinecraftServer server) {
        return getOrCreateInstance(server.overworld());
    }

    private static String name(ServerLevel level) {
        return  DamageEnchantsMyInventory.MOD_ID+"_"+level.dimension().location().toString().replace(':','.');
    }

    public void setForcedRunnerEnchantment(Holder<Enchantment> enchantment) {
        this.forcedRunnerEnchantment = enchantment;
    }

    public static ModLevelData loadStatic(CompoundTag compoundTag, ServerLevel level) {
        ModLevelData id = new ModLevelData(level);
        id.load(compoundTag,level);
        return id;
    }
    
    public void load(CompoundTag tag,ServerLevel level) {
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        return compoundTag;
    }
}
