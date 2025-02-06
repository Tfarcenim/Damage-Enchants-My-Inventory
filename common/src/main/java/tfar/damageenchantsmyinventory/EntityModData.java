package tfar.damageenchantsmyinventory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public record EntityModData(boolean infernalFire) {

    public static final Codec<EntityModData> CODEC = RecordCodecBuilder.create(
            entityModDataInstance -> entityModDataInstance.group(Codec.BOOL.fieldOf("infernalFire").forGetter(EntityModData::infernalFire)
            ).apply(entityModDataInstance,EntityModData::new)

    );

    public EntityModData() {
        this(false);
    }

    public static final BiFunction<EntityModData,Boolean,EntityModData> INFERNAL_FIRE = EntityModData::withInfernalFire;

    public EntityModData withInfernalFire(boolean infernalFire) {
        return new EntityModData(infernalFire);
    }

    public void toPacket(FriendlyByteBuf buf) {
        buf.writeBoolean(infernalFire);
    }

    public static EntityModData fromPacket(FriendlyByteBuf buf) {
        boolean infernalFire = buf.readBoolean();
        return new EntityModData(infernalFire);
    }

}
