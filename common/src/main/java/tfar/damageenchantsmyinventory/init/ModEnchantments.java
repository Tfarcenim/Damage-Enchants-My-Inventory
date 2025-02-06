package tfar.damageenchantsmyinventory.init;

import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import tfar.damageenchantsmyinventory.entity.AreaEffectCloudOwnerImmune;
import tfar.damageenchantsmyinventory.DEMIConfig;
import tfar.damageenchantsmyinventory.EntityModData;
import tfar.damageenchantsmyinventory.SimpleEnchantment;
import tfar.damageenchantsmyinventory.ducks.EntityDuck;

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
}
