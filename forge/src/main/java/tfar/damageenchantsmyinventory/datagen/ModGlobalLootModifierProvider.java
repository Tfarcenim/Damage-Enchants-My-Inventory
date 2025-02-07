package tfar.damageenchantsmyinventory.datagen;

import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import tfar.damageenchantsmyinventory.RandomDropsLootModifier;
import tfar.damageenchantsmyinventory.DamageEnchantsMyInventory;
import tfar.damageenchantsmyinventory.init.ModEnchantments;

import java.util.HashSet;
import java.util.Set;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(PackOutput output) {
        super(output, DamageEnchantsMyInventory.MOD_ID);
    }

    final Set<Block> tables = new HashSet<>();

    @Override
    protected void start() {
        tables.add(Blocks.GRASS);

        for (Block table : tables) {
            addModifier(table);
        }
    }

    protected static final LootItemCondition.Builder HAS_RANDOM_DROPS = MatchTool.toolMatches(ItemPredicate.Builder.item()
            .hasEnchantment(new EnchantmentPredicate(ModEnchantments.RANDOM_DROPS, MinMaxBounds.Ints.atLeast(1))));


    void addModifier(Block table) {
        ResourceLocation name = BuiltInRegistries.BLOCK.getKey(table);
        add("random_drops_" + name.getPath(), new RandomDropsLootModifier(new LootItemCondition[]{
               // LootTableIdCondition.builder(table.getLootTable()).build(),
                HAS_RANDOM_DROPS.build()
        }));
    }
}
