package tfar.damageenchantsmyinventory;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class DEMIConfig {

    public final ForgeConfigSpec.DoubleValue poison_cloud_chance;
    public final ForgeConfigSpec.DoubleValue  life_leech_amount;
    public final ForgeConfigSpec.DoubleValue  volatile_harvest_chance;
    public final ForgeConfigSpec.DoubleValue cursed_mirror_chance;
    public final ForgeConfigSpec.DoubleValue  butterfingers_chance;
    public final ForgeConfigSpec.DoubleValue  misfire_chance;
    public final ForgeConfigSpec.DoubleValue  critical_misfire_chance;
    public final ForgeConfigSpec.DoubleValue  overflow_chance;
    public final ForgeConfigSpec.DoubleValue  phantom_stalker_chance;

    public static final DEMIConfig DEMI_CONFIG;
    public static final ForgeConfigSpec SERVER_SPEC;




    static {
        final Pair<DEMIConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(DEMIConfig::new);
        SERVER_SPEC = specPair.getRight();
        DEMI_CONFIG = specPair.getLeft();
    }

    public DEMIConfig(ForgeConfigSpec.Builder builder) {
        builder.push("enchantments");
        poison_cloud_chance = chance(builder,"poison_cloud_chance",.125);
        volatile_harvest_chance = chance(builder,"volatile_harvest_chance",.125);
        cursed_mirror_chance = chance(builder,"cursed_mirror_chance",.25);
        life_leech_amount = chance(builder,"life_leech_amount",.25);
        butterfingers_chance = chance(builder,"butterfingers_chance",.25);

        misfire_chance = chance(builder,"misfire_chance",.25);
        critical_misfire_chance = chance(builder,"critical_misfire_chance",.125);
        overflow_chance = chance(builder,"overflow_chance",.00390625);

        phantom_stalker_chance = chance(builder,"phantom_stalker_chance",.125);
        builder.pop();
    }

    static ForgeConfigSpec.DoubleValue chance(ForgeConfigSpec.Builder builder,String path,double defaultC) {
        return builder.defineInRange(path,defaultC,0,1);
    }

}
