package tfar.damageenchantsmyinventory.init;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.phys.Vec2;
import tfar.damageenchantsmyinventory.entity.AreaEffectCloudOwnerImmune;
import tfar.damageenchantsmyinventory.DEMIConfig;
import tfar.damageenchantsmyinventory.EntityModData;
import tfar.damageenchantsmyinventory.SimpleEnchantment;
import tfar.damageenchantsmyinventory.ducks.EntityDuck;
import tfar.damageenchantsmyinventory.entity.ClonePlayerEntity;

public class ModEnchantments {
    public static final Enchantment INFERNAL_FLAME = SimpleEnchantment.Properties
            .builder(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND)
            .range(SimpleEnchantment.SILK_TOUCH_RANGE)
            .minCost(SimpleEnchantment.SILK_TOUCH_MIN)
            .postAttack((attacker, target, enchantmentLevel) -> EntityDuck.of(target).modifyData(EntityModData.INFERNAL_FIRE,true))
            .build();

    public static final Enchantment LIFE_LEECH = SimpleEnchantment.Properties
            .builder(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND)
            .range(SimpleEnchantment.SILK_TOUCH_RANGE)
            .minCost(SimpleEnchantment.SILK_TOUCH_MIN)
            .build();

    public static final Enchantment SHADOW_BLINK = SimpleEnchantment.Properties
            .builder(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_FEET, EquipmentSlot.FEET,EquipmentSlot.LEGS)
            .range(SimpleEnchantment.SILK_TOUCH_RANGE)
            .minCost(SimpleEnchantment.SILK_TOUCH_MIN)
            .postHurt((user, attacker, level) -> EntityDuck.of(user).modifyData(EntityModData.BLINK_TIMER,40))
            .build();

    public static final Enchantment TOXIC_CLOUD = SimpleEnchantment.Properties
            .builder(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_HEAD, EquipmentSlot.HEAD)
            .range(SimpleEnchantment.SILK_TOUCH_RANGE)
            .minCost(SimpleEnchantment.SILK_TOUCH_MIN)
            .postHurt((user, attacker, level) -> {
                if (user.getRandom().nextDouble() < DEMIConfig.poison_cloud_chance) {
                    AreaEffectCloud areaEffectCloud = new AreaEffectCloudOwnerImmune(user.level(),user.getX(),user.getY(),user.getZ());
                    areaEffectCloud.setPotion(Potions.POISON);
                    areaEffectCloud.setOwner(user);
                    user.level().addFreshEntity(areaEffectCloud);
                }
            })
            .build();

    public static final Enchantment VOLATILE_HARVEST = SimpleEnchantment.Properties
            .builder(Enchantment.Rarity.RARE, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND)
            .range(SimpleEnchantment.SILK_TOUCH_RANGE)
            .minCost(SimpleEnchantment.SILK_TOUCH_MIN)
            .build();

    public static final Enchantment ARCHERS_EYE = SimpleEnchantment.Properties
            .builder(Enchantment.Rarity.RARE, EnchantmentCategory.BOW, EquipmentSlot.MAINHAND)
            .range(SimpleEnchantment.SILK_TOUCH_RANGE)
            .minCost(SimpleEnchantment.SILK_TOUCH_MIN)
            .build();

    public static final Enchantment PHANTOM_STALKER = SimpleEnchantment.Properties
            .builder(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR_LEGS, EquipmentSlot.LEGS,EquipmentSlot.CHEST)
            .range(SimpleEnchantment.SILK_TOUCH_RANGE)
            .minCost(SimpleEnchantment.SILK_TOUCH_MIN)
            .postHurt((user, attacker, level) -> {
                if (user instanceof Player playerUser && attacker instanceof Player) {
                    ((ServerLevel)playerUser.level()).sendParticles(ParticleTypes.POOF,user.getX(),user.getY(),user.getZ(),5000,2,2,2,0);
                    ClonePlayerEntity clone = ModEntityTypes.CLONE_PLAYER.spawn((ServerLevel) user.level(),user.blockPosition(), MobSpawnType.EVENT);
                    if (clone != null) {
                        clone.setClone(playerUser.getGameProfile());
                        clone.setOwnerUUID(playerUser.getUUID());


                        for (EquipmentSlot slot : EquipmentSlot.values()) {
                            clone.setItemSlot(slot,playerUser.getItemBySlot(slot));
                        }

                        double angle = playerUser.getRandom().nextDouble() * 360;
                        int r = 64;
                        Vec2 vec2 = new Vec2((float) (playerUser.getX() + Mth.sin((float) (angle * Math.PI / 180)) * r),
                                (float) (playerUser.getZ() + Mth.cos((float) (angle * Math.PI / 180))) * r);
                        clone.getNavigation().moveTo(vec2.x,playerUser.getY(),vec2.y,2);
                        clone.setSprinting(true);
                        clone.setCustomName(playerUser.getDisplayName());
                    }
                }
            })
            .build();

