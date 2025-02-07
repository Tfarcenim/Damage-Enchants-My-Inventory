package tfar.damageenchantsmyinventory.mobeffect;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import tfar.damageenchantsmyinventory.platform.Services;

import java.util.ArrayList;
import java.util.List;

//There’s a small chance on hit to temporarily transform the target into a random passive mob (e.g., pig, sheep) for a few seconds.
public class PolymorphMobEffect extends UnCurableMobEffect {

    public PolymorphMobEffect(MobEffectCategory $$0, int $$1) {
        super($$0, $$1);
    }

    public static final List<EntityType<? extends LivingEntity>> TYPES = new ArrayList<>();

    @Override
    public void addAttributeModifiers(LivingEntity $$0, AttributeMap $$1, int $$2) {
        super.addAttributeModifiers($$0, $$1, $$2);
        Services.PLATFORM.morphIntoPassiveMob($$0);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity $$0, AttributeMap $$1, int $$2) {
        super.removeAttributeModifiers($$0, $$1, $$2);
        Services.PLATFORM.demorph($$0);
    }
}
