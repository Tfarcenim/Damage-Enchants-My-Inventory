package tfar.damageenchantsmyinventory;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.damageenchantsmyinventory.ducks.EntityDuck;
import tfar.damageenchantsmyinventory.init.ModBlocks;
import tfar.damageenchantsmyinventory.init.ModEnchantments;
import tfar.damageenchantsmyinventory.init.ModEntityTypes;
import tfar.damageenchantsmyinventory.network.PacketHandler;
import tfar.damageenchantsmyinventory.network.client.S2CEntityModData;
import tfar.damageenchantsmyinventory.platform.Services;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Items;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Stream;

// This class is part of the common project meaning it is shared between all supported loaders. Code written here can only
// import and access the vanilla codebase, libraries used by vanilla, and optionally third party libraries that provide
// common compatible binaries. This means common code can not directly use loader specific concepts such as Forge events
// however it will be compatible with all supported mod loaders.
public class DamageEnchantsMyInventory {

    public static final String MOD_ID = "damageenchantsmyinventory";
    public static final String MOD_NAME = "DamageEnchantsMyInventory";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    // The loader specific projects are able to import and use any code from the common project. This allows you to
    // write the majority of your code here and load it from your loader specific projects. This example has some
    // code that gets invoked by the entry point of the loader specific projects.
    public static void init() {
        Services.PLATFORM.registerAll(ModEnchantments.class,BuiltInRegistries.ENCHANTMENT, Enchantment.class);
        Services.PLATFORM.registerAll(ModBlocks.class,BuiltInRegistries.BLOCK, Block.class);
        Services.PLATFORM.registerAll(ModEntityTypes.class,BuiltInRegistries.ENTITY_TYPE, (Class<EntityType<?>>)(Object)EntityType.class);
        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.
        PacketHandler.registerPackets();
    }

    public static void entityTickEvent(Entity entity) {
        EntityDuck duck = EntityDuck.of(entity);
        if (entity.level().isClientSide) {
            // this.clearSoulFire();
        } else {
            if (duck.displayInfernalFlame()) {
                if (duck.getInfernalFireTicks() % 20 == 0) {
                    entity.hurt(entity.damageSources().onFire(), 1.0F);
                    duck.setInfernalFireTicks(20);
                }
                duck.setInfernalFireTicks(duck.getInfernalFireTicks() - 1);
           /* if (this.getTicksFrozen() > 0) {
                this.setTicksFrozen(0);
                self.level().levelEvent(null, LevelEvent.SOUND_EXTINGUISH_FIRE, this.blockPosition, 1);
            }*/
            }
            duck.modifyData(EntityModData.TICK);
        }

        if (!entity.level().isClientSide) {
            Services.PLATFORM.sendToTrackingClients(new S2CEntityModData(entity, duck.getModData()), entity);
            //      this.setFlagOnSoulFire(this.infernalFireTicks > 0);
        }
    }

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID,path);
    }

    public static Stream<Block> getKnownBlocks() {
        return getKnown(BuiltInRegistries.BLOCK);
    }
    public static Stream<Item> getKnownItems() {
        return getKnown(BuiltInRegistries.ITEM);
    }
    public static Stream<Enchantment> getKnownEnchantments() {
        return getKnown(BuiltInRegistries.ENCHANTMENT);
    }

    public static <V> Stream<V> getKnown(Registry<V> registry) {
        return registry.stream().filter(o -> registry.getKey(o).getNamespace().equals(MOD_ID));
    }
}