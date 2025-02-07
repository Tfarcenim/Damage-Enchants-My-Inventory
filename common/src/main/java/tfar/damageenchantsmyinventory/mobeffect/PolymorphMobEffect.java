package tfar.damageenchantsmyinventory.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

//There’s a small chance on hit to temporarily transform the target into a random passive mob (e.g., pig, sheep) for a few seconds.
public class PolymorphMobEffect extends UnCurableMobEffect {

    protected PolymorphMobEffect(MobEffectCategory $$0, int $$1) {
        super($$0, $$1);
    }

}
