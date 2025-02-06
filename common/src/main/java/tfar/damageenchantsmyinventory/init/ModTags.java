package tfar.damageenchantsmyinventory.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import tfar.damageenchantsmyinventory.DamageEnchantsMyInventory;

public class ModTags {
    public static class Enchantments{

        public static final TagKey<Enchantment> HUNTER = modTag("hunter");
        public static final TagKey<Enchantment> NEUTRAL = modTag("neutral");
        public static final TagKey<Enchantment> RUNNER = modTag("runner");

        static TagKey<Enchantment> modTag(String path) {
            return TagKey.create(Registries.ENCHANTMENT, DamageEnchantsMyInventory.id(path));
        }
    }

}
