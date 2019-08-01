package ladysnake.gaspunk.common.sickness;

import ladysnake.gaspunk.common.GasPunk;
import ladysnake.gaspunk.common.sickness.SicknessSarin;
import ladysnake.gaspunk.common.sickness.SicknessTearGas;
import ladysnake.pathos.Pathos;
import ladysnake.pathos.api.ISickness;
import ladysnake.pathos.api.SicknessEffect;
import ladysnake.pathos.sickness.Sickness;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@GameRegistry.ObjectHolder(GasPunk.MOD_ID)
@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
public class GasPunkSicknesses {

    public static final ISickness EYE_IRRITATION = null;

    @SubscribeEvent
    public static void addRegistries(RegistryEvent.NewRegistry event) {
        Sickness.REGISTRY = (ForgeRegistry<ISickness>)new RegistryBuilder<ISickness>()
                .setType(ISickness.class)
                .setName(new Identifier(Pathos.MOD_ID, "sicknesses"))
                .setDefaultKey(new Identifier(Pathos.MOD_ID, "none"))
                .create();
    }

    @SubscribeEvent
    public static void addSicknesses(RegistryEvent.Register<ISickness> event) {
        Sickness.REGISTRY.register(new Sickness() {
            @Override
            public boolean performEffect(LivingEntity carrier, SicknessEffect effect) {
                // automatically remove this effect
                effect.setSeverity(0);
                return true;
            }
        }.setRegistryName("none"));
        event.getRegistry().register(new SicknessTearGas().setRegistryName("eye_irritation"));
        event.getRegistry().register(new SicknessSarin().setRegistryName("lung_control_loss"));
    }
}
