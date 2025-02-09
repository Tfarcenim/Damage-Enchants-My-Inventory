package tfar.damageenchantsmyinventory.init;

import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import tfar.damageenchantsmyinventory.platform.Services;

public class ModEnchantmentCategories {
    public static final EnchantmentCategory ANY = Services.PLATFORM.create("any",item -> true);
    public static final EnchantmentCategory BUCKET = Services.PLATFORM.create("bucket",item -> item instanceof BucketItem);
    public static final EnchantmentCategory BOW_AND_CROSSBOW = Services.PLATFORM.create("bow_and_crossbow",item -> item instanceof BowItem ||item instanceof CrossbowItem);
    public static final EnchantmentCategory WEAPON_BOW_AND_CROSSBOW = Services.PLATFORM.create("weapon_bow_and_crossbow",item -> EnchantmentCategory.WEAPON.canEnchant(item) || BOW_AND_CROSSBOW.canEnchant(item));
    public static final EnchantmentCategory WEAPON_BOW_CROSSBOW_AND_SHIELD = Services.PLATFORM.create("weapon_bow_crossbow_and_shield",item -> item instanceof ShieldItem || WEAPON_BOW_AND_CROSSBOW.canEnchant(item));

}
