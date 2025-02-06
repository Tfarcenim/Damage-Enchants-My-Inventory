package tfar.damageenchantsmyinventory.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import tfar.damageenchantsmyinventory.DamageEnchantsMyInventory;
import tfar.damageenchantsmyinventory.init.ModEnchantments;
import tfar.damageenchantsmyinventory.init.ModTags;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class EnchantmentTagsProvider extends TagsProvider<Enchantment> {
    protected EnchantmentTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, Registries.ENCHANTMENT, pLookupProvider, DamageEnchantsMyInventory.MOD_ID, existingFileHelper);
    }

    protected EnchantmentTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, CompletableFuture<TagLookup<Enchantment>> pParentProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, Registries.ENCHANTMENT, pLookupProvider, pParentProvider, DamageEnchantsMyInventory.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        Set<Enchantment> vanilla = new HashSet<>();
        for (Enchantment enchantment : BuiltInRegistries.ENCHANTMENT) {
            if (BuiltInRegistries.ENCHANTMENT.getKey(enchantment).getNamespace().equals("minecraft")) {
                vanilla.add(enchantment);
            }
        }
        tag(ModTags.Enchantments.NEUTRAL).add(vanilla.stream().map(EnchantmentTagsProvider::getKey).toArray(ResourceKey[]::new));
        tag(ModTags.Enchantments.HUNTER).addTag(ModTags.Enchantments.NEUTRAL);
        tag(ModTags.Enchantments.RUNNER).addTag(ModTags.Enchantments.NEUTRAL).add(getKey(ModEnchantments.INFERNAL_FLAME));
    }

    static ResourceKey<Enchantment> getKey(Enchantment enchantment) {
        return BuiltInRegistries.ENCHANTMENT.getResourceKey(enchantment).get();
    }
}
