package tfar.damageenchantsmyinventory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public record EntityModData(boolean infernalFire,int blinkTimer) {

    public static final Codec<EntityModData> CODEC = RecordCodecBuilder.create(
            entityModDataInstance -> entityModDataInstance.group(
                    Codec.BOOL.fieldOf("infernalFire").forGetter(EntityModData::infernalFire),
                    Codec.INT.fieldOf("blinkTimer").forGetter(EntityModData::blinkTimer)
            ).apply(entityModDataInstance,EntityModData::new)

    );

    public EntityModData() {
        this(false,0);
    }

    public static final BiFunction<EntityModData,Boolean,EntityModData> INFERNAL_FIRE = EntityModData::withInfernalFire;
    public static final BiFunction<EntityModData,Integer,EntityModData> BLINK_TIMER = EntityModData::withBlinkTimer;

    public static final UnaryOperator<EntityModData> TICK = EntityModData::tick;

    public EntityModData withInfernalFire(boolean infernalFire) {
        return new EntityModData(infernalFire,blinkTimer);
    }

    public EntityModData withBlinkTimer(int blinkTimer) {
        return new EntityModData(infernalFire,blinkTimer);
    }

    public EntityModData tick() {
        if (blinkTimer > 0) {
            return new EntityModData(infernalFire,blinkTimer-1);
        }
        return this;
    }

    public void toPacket(FriendlyByteBuf buf) {
        buf.writeBoolean(infernalFire);
        buf.writeInt(blinkTimer);
    }

    public static EntityModData fromPacket(FriendlyByteBuf buf) {
        boolean infernalFire = buf.readBoolean();
        int blinkTimer = buf.readInt();
        return new EntityModData(infernalFire,blinkTimer);
    }

}
