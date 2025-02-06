package tfar.damageenchantsmyinventory.init;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import tfar.damageenchantsmyinventory.entity.SmallTnt;

public class ModEntityTypes {
    public static final EntityType<SmallTnt> SMALL_TNT = EntityType.Builder.<SmallTnt>of(SmallTnt::new, MobCategory.MISC).fireImmune().sized(0.49F, 0.49F)
            .clientTrackingRange(10).updateInterval(10).build("");
}
