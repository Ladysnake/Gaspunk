package ladysnake.pathos.network;

import ladysnake.pathos.Pathos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public static final SimpleNetworkWrapper NET = NetworkRegistry.INSTANCE.newSimpleChannel(Pathos.MOD_ID);
    private static int nextId;

    public static void initPackets() {
        NET.registerMessage(new SicknessMessage.SicknessMessageHandler(), SicknessMessage.class, nextId++, Side.CLIENT);
    }
}
