package tfar.damageenchantsmyinventory;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.function.IntUnaryOperator;

public class SimpleEnchantment extends Enchantment {
    protected final int maxLevel;
    protected final IntUnaryOperator minCost;
    protected final IntUnaryOperator range;
    protected final PostAttack postAttack;
    protected final PostHurt postHurt;

    protected SimpleEnchantment(Properties properties) {
        super(properties.rarity,properties.category,properties.slots);
        this.maxLevel = properties.maxLevel;
        this.minCost = properties.minCost;
        this.range = properties.range;
        this.postAttack = properties.postAttack;
        this.postHurt = properties.postHurt;
    }

    @Override
    public int getMaxLevel() {
        return maxLevel;
    }

    @Override
    public int getMinCost(int level) {
        return minCost.applyAsInt(level);
    }

    @Override
    public int getMaxCost(int level) {
        return minCost.applyAsInt(level) + range.applyAsInt(level);
    }

    @Override
    public void doPostAttack(LivingEntity $$0, Entity $$1, int $$2) {
        postAttack.doPostAttack($$0, $$1, $$2);
    }

    @Override
    public void doPostHurt(LivingEntity $$0, Entity $$1, int $$2) {
        postHurt.doPostHurt($$0, $$1, $$2);
    }

    public static class Properties {
        final Rarity rarity;
        final EnchantmentCategory category;
        final EquipmentSlot[] slots;
        int maxLevel = 1;

        IntUnaryOperator minCost = level -> 1 + level * 10;
        IntUnaryOperator range = level -> 5;
        PostAttack postAttack = (attacker, target, enchantmentLevel) -> {};
        PostHurt postHurt = (user, attacker, level) -> {};

        public Properties(Rarity rarity, EnchantmentCategory category, EquipmentSlot... slots) {
            this.rarity = rarity;
            this.category = category;
            this.slots = slots;
        }

        public static Properties builder(Rarity rarity, EnchantmentCategory category, EquipmentSlot... slots) {
            return new Properties(rarity,category,slots);
        }

        public Properties maxLevel(int level) {
            this.maxLevel = level;
            return this;
        }

        public Properties minCost(IntUnaryOperator minCost) {
            this.minCost = minCost;
            return this;
        }

        public Properties range(IntUnaryOperator range) {
            this.range = range;
            return this;
        }

        public Properties postAttack(PostAttack postAttack) {
            this.postAttack = postAttack;
            return this;
        }

        public Properties postHurt(PostHurt postHurt) {
            this.postHurt = postHurt;
            return this;
        }

        public SimpleEnchantment build() {
            return new SimpleEnchantment(this);
        }
    }

    @FunctionalInterface
    public interface PostAttack {
        void doPostAttack(LivingEntity attacker, Entity target, int enchantmentLevel);
    }

    @FunctionalInterface
    public interface PostHurt {
        void doPostHurt(LivingEntity user, Entity attacker, int level);
    }

    public static final IntUnaryOperator SILK_TOUCH_RANGE = level -> 50;
    public static final IntUnaryOperator SILK_TOUCH_MIN = level -> 15;

}
