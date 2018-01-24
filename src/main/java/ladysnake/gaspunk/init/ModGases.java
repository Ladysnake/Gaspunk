package ladysnake.gaspunk.init;

import ladysnake.gaspunk.GasPunk;
import ladysnake.gaspunk.gas.Gas;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

@Mod.EventBusSubscriber(modid = GasPunk.MOD_ID)
@GameRegistry.ObjectHolder(GasPunk.MOD_ID)
public class ModGases {

    public static final Gas AIR = new Gas();

    public static IForgeRegistry<Gas> REGISTRY;

    @SubscribeEvent
    public static void addRegistries(RegistryEvent.NewRegistry event) {
        REGISTRY = new RegistryBuilder<Gas>()
                .setType(Gas.class)
                .setName(new ResourceLocation(GasPunk.MOD_ID, "gases"))
                .setMaxID(255)
                .create();
    }

    @SubscribeEvent
    public static void addGases(RegistryEvent.Register<Gas> event) {
        event.getRegistry().registerAll(
                AIR.setRegistryName("air")
        );
    }
}
