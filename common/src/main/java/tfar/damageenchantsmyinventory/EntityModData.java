package tfar.damageenchantsmyinventory;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public record EntityModData(boolean infernalFire,int blinkTimer,boolean weakToNextArrow) {

    public static final Codec<EntityModData> CODEC = RecordCodecBuilder.create(
            entityModDataInstance -> entityModDataInstance.group(
                    Codec.BOOL.fieldOf("infernalFire").forGetter(EntityModData::infernalFire),
                    Codec.INT.fieldOf("blinkTimer").forGetter(EntityModData::blinkTimer),
                    Codec.BOOL.fieldOf("weakToNextArrow").forGetter(EntityModData::weakToNextArrow)
            ).apply(entityModDataInstance,EntityModData::new)

    );

    public EntityModData() {
        this(false,0,false);
    }

    public static final BiFunction<EntityModData,Boolean,EntityModData> INFERNAL_FIRE = EntityModData::withInfernalFire;
    public static final BiFunction<EntityModData,Integer,EntityModData> BLINK_TIMER = EntityModData::withBlinkTimer;
    public static final BiFunction<EntityModData,Boolean,EntityModData> WEAK_TO_NEXT_ARROW = EntityModData::setWeakToNextArrow;

    public static final UnaryOperator<EntityModData> TICK = EntityModData::tick;

    public EntityModData withInfernalFire(boolean infernalFire) {
        return new EntityModData(infernalFire,blinkTimer,weakToNextArrow);
    }

    public EntityModData withBlinkTimer(int blinkTimer) {
        return new EntityModData(infernalFire,blinkTimer,weakToNextArrow);
    }

    public EntityModData setWeakToNextArrow(boolean weakToNextArrow) {
        return new EntityModData(infernalFire,blinkTimer,weakToNextArrow);
    }

    public EntityModData tick() {
        if (blinkTimer > 0) {
            return new EntityModData(infernalFire,blinkTimer-1,weakToNextArrow);
        }
        return this;
    }

    public void toPacket(FriendlyByteBuf buf) {
        buf.writeBoolean(infernalFire);
        buf.writeInt(blinkTimer);
        buf.writeBoolean(weakToNextArrow);
    }

    public static EntityModData fromPacket(FriendlyByteBuf buf) {
        boolean infernalFire = buf.readBoolean();
        int blinkTimer = buf.readInt();
        boolean weakToNextArrow = buf.readBoolean();
        return new EntityModData(infernalFire,blinkTimer,weakToNextArrow);
    }

}
