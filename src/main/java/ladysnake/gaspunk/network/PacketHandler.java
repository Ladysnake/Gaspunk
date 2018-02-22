package ladysnake.gaspunk.network;

import ladysnake.gaspunk.GasPunk;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class PacketHandler {
    public static final SimpleNetworkWrapper NET = NetworkRegistry.INSTANCE.newSimpleChannel(GasPunk.MOD_ID);
    private static int nextId;

    public static void initPackets() {
        NET.registerMessage(new BreathMessageHandler(), BreathMessage.class, nextId++, Side.CLIENT);
        NET.registerMessage(new ShaderMessageHandler(), ShaderMessage.class, nextId++, Side.CLIENT);
        NET.registerMessage(new SpecialRewardMessageHandler(), SpecialRewardMessage.class, nextId++, Side.SERVER);
    }
}
