package tfar.damageenchantsmyinventory.entity;

import com.google.common.collect.Lists;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Objects;

public class AreaEffectCloudOwnerImmune extends AreaEffectCloud {
    public AreaEffectCloudOwnerImmune(Level $$0, double $$1, double $$2, double $$3) {
        super($$0, $$1, $$2, $$3);
    }

    public AreaEffectCloudOwnerImmune(EntityType<? extends AreaEffectCloud> $$0, Level $$1) {
        super($$0, $$1);
    }

    public void tick() {
        //super.tick();
        baseTick();
        boolean waiting = this.isWaiting();
        float radius = this.getRadius();
        if (this.level().isClientSide) {
            if (waiting && this.random.nextBoolean()) {
                return;
            }

            ParticleOptions particleoptions = this.getParticle();
            int i;
            float f1;
            if (waiting) {
                i = 2;
                f1 = 0.2F;
            } else {
                i = Mth.ceil((float)Math.PI * radius * radius);
                f1 = radius;
            }

            for(int j = 0; j < i; ++j) {
                float f2 = this.random.nextFloat() * ((float)Math.PI * 2F);
                float f3 = Mth.sqrt(this.random.nextFloat()) * f1;
                double d0 = this.getX() + (double)(Mth.cos(f2) * f3);
                double d2 = this.getY();
                double d4 = this.getZ() + (double)(Mth.sin(f2) * f3);
                double d5;
                double d6;
                double d7;
                if (particleoptions.getType() == ParticleTypes.ENTITY_EFFECT) {
                    int k = waiting && this.random.nextBoolean() ? 0xffffff : this.getColor();
                    d5 = (float)(k >> 16 & 255) / 255.0F;
                    d6 = (float)(k >> 8 & 255) / 255.0F;
                    d7 = (float)(k & 255) / 255.0F;
                } else if (waiting) {
                    d5 = 0.0D;
                    d6 = 0.0D;
                    d7 = 0.0D;
                } else {
                    d5 = (0.5D - this.random.nextDouble()) * 0.15D;
                    d6 = 0.01F;
                    d7 = (0.5D - this.random.nextDouble()) * 0.15D;
                }

                this.level().addAlwaysVisibleParticle(particleoptions, d0, d2, d4, d5, d6, d7);
            }
        } else {
            if (this.tickCount >= this.getWaitTime() + this.getDuration()) {
                this.discard();
                return;
            }

            boolean idle = this.tickCount < this.getWaitTime();
            if (waiting != idle) {
                this.setWaiting(idle);
            }

            if (idle) {
                return;
            }

            if (this.getRadiusPerTick() != 0.0F) {
                radius += this.getRadiusPerTick();
                if (radius < 0.5F) {
                    this.discard();
                    return;
                }

                this.setRadius(radius);
            }

            if (this.tickCount % 5 == 0) {
                this.victims.entrySet().removeIf(entry -> this.tickCount >= entry.getValue());
                List<MobEffectInstance> list = Lists.newArrayList();

                for(MobEffectInstance mobeffectinstance : this.getPotion().getEffects()) {
                    list.add(new MobEffectInstance(mobeffectinstance.getEffect(), mobeffectinstance.mapDuration(i -> i / 4), mobeffectinstance.getAmplifier(), mobeffectinstance.isAmbient(), mobeffectinstance.isVisible()));
                }

                list.addAll(this.effects);
                if (list.isEmpty()) {
                    this.victims.clear();
                } else {
                    List<LivingEntity> potentialVictims = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox(), EntitySelector.NO_SPECTATORS.and(entity -> !Objects.equals(entity.getUUID(),ownerUUID)));
                    if (!potentialVictims.isEmpty()) {
                        for(LivingEntity victim : potentialVictims) {
                            if (!this.victims.containsKey(victim) && victim.isAffectedByPotions()) {
                                double xdist = victim.getX() - this.getX();
                                double zdist = victim.getZ() - this.getZ();
                                double distSq = xdist * xdist + zdist * zdist;
                                if (distSq <= (double)(radius * radius)) {
                                    this.victims.put(victim, this.tickCount + this.reapplicationDelay);

                                    for(MobEffectInstance mobeffectinstance1 : list) {
                                        if (mobeffectinstance1.getEffect().isInstantenous()) {
                                            mobeffectinstance1.getEffect().applyInstantenousEffect(this, this.getOwner(), victim, mobeffectinstance1.getAmplifier(), 0.5D);
                                        } else {
                                            victim.addEffect(new MobEffectInstance(mobeffectinstance1), this);
                                        }
                                    }

                                    if (this.getRadiusOnUse() != 0.0F) {
                                        radius += this.getRadiusOnUse();
                                        if (radius < 0.5F) {
                                            this.discard();
                                            return;
                                        }

                                        this.setRadius(radius);
                                    }

                                    if (this.getDurationOnUse() != 0) {

                                        setDuration(getDuration() + getDurationOnUse());

                                      //  this.duration += this.getDurationOnUse();
                                        if (this.getDuration() <= 0) {
                                            this.discard();
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
