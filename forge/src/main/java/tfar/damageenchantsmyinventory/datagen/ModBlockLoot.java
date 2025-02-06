package tfar.damageenchantsmyinventory.datagen;

import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;
import tfar.damageenchantsmyinventory.init.ModBlocks;

import java.util.List;

public class ModBlockLoot extends VanillaBlockLoot {

    @Override
    protected void generate() {
        add(ModBlocks.INFERNAL_FIRE,noDrop());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return List.of(ModBlocks.INFERNAL_FIRE);
    }
}
