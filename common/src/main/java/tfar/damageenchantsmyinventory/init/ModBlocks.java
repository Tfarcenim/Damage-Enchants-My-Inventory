package tfar.damageenchantsmyinventory.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.SoulFireBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import tfar.damageenchantsmyinventory.InfernalFireBlock;

public class ModBlocks {
    public static final Block INFERNAL_FIRE =  new InfernalFireBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).replaceable().noCollission().instabreak().lightLevel((p_152605_) -> 10).sound(SoundType.WOOL).pushReaction(PushReaction.DESTROY));
}
