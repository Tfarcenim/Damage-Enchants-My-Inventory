package tfar.damageenchantsmyinventory.init;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import tfar.damageenchantsmyinventory.mobeffect.PhantomNoisesEffect;
import tfar.damageenchantsmyinventory.mobeffect.PolymorphMobEffect;
import tfar.damageenchantsmyinventory.mobeffect.UnCurableMobEffect;

public class ModMobEffects {

    public static final MobEffect INVERTED_CONTROLS = new UnCurableMobEffect(MobEffectCategory.HARMFUL,0xff0000);
    public static final MobEffect PHANTOM_NOISES = new PhantomNoisesEffect(MobEffectCategory.HARMFUL,0x111111);
    public static final MobEffect POLYMORPH = new PolymorphMobEffect(MobEffectCategory.HARMFUL,0x660000);

}
