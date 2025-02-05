package tfar.damageenchantsmyinventory;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@Mod(DamageEnchantsMyInventory.MOD_ID)
public class DamageEnchantsMyInventoryForge {

    public static Map<Registry<?>, List<Pair<ResourceLocation, Supplier<?>>>> registerLater = new HashMap<>();

    public DamageEnchantsMyInventoryForge() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        // This method is invoked by the Forge mod loader when it is ready
        // to load your mod. You can access Forge and Common code in this
        // project.
        bus.addListener(this::registerObjs);
        bus.addListener(this::setup);
        // Use Forge to bootstrap the Common mod.
        DamageEnchantsMyInventory.LOG.info("Hello Forge world!");
        DamageEnchantsMyInventory.init();
        
    }

    public void registerObjs(RegisterEvent event) {
        Registry<?> registry =event.getVanillaRegistry();
        List<Pair<ResourceLocation, Supplier<?>>> list = registerLater.get(registry);

        if (list != null) {
            for (Pair<ResourceLocation,Supplier<?>> pair : list) {
                event.register((ResourceKey<? extends Registry<Object>>)registry.key(),pair.getLeft(),(Supplier<Object>)pair.getValue());
            }
        }
        //  event.register(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Soosigs.id("add_item_chance"),() -> AddItemChanceLootModifier.CODEC);
    }

    void setup(FMLCommonSetupEvent event) {
        registerLater.clear();
    }

}