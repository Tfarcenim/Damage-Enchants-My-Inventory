package tfar.damageenchantsmyinventory.ducks;

import net.minecraft.world.entity.Entity;
import tfar.damageenchantsmyinventory.EntityModData;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public interface EntityDuck {
    EntityModData getModData();

    void setModData(EntityModData entityModData);

    default void modifyData(UnaryOperator<EntityModData> function) {
        setModData(function.apply(getModData()));
    }

    default <T> void modifyData(BiFunction<EntityModData,T,EntityModData> function,T subData) {
        setModData(function.apply(getModData(),subData));
    }

    static EntityDuck of(Entity entity) {
        return (EntityDuck) entity;
    }
}
