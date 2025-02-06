package tfar.damageenchantsmyinventory.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.FireBlock;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import tfar.damageenchantsmyinventory.DamageEnchantsMyInventory;
import tfar.damageenchantsmyinventory.init.ModBlocks;

public class ModBlockstateProvider extends BlockStateProvider {
    public ModBlockstateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, DamageEnchantsMyInventory.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        //          "model": "minecraft:block/fire_side0"
        //          "model": "minecraft:block/fire_side1"
        //          "model": "minecraft:block/fire_side_alt0"
        //          "model": "minecraft:block/fire_side_alt1"

        ResourceLocation fire_0 = modLoc("block/infernal_fire_0");
        ResourceLocation fire_1 = modLoc("block/infernal_fire_1");

        ModelFile fire_side0 = models().withExistingParent("infernal_fire_side0", mcLoc("block/template_fire_side"))
                .texture("fire",fire_0);

        ModelFile fire_side_alt0 = models().withExistingParent("infernal_fire_side_alt0", mcLoc("block/template_fire_side_alt"))
                .texture("fire",fire_0);

        ModelFile fire_side1 = models().withExistingParent("infernal_fire_side1", mcLoc("block/template_fire_side"))
                .texture("fire",fire_1);

        ModelFile fire_side_alt1 = models().withExistingParent("infernal_fire_side_alt1", mcLoc("block/template_fire_side_alt"))
                .texture("fire",fire_1);

        getMultipartBuilder(ModBlocks.INFERNAL_FIRE)
                .part().modelFile(models().withExistingParent("infernal_fire_floor0", mcLoc("block/template_fire_floor"))
                        .texture("fire",fire_0))
                .nextModel()
                .modelFile(models().withExistingParent("infernal_fire_floor1", mcLoc("block/template_fire_floor"))
                        .texture("fire",fire_1)).addModel()
                .condition(FireBlock.NORTH,false)
                .condition(FireBlock.EAST,false)
                .condition(FireBlock.SOUTH,false)
                .condition(FireBlock.WEST,false)
                .condition(FireBlock.UP,false)
                .end()
                .part().modelFile(fire_side0).nextModel().modelFile(fire_side_alt0)
                .nextModel().modelFile(fire_side1).nextModel().modelFile(fire_side_alt1)
                .addModel()
                .useOr()
                .nestedGroup()
                .condition(FireBlock.NORTH,false)
                .condition(FireBlock.EAST,false)
                .condition(FireBlock.SOUTH,false)
                .condition(FireBlock.WEST,false)
                .condition(FireBlock.UP,false)
                .end()
                .nestedGroup()
                .condition(FireBlock.NORTH,true)
                .end()
                .end()
                .part().modelFile(fire_side0).rotationY(90).nextModel().modelFile(fire_side_alt0).rotationY(90)
                .nextModel().modelFile(fire_side1).rotationY(90).nextModel().modelFile(fire_side_alt1).rotationY(90)
                .addModel()
                .useOr()
                .nestedGroup()
                .condition(FireBlock.NORTH,false)
                .condition(FireBlock.EAST,false)
                .condition(FireBlock.SOUTH,false)
                .condition(FireBlock.WEST,false)
                .condition(FireBlock.UP,false)
                .end()
                .nestedGroup()
                .condition(FireBlock.EAST,true)
                .end()
                .end()
                .part().modelFile(fire_side0).rotationY(180).nextModel().modelFile(fire_side_alt0).rotationY(180)
                .nextModel().modelFile(fire_side1).rotationY(180).nextModel().modelFile(fire_side_alt1).rotationY(180)
                .addModel()
                .useOr()
                .nestedGroup()
                .condition(FireBlock.NORTH,false)
                .condition(FireBlock.EAST,false)
                .condition(FireBlock.SOUTH,false)
                .condition(FireBlock.WEST,false)
                .condition(FireBlock.UP,false)
                .end()
                .nestedGroup()
                .condition(FireBlock.SOUTH,true)
                .end()
                .end()
                .part().modelFile(fire_side0).rotationY(270).nextModel().modelFile(fire_side_alt0).rotationY(270)
                .nextModel().modelFile(fire_side1).rotationY(270).nextModel().modelFile(fire_side_alt1).rotationY(270)
                .addModel()
                .useOr()
                .nestedGroup()
                .condition(FireBlock.NORTH,false)
                .condition(FireBlock.EAST,false)
                .condition(FireBlock.SOUTH,false)
                .condition(FireBlock.WEST,false)
                .condition(FireBlock.UP,false)
                .end()
                .nestedGroup()
                .condition(FireBlock.WEST,true)
                .end()
                .end()
                .part().modelFile(models().withExistingParent("infernal_fire_up0", mcLoc("block/template_fire_up"))
                        .texture("fire",fire_0))
                .nextModel()
                .modelFile(models().withExistingParent("infernal_fire_up_alt0", mcLoc("block/template_fire_up_alt"))
                        .texture("fire",fire_0)).nextModel()
                .modelFile(models().withExistingParent("infernal_fire_up1", mcLoc("block/template_fire_up"))
                        .texture("fire",fire_1))
                .nextModel()
                .modelFile(models().withExistingParent("infernal_fire_up_alt1", mcLoc("block/template_fire_up_alt"))
                        .texture("fire",fire_1)).addModel()
                .condition(FireBlock.UP,true)
                .end()
        ;
    }
}
