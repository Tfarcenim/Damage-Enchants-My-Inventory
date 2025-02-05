package tfar.damageenchantsmyinventory.platform;

import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.lang3.tuple.Pair;
import tfar.damageenchantsmyinventory.platform.services.IPlatformHelper;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
        List<Pair<ResourceLocation, Supplier<?>>> list = SoosigsForge.registerLater.computeIfAbsent(registry, k -> new ArrayList<>());
        for (Map.Entry<String, ? extends F> entry : map.entrySet()) {
            list.add(Pair.of(Soosigs.id(entry.getKey()), entry::getValue));
        }
    }

    @Override
    public <F> void unfreeze(Registry<F> registry) {
        ((MappedRegistry<F>)registry).unfreeze();
    }
}