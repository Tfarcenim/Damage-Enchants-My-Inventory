package tfar.damageenchantsmyinventory;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.lang3.tuple.Pair;
import tfar.damageenchantsmyinventory.client.ModClientForge;
import tfar.damageenchantsmyinventory.datagen.ModDatagen;
import tfar.damageenchantsmyinventory.ducks.EntityDuck;
import tfar.damageenchantsmyinventory.ducks.PlayerDuck;
import tfar.damageenchantsmyinventory.entity.ClonePlayerEntity;
import tfar.damageenchantsmyinventory.entity.SmallTntEntity;
import tfar.damageenchantsmyinventory.init.ModEnchantments;
import tfar.damageenchantsmyinventory.init.ModEntityTypes;
import tfar.damageenchantsmyinventory.init.ModMobEffects;
import tfar.damageenchantsmyinventory.init.ModTags;
import tfar.damageenchantsmyinventory.mobeffect.PhantomNoisesEffect;

import java.util.*;
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
        bus.addListener(this::attributes);
        if (FMLEnvironment.dist.isClient()) {
            ModClientForge.init(bus);
        }
        // Use Forge to bootstrap the Common mod.
        DamageEnchantsMyInventory.init();
        MinecraftForge.EVENT_BUS.addListener(this::commands);
        MinecraftForge.EVENT_BUS.addListener(this::damage);
        MinecraftForge.EVENT_BUS.addListener(this::blockBreak);
    }

    void preventCuring(MobEffectEvent
                       ) {

    }

    void damage(LivingDamageEvent event) {
        LivingEntity living = event.getEntity();
        DamageSource source = event.getSource();
        float amount = event.getAmount();

        if (source.getEntity() instanceof LivingEntity livingAttacker) {
            if (livingAttacker.getMainHandItem().getEnchantmentLevel(ModEnchantments.LIFE_LEECH) > 0) {
                livingAttacker.heal((float) (amount * DEMIConfig.life_leech_amount));
            }

            if (livingAttacker.getMainHandItem().getEnchantmentLevel(ModEnchantments.HOWLING_ECHO) > 0) {
                living.addEffect(new MobEffectInstance(ModMobEffects.PHANTOM_NOISES,40 * 20,0,false,false));
            }

            if (livingAttacker.getMainHandItem().getEnchantmentLevel(ModEnchantments.ARCHERS_EYE) > 0) {
                living.addEffect(new MobEffectInstance(MobEffects.GLOWING,1000,0,false,false));
                if (EntityDuck.of(living).getModData().weakToNextArrow()) {
                    event.setAmount(event.getAmount() *2);
                    EntityDuck.of(living).modifyData(EntityModData.WEAK_TO_NEXT_ARROW,false);
                } else {
                    EntityDuck.of(living).modifyData(EntityModData.WEAK_TO_NEXT_ARROW,true);
                }
            }
        }

        if (source.getEntity() instanceof Player playerAttacker && living instanceof Player playerTarget) {
            boolean isAttackerRunner = PlayerDuck.of(playerAttacker).isRunner();
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

    void blockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();
        BlockPos pos = event.getPos();
        Level level = (Level) event.getLevel();
        ItemStack stack = player.getMainHandItem();
        if (stack.getEnchantmentLevel(ModEnchantments.VOLATILE_HARVEST) > 0 && player.getRandom().nextDouble() < DEMIConfig.volatile_harvest_chance ) {
            SmallTntEntity smallTntEntity = new SmallTntEntity(level,pos.getX()+.5,pos.getY(),pos.getZ()+.5,player);
            level.addFreshEntity(smallTntEntity);
        }
    }

    void attributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.CLONE_PLAYER, ClonePlayerEntity.createAttributes().build());
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
        PhantomNoisesEffect.setup();
    }

}