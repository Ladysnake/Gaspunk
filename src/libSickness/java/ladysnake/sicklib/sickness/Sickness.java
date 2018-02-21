package ladysnake.sicklib.sickness;

import ladysnake.sicklib.Pathos;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryBuilder;

@GameRegistry.ObjectHolder(Pathos.MOD_ID)
@Mod.EventBusSubscriber(modid = Pathos.MOD_ID)
public abstract class Sickness extends IForgeRegistryEntry.Impl<ISickness> implements ISickness {
    public static IForgeRegistry<ISickness> REGISTRY;

    public static final Sickness NONE = null;

    @SubscribeEvent
    public static void addRegistries(RegistryEvent.NewRegistry event) {
        REGISTRY = new RegistryBuilder<ISickness>()
                .setType(ISickness.class)
                .setDefaultKey(new ResourceLocation(Pathos.MOD_ID, "none"))
                .setName(new ResourceLocation(Pathos.MOD_ID, "gases"))
                .create();
    }

    @SubscribeEvent
    public static void addSicknesses(RegistryEvent.Register<ISickness> event) {
        REGISTRY.register(new Sickness() {
            @Override
            public boolean performEffect(EntityLivingBase carrier, SicknessEffect effect) {
                // automatically remove this effect
                effect.setSeverity(0);
                return true;
            }
        }.setRegistryName("none"));
    }

}
