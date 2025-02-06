package tfar.damageenchantsmyinventory.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;
import tfar.damageenchantsmyinventory.DamageEnchantsMyInventory;

public class ModLangProvider extends LanguageProvider {
    public ModLangProvider(PackOutput output) {
        super(output, DamageEnchantsMyInventory.MOD_ID, "en_us");
    }

    @Override
    protected void addTranslations() {
       // add(ModEntities.SOOSIG,"Soosig");
       // add(ModItems.SOOSIG_EGG,"Soosig Egg");
       // add(ModBlocks.GENE_INJECTOR,"Gene Injector");
    }
}
