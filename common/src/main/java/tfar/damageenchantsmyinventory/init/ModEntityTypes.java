package tfar.damageenchantsmyinventory.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import tfar.damageenchantsmyinventory.entity.ClonePlayerEntity;
import tfar.damageenchantsmyinventory.entity.SmallTntEntity;

public class ModEntityTypes {
    public static final EntityType<SmallTntEntity> SMALL_TNT = EntityType.Builder.<SmallTntEntity>of(SmallTntEntity::new, MobCategory.MISC).fireImmune().sized(0.49F, 0.49F)
            .clientTrackingRange(10).updateInterval(10).build("");

    public static final EntityType<ClonePlayerEntity> CLONE_PLAYER = EntityType.Builder.of(ClonePlayerEntity::new, MobCategory.MISC).sized(.8f,1.8f)
            .clientTrackingRange(32).updateInterval(2).build("");

}
