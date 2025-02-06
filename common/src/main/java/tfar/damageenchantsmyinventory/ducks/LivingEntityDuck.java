package tfar.damageenchantsmyinventory.ducks;

import net.minecraft.world.entity.LivingEntity;

public interface LivingEntityDuck {

    static LivingEntityDuck of(LivingEntity livingEntity) {
        return (LivingEntityDuck) livingEntity;
    }
}
