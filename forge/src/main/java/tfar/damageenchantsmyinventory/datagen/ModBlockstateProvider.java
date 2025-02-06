package tfar.damageenchantsmyinventory.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.FireBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import tfar.damageenchantsmyinventory.DamageEnchantsMyInventory;
import tfar.damageenchantsmyinventory.init.ModBlocks;

public class ModBlockstateProvider extends BlockStateProvider {
    public ModBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, DamageEnchantsMyInventory.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {


        getMultipartBuilder(ModBlocks.INFERNAL_FIRE)
                .part().modelFile(models().withExistingParent("fire", mcLoc("block/template_fire_floor"))
                        .texture("fire",modLoc("block/infernal_fire_0"))).addModel()
                .condition(FireBlock.NORTH,false)
                .condition(FireBlock.EAST,false)
                .condition(FireBlock.SOUTH,false)
                .condition(FireBlock.WEST,false)
                .condition(FireBlock.UP,false)
                .end()

        ;
    }
}
