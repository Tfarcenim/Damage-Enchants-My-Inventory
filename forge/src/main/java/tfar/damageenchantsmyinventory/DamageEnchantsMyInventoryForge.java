package tfar.damageenchantsmyinventory;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
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
import tfar.damageenchantsmyinventory.mobeffect.PolymorphMobEffect;
import tfar.damageenchantsmyinventory.platform.Services;

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
        MinecraftForge.EVENT_BUS.addListener(this::leftClick);
        MinecraftForge.EVENT_BUS.addListener(this::startUsingItem);
        MinecraftForge.EVENT_BUS.addListener(this::death);
        MinecraftForge.EVENT_BUS.addListener(this::projectileDamage);
    }

    void projectileDamage(ProjectileImpactEvent event) {
        Projectile projectile = event.getProjectile();
        HitResult hitResult = event.getRayTraceResult();
        if (hitResult instanceof EntityHitResult entityHitResult) {
            Entity entity = entityHitResult.getEntity();
            if (EntityDuck.of(projectile).displayInfernalFlame()) {
                EntityDuck.of(entity).modifyData(EntityModData.INFERNAL_FIRE,true);
            }
        }

    }

    void damage(LivingDamageEvent event) {
        LivingEntity living = event.getEntity();
        DamageSource source = event.getSource();
        float amount = event.getAmount();

        if (source.getEntity() instanceof LivingEntity livingAttacker) {
            if (livingAttacker.getMainHandItem().getEnchantmentLevel(ModEnchantments.LIFE_LEECH) > 0) {
                livingAttacker.heal((float) (amount * DEMIConfig.life_leech_amount * livingAttacker.getMainHandItem().getEnchantmentLevel(ModEnchantments.LIFE_LEECH)));
            }

            if (livingAttacker.getMainHandItem().getEnchantmentLevel(ModEnchantments.HOWLING_ECHO) > 0) {
                living.addEffect(new MobEffectInstance(ModMobEffects.PHANTOM_NOISES, 40 * 20, 0, false, false));
            }

            if (livingAttacker.getMainHandItem().getEnchantmentLevel(ModEnchantments.ARCHERS_EYE) > 0) {
                living.addEffect(new MobEffectInstance(MobEffects.GLOWING, 1000, 0, false, false));
                if (EntityDuck.of(living).getModData().weakToNextArrow()) {
                    event.setAmount(event.getAmount() * 2);
                    EntityDuck.of(living).modifyData(EntityModData.WEAK_TO_NEXT_ARROW, false);
                } else {
                    EntityDuck.of(living).modifyData(EntityModData.WEAK_TO_NEXT_ARROW, true);
                }
            }
            if (livingAttacker.getMainHandItem().getEnchantmentLevel(ModEnchantments.INFERNAL_FLAME) > 0 && livingAttacker.getMainHandItem().getItem() instanceof TieredItem) {
                            EntityDuck.of(living).modifyData(EntityModData.INFERNAL_FIRE,true);
            }
        }

        if (/*source.getEntity() instanceof Player playerAttacker && */living instanceof Player playerTarget) {
            boolean isAttackerRunner = false;//PlayerDuck.of(playerAttacker).isRunner();
            boolean isTargetRunner = PlayerDuck.of(playerTarget).isRunner();
            if (isAttackerRunner != isTargetRunner) {
                if (isTargetRunner) {
                    enchantRandomItem(playerTarget);
                } else {
                    if (ModLevelData.getOrCreateDefaultInstance(living.getServer()).huntersGainEnchantments) {
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
        if (stack.getEnchantmentLevel(ModEnchantments.VOLATILE_HARVEST) > 0 && player.getRandom().nextDouble() < DEMIConfig.volatile_harvest_chance) {
            SmallTntEntity smallTntEntity = new SmallTntEntity(level, pos.getX() + .5, pos.getY(), pos.getZ() + .5, player);
            level.addFreshEntity(smallTntEntity);
        }
    }

    void leftClick(AttackEntityEvent event) {
        Player player = event.getEntity();
        ItemStack stack = player.getMainHandItem();
        if (stack.getEnchantmentLevel(ModEnchantments.BUTTERFINGERS) > 0) {
            if (player.getRandom().nextDouble() < DEMIConfig.butterfingers_chance) {
                player.drop(stack.copy(), true);
                stack.setCount(0);
            }
        }
    }

    void startUsingItem(LivingEntityUseItemEvent.Start event) {
        LivingEntity livingEntity = event.getEntity();
        ItemStack stack = event.getItem();

        if (livingEntity instanceof ServerPlayer player) {
            if (stack.getEnchantmentLevel(ModEnchantments.BUTTERFINGERS) > 0) {
                if (player.getRandom().nextDouble() < DEMIConfig.butterfingers_chance) {
                    player.drop(stack.copy(), true);
                    stack.setCount(0);
                }
            }
        }
    }

    void attributes(EntityAttributeCreationEvent event) {
        event.put(ModEntityTypes.CLONE_PLAYER, ClonePlayerEntity.createAttributes().build());
    }

    static void enchantRandomItem(Player player) {
        IntList candidates = new IntArrayList();
        boolean isRunner = PlayerDuck.of(player).isRunner();
        Holder<Enchantment> forced = null;
        if (isRunner) {
            forced = ModLevelData.getOrCreateDefaultInstance(player.getServer()).forcedRunnerEnchantment;
        }
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack stack = player.getInventory().getItem(i);
            if (!stack.isEmpty()) {
                if (forced != null) {
                    Enchantment enchantment = forced.get();
                    if (isEligible(stack, enchantment, isRunner)) {
                        candidates.add(i);
                        break;
                    }
                } else {
                    for (Enchantment enchantment : BuiltInRegistries.ENCHANTMENT) {
                        if (isEligible(stack, enchantment, isRunner)) {
                            candidates.add(i);
                            break;
                        }
                    }
                }
            }
        }
        if (candidates.isEmpty()) return;
        int choose = candidates.getInt(player.getRandom().nextInt(candidates.size()));
        ItemStack stack = player.getInventory().getItem(choose);
        List<Enchantment> possible = forced == null ? getPossibleEnchantments(stack, player.level(), isRunner) : List.of(forced.value());
        if (!possible.isEmpty()) {
            Enchantment randomEnchant = possible.get(player.getRandom().nextInt(possible.size()));
            int existingLevel = stack.getEnchantmentLevel(randomEnchant);
            cleanupDuplicates(stack,randomEnchant);
            stack.enchant(randomEnchant, existingLevel + 1);



            if (forced != null) {
                ModLevelData.getOrCreateDefaultInstance(player.getServer()).setForcedRunnerEnchantment(null);
            }
        }
    }

    static void cleanupDuplicates(ItemStack stack,Enchantment enchantment) {
        CompoundTag tag = stack.getTag();

        ListTag listtag = tag.getList("Enchantments", CompoundTag.TAG_COMPOUND);

        ResourceLocation enchantmentId = EnchantmentHelper.getEnchantmentId(enchantment);
        listtag.removeIf(tag1 -> ((CompoundTag)tag1).getString("id").equals(enchantmentId.toString()));

    }

    public static boolean isEligible(ItemStack stack, Enchantment enchantment, boolean isRunner) {
        if (enchantment.canEnchant(stack) && TagUtil.isIn(isRunner ? ModTags.Enchantments.RUNNER : ModTags.Enchantments.HUNTER, enchantment)) {
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
            if (isEligible(stack, enchantment, isRunner)) {
                enchantments.add(enchantment);
            }
        }
        return enchantments;
    }

    void commands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher(),event.getBuildContext());
    }

    void death(LivingDeathEvent event) {
        LivingEntity livingEntity = event.getEntity();
        DamageSource source = event.getSource();
        if (livingEntity instanceof Mob mob && source.getEntity() instanceof LivingEntity living
                && living.getMainHandItem().getEnchantmentLevel(ModEnchantments.DUPLICATION) > 0) {
            if (!blacklisted(mob)) {
                mob.getType().spawn((ServerLevel) livingEntity.level(),mob.blockPosition(), MobSpawnType.EVENT);
                mob.getType().spawn((ServerLevel) livingEntity.level(),mob.blockPosition(), MobSpawnType.EVENT);
            }
        }
    }

    public static boolean blacklisted(Mob mob) {
        boolean b = mob instanceof EnderDragon || mob.getParts() != null;
        return b;
    }

    public static void onItemTick(ItemStack stack, Level level, Entity entity, int slot, boolean equipped) {
        if (stack.getItem() instanceof BucketItem bucketItem && bucketItem.getFluid() != Fluids.EMPTY && entity instanceof ServerPlayer player) {
            if (player.getRandom().nextDouble() < DEMIConfig.overflow_chance && stack.getEnchantmentLevel(ModEnchantments.OVERFLOW) > 0) {
                BlockHitResult blockHitResult = (BlockHitResult) player.pick(player.getAttributeValue(ForgeMod.BLOCK_REACH.get()),1,false);
                if (blockHitResult.getType() != HitResult.Type.MISS) {
                    BlockPos pos = blockHitResult.getBlockPos();
                    boolean b = bucketItem.emptyContents(player, level, blockHitResult.getBlockPos(), blockHitResult, stack);
                    if (b) {
                        bucketItem.checkExtraContent(player, level, stack, pos);
                        CriteriaTriggers.PLACED_BLOCK.trigger(player, pos, stack);
                        player.awardStat(Stats.ITEM_USED.get(bucketItem));
                        ItemStack stack1 = BucketItem.getEmptySuccessItem(stack,player);
                        player.getInventory().setItem(slot, stack1);
                    }
                }
            }
        }
    }

    public void registerObjs(RegisterEvent event) {
        Registry<?> registry = event.getVanillaRegistry();
        List<Pair<ResourceLocation, Supplier<?>>> list = registerLater.get(registry);

        if (list != null) {
            for (Pair<ResourceLocation, Supplier<?>> pair : list) {
                event.register((ResourceKey<? extends Registry<Object>>) registry.key(), pair.getLeft(), (Supplier<Object>) pair.getValue());
            }
        }
        event.register(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, DamageEnchantsMyInventory.id("random_drops"),() -> RandomDropsLootModifier.CODEC);
    }

    void setup(FMLCommonSetupEvent event) {
        registerLater.clear();
        PhantomNoisesEffect.setup();
        Collections.addAll(PolymorphMobEffect.TYPES, EntityType.RABBIT, EntityType.COW, EntityType.PIG, EntityType.SHEEP);
    }

}