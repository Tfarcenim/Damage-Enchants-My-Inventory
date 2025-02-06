package tfar.damageenchantsmyinventory.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public class ModBlocks {
    public static final Block INFERNAL_FIRE =  new FireBlock(BlockBehaviour.Properties.of().mapColor(MapColor.FIRE).replaceable().noCollission()
            .instabreak().lightLevel((p_152607_) -> 15).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY));
}
