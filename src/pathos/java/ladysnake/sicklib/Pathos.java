package ladysnake.sicklib;

import ladysnake.sicklib.capability.CapabilitySickness;
import ladysnake.sicklib.item.ModItems;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/*
  We might split this lib to a separate mod one day, which is why it's isolated from the main source set
 */
public class Pathos {
    public static final String MOD_ID = "gaspunk";

    public static void preInit(FMLPreInitializationEvent event) {
        // Because apparently EventBusSubscriber is broken in this source set
        MinecraftForge.EVENT_BUS.register(ModItems.class);
        MinecraftForge.EVENT_BUS.register(CapabilitySickness.class);
    }
}
