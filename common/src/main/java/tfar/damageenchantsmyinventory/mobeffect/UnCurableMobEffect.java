package tfar.damageenchantsmyinventory.mobeffect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class UnCurableMobEffect extends MobEffect {
    public UnCurableMobEffect(MobEffectCategory $$0, int $$1) {
        super($$0, $$1);
    }

    /**
     * Get a fresh list of items that can cure this Potion.
     * All new PotionEffects created from this Potion will call this to initialize the default curative items
     * @see MobEffectInstance#getCurativeItems()
     * @return A list of items that can cure this Potion
     */
    @SuppressWarnings("unused")
    public List<ItemStack> getCurativeItems() {
        return List.of();
    }

}
