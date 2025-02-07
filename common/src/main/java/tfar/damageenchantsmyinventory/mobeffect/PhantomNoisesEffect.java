package tfar.damageenchantsmyinventory.mobeffect;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PhantomNoisesEffect extends UnCurableMobEffect {
    public PhantomNoisesEffect(MobEffectCategory $$0, int $$1) {
        super($$0, $$1);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 50 == 0;
    }

    //note, runs on client and server
    @Override
    public void applyEffectTick(LivingEntity livingEntity, int amplifier) {
        if (livingEntity.level().isClientSide) {
            Vec3 pos = getRandomPos(livingEntity);
            livingEntity.level().playLocalSound(pos.x,pos.y,pos.z, getRandom(livingEntity.getRandom()), livingEntity.getSoundSource(),1,1,false);
        }
    }

    public static Vec3 getRandomPos(LivingEntity entity) {
        return entity.position().offsetRandom(entity.getRandom(),8);
    }

    public static void setup() {
        Collections.addAll(PhantomNoisesEffect.POSSIBLE_SOUNDS,
                SoundEvents.ARROW_SHOOT,
                SoundEvents.ENDERMAN_AMBIENT,
                SoundEvents.GRASS_STEP,
                SoundEvents.PILLAGER_AMBIENT,
                SoundEvents.SKELETON_AMBIENT,
                SoundEvents.STONE_STEP,
                SoundEvents.WITCH_AMBIENT,
                SoundEvents.ZOMBIE_AMBIENT
        );
    }

    static SoundEvent getRandom(RandomSource random) {
        return POSSIBLE_SOUNDS.get(random.nextInt(POSSIBLE_SOUNDS.size()));
    }

    //Effect: Each successful shot against a target makes them hear phantom footsteps, bowshots, or mob noises around them for a short time.

    public static final List<SoundEvent> POSSIBLE_SOUNDS = new ArrayList<>();


}