    public static final Enchantment CURSED_MIRROR = SimpleEnchantment.Properties
            .builder(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND)
            .range(SimpleEnchantment.SILK_TOUCH_RANGE)
            .minCost(SimpleEnchantment.SILK_TOUCH_MIN)
            .postAttack((attacker, target, enchantmentLevel) -> {
                if (target instanceof Player playerTarget && ((Player) target).getRandom().nextDouble() < DEMIConfig.invert_controls_chance) {
                    playerTarget.addEffect(new MobEffectInstance(ModMobEffects.INVERTED_CONTROLS,10*20,0,false,false));
                }
            })
            .build();

    public static final Enchantment HOWLING_ECHO = SimpleEnchantment.Properties
            .builder(Enchantment.Rarity.RARE, EnchantmentCategory.BOW, EquipmentSlot.MAINHAND)
            .range(SimpleEnchantment.SILK_TOUCH_RANGE)
            .minCost(SimpleEnchantment.SILK_TOUCH_MIN)
            .build();

    public static final Enchantment POLYMORPH_TOUCH = SimpleEnchantment.Properties
            .builder(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND)
            .range(SimpleEnchantment.SILK_TOUCH_RANGE)
            .minCost(SimpleEnchantment.SILK_TOUCH_MIN)
            .postAttack((attacker, target, enchantmentLevel) -> {
                if (target instanceof LivingEntity entity) {
                    entity.addEffect(new MobEffectInstance(ModMobEffects.POLYMORPH,6 * 20,0,false,false));
                }
            })
            .build();

    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    public static final Enchantment BUTTERFINGERS = SimpleEnchantment.Properties
            .builder(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND)
            .range(SimpleEnchantment.SILK_TOUCH_RANGE)
            .minCost(SimpleEnchantment.SILK_TOUCH_MIN)
            .setCurse()
            .build();

    public static final Enchantment MISFIRE = SimpleEnchantment.Properties
            .builder(Enchantment.Rarity.RARE, ModEnchantmentCategories.BOW_AND_CROSSBOW, EquipmentSlot.MAINHAND)
            .range(SimpleEnchantment.SILK_TOUCH_RANGE)
            .minCost(SimpleEnchantment.SILK_TOUCH_MIN)
            .setCurse()
            .build();

    public static final Enchantment OVERFLOW = SimpleEnchantment.Properties
            .builder(Enchantment.Rarity.RARE, ModEnchantmentCategories.BUCKET, EquipmentSlot.MAINHAND)
            .range(SimpleEnchantment.SILK_TOUCH_RANGE)
            .minCost(SimpleEnchantment.SILK_TOUCH_MIN)
            .setCurse()
            .build();

    public static final Enchantment INVENTORY_LOCK = SimpleEnchantment.Properties
            .builder(Enchantment.Rarity.RARE, ModEnchantmentCategories.ANY, EquipmentSlot.MAINHAND)
            .range(SimpleEnchantment.SILK_TOUCH_RANGE)
            .minCost(SimpleEnchantment.SILK_TOUCH_MIN)
            .setCurse()
            .build();

    public static final Enchantment DUPLICATION = SimpleEnchantment.Properties
            .builder(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND)
            .range(SimpleEnchantment.SILK_TOUCH_RANGE)
            .minCost(SimpleEnchantment.SILK_TOUCH_MIN)
            .build();

    public static final Enchantment RANDOM_DROPS = SimpleEnchantment.Properties
            .builder(Enchantment.Rarity.RARE, EnchantmentCategory.DIGGER, EquipmentSlot.MAINHAND)
            .range(SimpleEnchantment.SILK_TOUCH_RANGE)
            .minCost(SimpleEnchantment.SILK_TOUCH_MIN)
            .build();

    //1. Butterfingers
    //Gear Slot: Weapon (Sword/Axe, Bow) or Off-Hand
    //Effect: Periodically, there’s a chance that whenever the hunter tries to attack or block, their weapon slips out of their hand and drops on the ground.
    //2. Misfire Bow
    //Gear Slot: Bow/Crossbow
    //Effect: Every so often, when the hunter tries to shoot an arrow, the arrow spawns behind them or fires in a random direction.
    //3. Overflow
    //Gear Slot: Bucket
    //Effect: Every so often, when a hunter has a bucket full of water or lava in their inventory it will automatically place the contents of the bucket where the hunter is looking
    //4. Inventory Lock
    //Gear Slot: Any Item
    //Effect: The item cannot be dropped from the players inventory

    //1. Duplication Enchant
    //Gear Slot: Weapon (Sword, Axe, or Bow)
    //Effect: When you kill a mob with this enchanted weapon, that mob drops its normal loot but also spawns two new copies of itself at the location where it died.
    //2. Random Drops
    //Gear Slot: Tool (Pickaxe, Axe, Shovel, etc.)
    //Effect: Whenever you break a block with this enchanted tool, the resulting drop is replaced with a completely random item from the game’s loot table.

}
