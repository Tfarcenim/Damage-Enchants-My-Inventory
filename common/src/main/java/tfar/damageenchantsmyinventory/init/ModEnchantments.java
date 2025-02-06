package tfar.damageenchantsmyinventory.init;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
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
}
