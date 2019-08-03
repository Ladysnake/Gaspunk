package ladysnake.gaspunk.common.network;

import ladysnake.gaspunk.common.GasPunk;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class PacketHandler {
    public static final SimpleNetworkWrapper NET = NetworkRegistry.INSTANCE.newSimpleChannel(GasPunk.MOD_ID);
    private static int nextId;

    public static void initPackets() {
        NET.registerMessage(new SpecialRewardMessageHandler(), SpecialRewardMessage.class, nextId++, Side.SERVER);
    }
}
