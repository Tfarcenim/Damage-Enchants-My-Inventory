package tfar.damageenchantsmyinventory.init;

import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import tfar.damageenchantsmyinventory.platform.Services;

public class ModEnchantmentCategories {
    public static final EnchantmentCategory ANY = Services.PLATFORM.create("any",item -> true);
    public static final EnchantmentCategory BUCKET = Services.PLATFORM.create("bucket",item -> item instanceof BucketItem);
    public static final EnchantmentCategory BOW_AND_CROSSBOW = Services.PLATFORM.create("bow_and_crossbow",item -> item instanceof BowItem ||item instanceof CrossbowItem);
    public static final EnchantmentCategory SWORD_AND_AXE = Services.PLATFORM.create("sword_and_axe",item -> item instanceof SwordItem || item instanceof AxeItem);
    public static final EnchantmentCategory WEAPON_BOW_AND_CROSSBOW = Services.PLATFORM.create("weapon_bow_and_crossbow",item -> EnchantmentCategory.WEAPON.canEnchant(item) || BOW_AND_CROSSBOW.canEnchant(item));
    public static final EnchantmentCategory SWORD_AXE_BOW_CROSSBOW_AND_SHIELD = Services.PLATFORM.create("sword_axe_bow_crossbow_and_shield", item -> item instanceof ShieldItem || SWORD_AND_AXE.canEnchant(item) || BOW_AND_CROSSBOW.canEnchant(item));

}
