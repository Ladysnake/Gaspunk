package ladysnake.gaspunk.init;

import ladysnake.gaspunk.GasPunk;
import ladysnake.pathos.Pathos;
import ladysnake.pathos.api.ISickness;
import ladysnake.pathos.sickness.Sickness;
import ladysnake.pathos.api.SicknessEffect;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@GameRegistry.ObjectHolder(GasPunk.MOD_ID)
@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
public class ModSicknesses {

    public static final ISickness TEAR_GAS = null;

    @SubscribeEvent
    public static void addRegistries(RegistryEvent.NewRegistry event) {
        Sickness.REGISTRY = (ForgeRegistry<ISickness>)new RegistryBuilder<ISickness>()
                .setType(ISickness.class)
                .setName(new ResourceLocation(Pathos.MOD_ID, "sicknesses"))
                .setDefaultKey(new ResourceLocation(Pathos.MOD_ID, "none"))
                .create();
    }

    @SubscribeEvent
    public static void addSicknesses(RegistryEvent.Register<ISickness> event) {
        Sickness.REGISTRY.register(new Sickness() {
            @Override
            public boolean performEffect(EntityLivingBase carrier, SicknessEffect effect) {
                // automatically remove this effect
                effect.setSeverity(0);
                return true;
            }
        }.setRegistryName("none"));
    }
}
