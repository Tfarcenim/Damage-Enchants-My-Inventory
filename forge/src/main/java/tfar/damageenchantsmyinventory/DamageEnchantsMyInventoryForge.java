package tfar.damageenchantsmyinventory;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import tfar.damageenchantsmyinventory.datagen.ModDatagen;
import tfar.damageenchantsmyinventory.ducks.PlayerDuck;
import tfar.damageenchantsmyinventory.init.ModTags;

import java.util.ArrayList;
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
        bus.addListener(ModDatagen::gather);
        // Use Forge to bootstrap the Common mod.
        DamageEnchantsMyInventory.LOG.info("Hello Forge world!");
        DamageEnchantsMyInventory.init();
        MinecraftForge.EVENT_BUS.addListener(this::commands);
        MinecraftForge.EVENT_BUS.addListener(this::damage);
    }

    void damage(LivingDamageEvent event) {
        LivingEntity living = event.getEntity();
        DamageSource source = event.getSource();
        if (/*source.getEntity() instanceof Player playerAttacker && */living instanceof Player playerTarget) {
            boolean isAttackerRunner = false;//PlayerDuck.of(playerAttacker).isRunner();
            boolean isTargetRunner = PlayerDuck.of(playerTarget).isRunner();
            if (isAttackerRunner != isTargetRunner) {
                if (isTargetRunner) {
                    enchantRandomItem(playerTarget);
                } else {
                    if (ModData.getOrCreateDefaultInstance(living.getServer()).huntersGainEnchantments) {
                        enchantRandomItem(playerTarget);
                    }
                }
            }
        }
    }

    static void enchantRandomItem(Player player) {
        IntList candidates = new IntArrayList();
        boolean isRunner = PlayerDuck.of(player).isRunner();
        for (int i = 0; i < player.getInventory().getContainerSize();i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty()) {
                for (Enchantment enchantment : BuiltInRegistries.ENCHANTMENT) {
                    if (isEligible(stack,enchantment,isRunner)) {
                            candidates.add(i);
                            break;
                    }
                }
            }
        }
        if (candidates.isEmpty()) return;
        int choose = candidates.getInt(player.getRandom().nextInt(candidates.size()));
        ItemStack stack = player.getInventory().getItem(choose);
        List<Enchantment> possible = getPossibleEnchantments(stack,player.level(),isRunner);
        if (!possible.isEmpty()) {
            Enchantment randomEnchant = possible.get(player.getRandom().nextInt(possible.size()));
            int existingLevel = stack.getEnchantmentLevel(randomEnchant);
            stack.enchant(randomEnchant,existingLevel +1);
        }
    }

    public static boolean isEligible(ItemStack stack,Enchantment enchantment,boolean isRunner) {
        if (enchantment.canEnchant(stack) && TagUtil.isIn(isRunner ? ModTags.Enchantments.RUNNER : ModTags.Enchantments.HUNTER,enchantment)) {
            Map<Enchantment, Integer> allEnchantments = stack.getAllEnchantments();
            for (Enchantment existing : allEnchantments.keySet()) {
                if (enchantment != existing && !enchantment.isCompatibleWith(existing)) {
                    return false;
                }
            }
            int level1 = stack.getEnchantmentLevel(enchantment);
            return level1 < enchantment.getMaxLevel();
        }
        return false;
    }

    static List<Enchantment> getPossibleEnchantments(ItemStack stack, Level level, boolean isRunner) {
        List<Enchantment> enchantments = new ArrayList<>();
        for (Enchantment enchantment : BuiltInRegistries.ENCHANTMENT) {
            if(isEligible(stack,enchantment,isRunner)) {
                enchantments.add(enchantment);
            }
        }
        return enchantments;
    }

    void commands(RegisterCommandsEvent event){
        ModCommands.register(event.getDispatcher());
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