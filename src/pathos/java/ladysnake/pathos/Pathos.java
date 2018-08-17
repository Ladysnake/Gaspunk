package ladysnake.pathos;

import ladysnake.pathos.network.PacketHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;

/*
  We might split this lib to a separate mod one day, which is why it's isolated from the main source set
 */
@Mod(modid = Pathos.MOD_ID, version = "@VERSION@")
public class Pathos {
    public static final String MOD_ID = "pathos";

    @Mod.EventHandler
    public static void init(FMLInitializationEvent event) {
        PacketHandler.initPackets();
    }

}
